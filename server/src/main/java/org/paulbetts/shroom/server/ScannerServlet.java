package org.paulbetts.shroom.server;

import com.dropbox.core.DbxAccountInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class ScannerServlet extends HttpServlet {
    static ObjectMapper om = new ObjectMapper();
    private static final Logger log = Logger.getLogger(ScannerServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("dropbox_token");
        if (token == null) {
            failRequestWith(400, "Bad Token", resp);
            return;
        }

        String memcacheKey = "scanner_" + token;
        MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();
        Object cachedResponse = memcache.get(memcacheKey);

        if (cachedResponse != null) {
            resp.setContentType("application/json");
            PrintWriter w = resp.getWriter();
            w.write((String) cachedResponse);
            w.close();
            return;
        }

        DbxClient client = new DbxClient(new DbxRequestConfig("ShroomService", "en", new AppengineHttpRequestor()), token);

        resp.setContentType("application/json");
        final PrintWriter writer = resp.getWriter();

        Observable<List<DbxEntry>> scans = searchForRoms(client, Arrays.asList("smc"))
                .publish()
                .refCount();

        final ArrayList<String> lines = new ArrayList<>();
        scans.subscribe(dbxEntries -> {
            for(DbxEntry e: dbxEntries) {
                try {
                    StringWriter sw = new StringWriter();
                    om.writeValue(sw, e);
                    String line = sw.toString().replace('\n', ' ') + "\n";
                    writer.write(line);
                    lines.add(line);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        scans.toBlocking().last();

        memcache.put(memcacheKey, join("", lines),
                Expiration.byDeltaSeconds(60), MemcacheService.SetPolicy.SET_ALWAYS);
        writer.close();
    }

    private Observable<List<DbxEntry>> searchForRoms(final DbxClient client, final List<String> extensions) {
        return Observable.create((Observer<? super List<DbxEntry>> op) -> {
            try {
                for(String ext: extensions) {
                    op.onNext(client.searchFileAndFolderNames("/", ext));
                }
            } catch (DbxException e) {
                op.onError(e);
            }

            op.onCompleted();
            return Subscriptions.empty();
        });
    }

    String join(String delimiter, List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for(String line: lines) {
            sb.append(line);
            sb.append(delimiter);
        }

        return sb.toString();
    }

    private Observable<List<DbxEntry>> parallelSearchForRoms(final DbxClient client, List<String> extensions) {
        // NB: This *should* be faster, but seems to hang for some reason.
        // Code left here for posterity.
        Observable<Observable<List<DbxEntry>>> scans =
                Observable.from(extensions)
                        .map(s -> RxAppEngine.deferStart(() -> {
                            try {
                                log.warning("Attempting search!");
                                List<DbxEntry> ret = client.searchFileAndFolderNames("/", s);

                                log.warning("Finished search!");
                                return ret;
                            } catch (DbxException e) {
                                throw new RuntimeException(e);
                            }
                        }));

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
