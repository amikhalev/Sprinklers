package org.amikhalev.sprinklers.dto;

import com.google.common.base.Throwables;

/**
 * Created by alex on 5/5/15.
 */
public class UncaughtExceptionObject {
    private String message;
    private String stackTrace;

    public UncaughtExceptionObject() {
    }

    public UncaughtExceptionObject(Throwable e) {
        this.message = e.getMessage();
        this.stackTrace = Throwables.getStackTraceAsString(e);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return message + '\n' + stackTrace;
    }
}
