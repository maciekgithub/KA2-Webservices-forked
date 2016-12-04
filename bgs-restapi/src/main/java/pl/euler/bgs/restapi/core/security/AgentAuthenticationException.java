package pl.euler.bgs.restapi.core.security;

import org.springframework.http.HttpStatus;
import pl.euler.bgs.restapi.web.common.HttpCodeException;

public class AgentAuthenticationException extends HttpCodeException {

    public AgentAuthenticationException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
