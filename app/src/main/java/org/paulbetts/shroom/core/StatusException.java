package org.paulbetts.shroom.core;

import com.google.android.gms.common.api.Status;

/**
 * Created by paul on 8/6/14.
 */
public class StatusException extends Exception {
    private final Status status;

    public StatusException(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
