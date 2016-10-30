package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/api")
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);

    private final DatabaseService databaseService;

    @Autowired
    public ApiController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Timed(name = "GET /dictionaries")
    @GetMapping("/dictionaries")
    public ResponseEntity<JsonRawResponse> getDictionaries(ApiHeaders headers, HttpServletRequest request) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/dictionaries", HttpMethod.GET , headers))
                .convertToWebResponse();
    }

    @Timed(name = "POST /lists")
    @PostMapping("/lists")
    public ResponseEntity<JsonRawResponse> createList(ApiHeaders headers, @RequestBody JsonNode json) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/lists", HttpMethod.POST, headers, json.toString()))
                .convertToWebResponse();
    }

    @Timed(name = "GET /lists")
    @GetMapping("/lists/{listName}")
    public ResponseEntity<JsonRawResponse> getList(HttpServletRequest request, ApiHeaders headers, @PathVariable String listName) {

        String requestParams = Try.of(() -> URLDecoder.decode(request.getQueryString(), Charsets.UTF_8.name())).get();
        String url = "/lists/" + listName;

        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, HttpMethod.GET, requestParams, headers))
                .convertToWebResponse();
    }

    @Timed(name = "DELETE /lists")
    @DeleteMapping("/lists/{listName}")
    public ResponseEntity<JsonRawResponse> deleteAbonentList(ApiHeaders headers, @PathVariable String listName) {
        String url = "/lists/"+listName;
        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, HttpMethod.DELETE, headers))
                .convertToWebResponse();
    }

    @ExceptionHandler(value = {MissingHeaderException.class, IncorrectHeaderException.class})
    void handleBadRequests(HttpServletResponse response, MissingHeaderException exception) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

}
