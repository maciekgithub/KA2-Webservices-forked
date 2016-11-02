package pl.euler.bgs.restapi.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import pl.euler.bgs.restapi.core.management.MaintenanceTriggerMode;
import pl.euler.bgs.restapi.web.api.headers.IncorrectHeaderException;
import pl.euler.bgs.restapi.web.api.headers.MissingHeaderException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
@SuppressWarnings("unused")
public class GlobalControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(MaintenanceTriggerMode.class, new CaseInsensitiveEnumConverter<>(MaintenanceTriggerMode.class));
    }

    /**
     * Exception handler for exceptions regarding headers!
     */
    @ExceptionHandler(value = {MissingHeaderException.class, IncorrectHeaderException.class})
    void handleBadRequests(HttpServletResponse response, MissingHeaderException exception) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    /**
     * Exception handler which returns specific error code based on prepared exception.
     */
    @ExceptionHandler(value = {HttpCodeException.class})
    void handleBadRequests(HttpServletResponse response, HttpCodeException exception) throws IOException {
        response.sendError(exception.getHttpStatus().value(), exception.getMessage());
    }

}
