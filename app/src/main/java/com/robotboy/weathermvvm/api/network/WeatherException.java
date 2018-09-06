package com.robotboy.weathermvvm.api.network;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.concurrent.TimeoutException;

public class WeatherException extends Throwable {
    private final Integer code;
    private static final int SERVICE_UNAVAILABLE = -1;
    private static final int SERVICE_ERROR = -1;
    private static final int UNKNOWN_ERROR = -2;

    private WeatherException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static WeatherException create(int code) {
        if (code == 404) {
            return new WeatherException(SERVICE_ERROR, new IOException("404"));
        } else {
            return new WeatherException(UNKNOWN_ERROR, new UnknownServiceException());
        }
    }

    public static WeatherException create(Throwable cause) {
        if (cause instanceof TimeoutException || cause instanceof SocketTimeoutException || cause instanceof UnknownHostException || cause instanceof IOException) {
            return new WeatherException(SERVICE_UNAVAILABLE, cause);
        } else if (cause instanceof WeatherException) {
            return (WeatherException) cause;
        } else if (cause.getMessage() != null && !cause.getMessage().isEmpty()) {
            return new WeatherException(UNKNOWN_ERROR, cause);
        } else {
            return new WeatherException(UNKNOWN_ERROR, cause);
        }
    }
}
