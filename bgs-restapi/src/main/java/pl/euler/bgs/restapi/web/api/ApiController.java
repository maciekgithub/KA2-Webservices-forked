package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/api")
public class ApiController {
    private static final Logger log = LoggerFactory.getLogger(ApiController.class);
    //todo probably we won't need that, just in case right now
    private final static String REQUEST_TYPE_DICTIONARIES_JSON = "{ \"request_type\": \"DICTIONARIES\" }";

    private final DatabaseService databaseService;

    @Autowired
    public ApiController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Timed(name = "/metric, /watchdog")
    @PostMapping(value = {"/metric", "watchdog"})
    public ResponseEntity<JsonRawResponse> getMetrics(HttpServletRequest request, @RequestBody JsonNode json, ApiHeaders headers) {
        String mapping = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE); //dynamic mapping determine
        DatabaseRequest dbRequest = new DatabaseRequest(mapping, "", headers, json.toString());
        return databaseService.executeRequestLogic(dbRequest).convertToWebResponse();
    }

    @Timed(name = "/dictionaries")
    @GetMapping("/dictionaries")
    public ResponseEntity<JsonRawResponse> getDictionaries(ApiHeaders headers) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/dictionaries", "", headers, REQUEST_TYPE_DICTIONARIES_JSON))
                .convertToWebResponse();
    }

    @ExceptionHandler(MissingHeaderException.class)
    void handleBadRequests(HttpServletResponse response, MissingHeaderException exception) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

}
