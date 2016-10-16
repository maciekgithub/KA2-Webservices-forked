package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;

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

    @Timed(name = "/metric, /dictionary, /watchdog")
    @PostMapping(value = {"/metric", "/dictionary", "watchdog"})
    public ResponseEntity<JsonRawResponse> getMetrics(HttpServletRequest request, @RequestBody JsonNode json) {
        String mapping = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE); //dynamic mapping determine
        log.info("request: {}, request type: {}, agent_name: {}, request_timestamp: {} ", mapping,
                 json.get("request_type"), json.get("agent_name"), json.get("request_timestamp"));
        DatabaseResponse dbResponse = databaseService.executeRequestLogic(mapping, json.toString());
        log.info("response: {}, response code: {}, response json: {}", mapping, dbResponse.getStatusCode(), dbResponse.getJson());
        return dbResponse.convertToWebResponse();
    }

}
