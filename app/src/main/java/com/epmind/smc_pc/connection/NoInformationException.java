package com.epmind.smc_pc.connection;

/**
 * Created by gustavosmc on 16/12/15.
 */
public class NoInformationException extends Exception {
    public static final String msg = "Invalid Information Server";


    public NoInformationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NoInformationException(String detailMessage) {
        super(detailMessage);
    }

    public NoInformationException(Throwable throwable) {
        super(throwable);
    }

    public NoInformationException() {

    }
}
