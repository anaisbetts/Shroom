package org.paulbetts.shroom.server;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;

import org.paulbetts.shroom.models.RomInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public class ScannerServlet extends HttpServlet {
    static ObjectMapper om = new ObjectMapper();
    private static final Logger log = Logger.getLogger(ScannerServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getHeader("Authorization");
        if (token == null) {
            failRequestWith(400, "Bad Token", resp);
            return;
        }

        token = token.replace("Bearer ", "");

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

        Observable<List<RomInfo>> scans = searchForRoms(client, Arrays.asList("smc"))
                .publish()
                .refCount();

        final ArrayList<String> lines = new ArrayList<>();
        scans.subscribe(roms -> {
            for(RomInfo e: roms) {
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
        }, ex -> {
            log.log(Level.INFO, "Dropbox scan failed", ex);
        });

        try {
            scans.toBlocking().last();
        } catch (Exception ex) {
            failRequestWith(400, "Invalid Dropbox Token", resp);
            return;
        }

        memcache.put(memcacheKey, joinStrings("", lines),
                Expiration.byDeltaSeconds(60), MemcacheService.SetPolicy.SET_ALWAYS);
        writer.close();
    }

    private Observable<List<RomInfo>> searchForRoms(final DbxClient client, final List<String> extensions) {
        return Observable.create((Subscriber<? super List<RomInfo>> op) -> {
            try {
                for (String ext : extensions) {
                    if (op.isUnsubscribed()) break;

                    ArrayList<RomInfo> ret = new ArrayList<>();
                    for(DbxEntry e: client.searchFileAndFolderNames("/", ext)) {
                        // NB: There's no reason that we won't get unrelated files
                        // that happen to have 'smc' et al in the name as a token.
                        if (!e.name.endsWith(ext) || !e.isFile()) return;

                        ret.add(dropboxToRomInfo(e));
                    }

                    op.onNext(ret);
                }
            } catch (DbxException e) {
                op.onError(e);
            }

            op.onCompleted();
        });
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

    private RomInfo dropboxToRomInfo(DbxEntry entry) {
        return new RomInfo(entry.name, "", "", entry.path);
    }

    String joinStrings(String delimiter, List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for(String line: lines) {
            sb.append(line);
            sb.append(delimiter);
        }

        return sb.toString();
    }

}
