package pl.euler.bgs.restapi.web.common;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Exception which return {@link org.springframework.http.HttpStatus#BAD_REQUEST} status within exception.
 */
public class BadRequestException extends HttpCodeException {

    public BadRequestException(String message) {
        super(BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(BAD_REQUEST, message, cause);
    }

}
