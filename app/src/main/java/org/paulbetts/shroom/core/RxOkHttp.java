package org.paulbetts.shroom.core;

import android.os.AsyncTask;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by paul on 8/23/14.
 */
public class RxOkHttp {
    public static Observable<Response> request(OkHttpClient client, Request request) {
        return Observable.create((Subscriber<? super Response> subj) -> {
            final Call call = client.newCall(request);

            subj.add(Subscriptions.create(() -> call.cancel()));

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    subj.onError(e);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Throwable error = getFailureExceptionOnBadStatus(response);
                    if (error != null) {
                        subj.onError(error);
                        return;
                    }

                    subj.onNext(response);
                    subj.onCompleted();
                }
            });
        });
    }

    public static Observable<byte[]> streamBytes(OkHttpClient client, Request request) {
        return request(client, request)
                .flatMap(response -> Observable.create((Subscriber<? super byte[]> subj) -> {
                    AsyncTask t = new AsyncTask() {
                        @Override
                        protected Void doInBackground(Object[] objects) {
                            InputStream stream;
                            byte[] buffer = new byte[65536];
                            int bytesRead = 0;

                            stream = response.body().byteStream();
                            try {
                                while (bytesRead > -1 && !subj.isUnsubscribed()) {
                                    bytesRead = stream.read(buffer, 0, 65536);
                                    if (bytesRead == 0) continue;

                                    subj.onNext(Arrays.copyOfRange(buffer, 0, bytesRead));
                                }

                                if (!subj.isUnsubscribed()) subj.onCompleted();
                                stream.close();
                            } catch (IOException ex) {
                                subj.onError(ex);
                            }

                            return null;
                        }
                    };

                    subj.add(Subscriptions.create(() -> t.cancel(false)));
                    t.execute();
                }));
    }

    public static Observable<String> streamStrings(OkHttpClient client, Request request) {
        return streamBytes(client, request)
                .map(bytes -> {
                    try {
                        return new String(bytes, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException("UTF8 isn't supported this will never happen");
                    }
                });
    }

    public static Observable<String> streamLines(OkHttpClient client, Request request) {
        return streamStrings(client, request)
                .concatWith(Observable.just("\n"))
                .flatMap(new Func1<String, Observable<? extends String>>() {
                    String remainingString = "";

                    @Override
                    public Observable<? extends String> call(String s) {
                        String[] lines = (remainingString + s).split("\n");
                        if (lines[lines.length - 1].length() != 0) {
                            remainingString = lines[lines.length - 1];

                            return Observable.from(Arrays.copyOfRange(lines, 0, lines.length - 2));
                        }

                        remainingString = "";
                        return Observable.from(lines);
                    }
                });
    }

    private static Throwable getFailureExceptionOnBadStatus(Response resp) {
        if (resp.code() < 399) return null;
        return new FailedResponseException(resp);
    }
}
