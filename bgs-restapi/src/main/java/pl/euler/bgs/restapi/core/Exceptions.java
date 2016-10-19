package pl.euler.bgs.restapi.core;

import java.util.function.Function;

public class Exceptions {

    public static Function<Throwable, RuntimeException> toIllegalStateException(String message) {
        return throwable -> new IllegalStateException(message, throwable);
    }

}
