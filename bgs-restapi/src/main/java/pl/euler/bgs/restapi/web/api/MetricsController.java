package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

@ApiController
@SuppressWarnings("unused")
public class MetricsController {
    private static final Logger log = LoggerFactory.getLogger(MetricsController.class);

    private final DatabaseService databaseService;

    @Autowired
    public MetricsController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/dictionaries")
    @Timed(name = "GET /dictionaries")
    @ApiOperation("Metric dictionary")
    public ResponseEntity<JsonRawResponse> getDictionaries(RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params)).convertToWebResponse();
    }

    @PostMapping("/lists")
    @Timed(name = "POST /lists")
    @ApiOperation("Create metric")
    public ResponseEntity<JsonRawResponse> createList(RequestParams params, @RequestBody JsonNode json) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params, json.toString())).convertToWebResponse();
    }

    @RequestMapping(path = "/lists/{listName}", method = {RequestMethod.POST, RequestMethod.PUT})
    @Timed(name = "POST/PUT /lists/{listName}")
    @ApiOperation("Create new list without metric")
    public ResponseEntity<JsonRawResponse> createList(@RequestBody JsonNode json, RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params, json.toString())).convertToWebResponse();
    }

    @GetMapping("/lists")
    @Timed(name = "GET /lists")
    @ApiOperation("Get list of subscriber lists")
    public ResponseEntity<JsonRawResponse> getSubscriberLists(RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params)).convertToWebResponse();
    }

    @GetMapping("/lists/{listName}")
    @Timed(name = "GET /lists/{listName}")
    @ApiOperation("Get content of subscriber list")
    public ResponseEntity<JsonRawResponse> getSpecificSubscriberList(RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params)).convertToWebResponse();
    }

    @ApiOperation("Delete subscriber list")
    @Timed(name = "DELETE /lists")
    @DeleteMapping("/lists/{listName}")
    public ResponseEntity<JsonRawResponse> deleteSubscriberList(RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params)).convertToWebResponse();
    }

}
