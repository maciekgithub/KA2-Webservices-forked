package pl.euler.bgs.restapi.web.common;

import org.springframework.http.HttpStatus;

/**
 * Exception which return specific HTTP response code.
 */
public class HttpCodeException extends RuntimeException {
    private HttpStatus httpStatus;

    public HttpCodeException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpCodeException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
