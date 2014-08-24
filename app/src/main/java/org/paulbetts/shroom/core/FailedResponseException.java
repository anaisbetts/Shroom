package org.paulbetts.shroom.core;

import com.squareup.okhttp.Response;

/**
 * Created by paul on 8/23/14.
 */
public class FailedResponseException extends RuntimeException {
    private Response response;

    public FailedResponseException(Response response) {
        super("HTTP request failed with code: " + response.code());
        this.response = response;
    }

    public FailedResponseException(Response response, String detailMessage) {
        super(detailMessage);
        this.response = response;
    }

    public FailedResponseException(Response response, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.response = response;
    }

    public FailedResponseException(Response response, Throwable throwable) {
        super(throwable);
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
