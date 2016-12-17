package pl.euler.bgs.restapi.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.euler.bgs.restapi.core.management.MaintenanceTriggerMode;
import pl.euler.bgs.restapi.web.api.params.IncorrectHeaderException;
import pl.euler.bgs.restapi.web.api.params.MissingHeaderException;
import pl.euler.bgs.restapi.web.management.MaintenanceController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(MaintenanceTriggerMode.class, new CaseInsensitiveEnumConverter<>(MaintenanceTriggerMode.class));
        dataBinder.registerCustomEditor(MaintenanceController.LogFileMode.class, new CaseInsensitiveEnumConverter<>(MaintenanceController.LogFileMode.class));
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, Object>> handleBasicException(HttpServletRequest request, Exception exception) throws IOException {
        return error(request, HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ResponseBody
    @ExceptionHandler(value = {MissingHeaderException.class, IncorrectHeaderException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequests(HttpServletRequest request, Exception exception) throws IOException {
        return error(request, HttpStatus.BAD_REQUEST, exception);
    }

    @ResponseBody
    @ExceptionHandler(value = {HttpCodeException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequests(HttpServletRequest request, HttpCodeException exception) throws IOException {
        return error(request, exception.getHttpStatus(), exception);
    }


    private ResponseEntity<Map<String, Object>> error(HttpServletRequest request, HttpStatus httpStatus, Throwable ex) {
        Map<String, Object> body = getErrorAttributes(new ServletRequestAttributes(request), httpStatus, ex);
        return new ResponseEntity<>(body, httpStatus);
    }


    private Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, HttpStatus httpStatus, Throwable ex) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        addStatus(errorAttributes, httpStatus);
        addErrorDetails(errorAttributes, ex);
        addPath(errorAttributes, requestAttributes);
        return errorAttributes;
    }

    private void addStatus(Map<String, Object> errorAttributes, HttpStatus status) {
        if (status == null) {
            errorAttributes.put("status", 999);
            errorAttributes.put("error", "None");
            return;
        }
        errorAttributes.put("status", status.value());
        try {
            errorAttributes.put("error", status.getReasonPhrase());
        } catch (Exception ex) {
            errorAttributes.put("error", "Http Status " + status);
        }
    }

    private void addErrorDetails(Map<String, Object> errorAttributes, Throwable ex) {
        errorAttributes.put("exception", ex.getClass().getName());
        errorAttributes.put("message", ex.getMessage());
    }

    private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

}
