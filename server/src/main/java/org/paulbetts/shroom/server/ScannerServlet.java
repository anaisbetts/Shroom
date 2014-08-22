package org.paulbetts.shroom.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.api.server.spi.config.Api;
import com.google.appengine.repackaged.org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by paul on 8/21/14.
 */
public class ScannerServlet extends HttpServlet {
    static ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("dropbox_token");
        if (token == null) {
            failRequestWith(400, "Bad Token", resp);
            return;
        }

        resp.setContentType("text/plain");
        resp.getWriter().println("Hello!");
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
