package org.paulbetts.shroom.server;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;

import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.util.async.Async;

public class ScannerServlet extends HttpServlet {
    static ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("dropbox_token");
        if (token == null) {
            failRequestWith(400, "Bad Token", resp);
            return;
        }

        DbxClient client = new DbxClient(new DbxRequestConfig("ShroomService", "en", new AppengineHttpRequestor()), token);

        resp.setContentType("application/json");
        final PrintWriter writer = resp.getWriter();

        Observable<List<DbxEntry>> scans = searchForRoms(client, Arrays.asList("smc"))
                .publish()
                .refCount();

        scans.subscribe(new Action1<List<DbxEntry>>() {
            @Override
            public void call(List<DbxEntry> dbxEntries) {
                for(DbxEntry e: dbxEntries) {
                    try {
                        StringWriter sw = new StringWriter();
                        om.writeValue(sw, e);
                        writer.write(sw.toString().replace('\n', ' ') + "\n");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        scans.toBlocking().last();
        writer.close();
    }

    private Observable<List<DbxEntry>> searchForRoms(final DbxClient client, List<String> extensions) {
        Observable<Observable<List<DbxEntry>>> scans = Observable.from(extensions)
                .map(new Func1<String, Observable<List<DbxEntry>>>() {
                    @Override
                    public Observable<List<DbxEntry>> call(final String s) {
                        return RxAppEngine.deferStart(new Func0<List<DbxEntry>>() {
                            @Override
                            public List<DbxEntry> call() {
                                try {
                                    return client.searchFileAndFolderNames("/", s);
                                } catch (DbxException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                });

        return Observable.merge(scans, 8);
    }

    private void failRequestWith(int code, String message, HttpServletResponse resp) throws IOException {
        Writer writer = resp.getWriter();

        resp.setContentType("application/json");
        resp.setStatus(code);
        om.writeValue(writer, new ErrorMessage(message));

        writer.close();
    }

    class ErrorMessage {
        ErrorMessage(String message) {
            this.message = message;
        }

        public String message;
    }
}
