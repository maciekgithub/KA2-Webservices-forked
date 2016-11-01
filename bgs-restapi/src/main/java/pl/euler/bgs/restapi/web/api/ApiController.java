package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Charsets;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/dictionaries")
    @Timed(name = "GET /dictionaries")
    @ApiOperation("Metric dictionary")
    public ResponseEntity<JsonRawResponse> getDictionaries(ApiHeaders headers, HttpServletRequest request) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/dictionaries", HttpMethod.GET , headers))
                .convertToWebResponse();
    }

    @PostMapping("/lists/{listName}")
    @Timed(name = "POST /lists/{listName}")
    @ApiOperation("Create new list without metric")
    public ResponseEntity<JsonRawResponse> createList(@PathVariable String listName, ApiHeaders headers, @RequestBody JsonNode json) {
        String url = "/lists/" + listName;
        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, HttpMethod.POST, headers, json.toString()))
                .convertToWebResponse();
    }

    @GetMapping("/lists/{listName}")
    @Timed(name = "GET /lists/{listName}")
    @ApiOperation("Get content of subscriber list")
    public ResponseEntity<JsonRawResponse> getSpecificSubscriberList(HttpServletRequest request, ApiHeaders headers, @PathVariable String listName) {
        String requestParams = Try.of(() -> URLDecoder.decode(request.getQueryString(), Charsets.UTF_8.name())).get();
        String url = "/lists/" + listName;
        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, HttpMethod.GET, requestParams, headers))
                .convertToWebResponse();
    }

    @GetMapping("/lists")
    @Timed(name = "GET /lists")
    @ApiOperation("Get list of subscriber lists")
    public ResponseEntity<JsonRawResponse> getSubscriberLists(HttpServletRequest request, ApiHeaders headers) {
        String requestParams = Try.of(() -> URLDecoder.decode(request.getQueryString(), Charsets.UTF_8.name())).orElse("");
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/lists", HttpMethod.GET, requestParams, headers))
                .convertToWebResponse();
    }

    @ApiOperation("Delete subscriber list")
    @Timed(name = "DELETE /lists")
    @DeleteMapping("/lists/{listName}")
    public ResponseEntity<JsonRawResponse> deleteSubscriberList(ApiHeaders headers, @PathVariable String listName) {
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
