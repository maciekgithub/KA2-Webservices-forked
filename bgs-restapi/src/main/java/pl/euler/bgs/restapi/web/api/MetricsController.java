package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.web.api.headers.ApiHeaders;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;

@ApiController
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
    public ResponseEntity<JsonRawResponse> getDictionaries(ApiHeaders headers, RequestParams params) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/dictionaries", params, headers))
                .convertToWebResponse();
    }

    @PostMapping("/lists")
    @Timed(name = "POST /lists")
    @ApiOperation("Create metric")
    public ResponseEntity<JsonRawResponse> createList(ApiHeaders headers, RequestParams params, @RequestBody JsonNode json) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/lists", params, headers, json.toString()))
                .convertToWebResponse();
    }

    @RequestMapping(path = "/lists/{listName}", method = {RequestMethod.POST, RequestMethod.PUT})
    @Timed(name = "POST/PUT /lists/{listName}")
    @ApiOperation("Create new list without metric")
    public ResponseEntity<JsonRawResponse> createList(@PathVariable String listName, ApiHeaders headers, @RequestBody JsonNode json,
            RequestParams params) {

        String url = "/lists/" + listName;
        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, params, headers, json.toString()))
                .convertToWebResponse();
    }

    @GetMapping("/lists")
    @Timed(name = "GET /lists")
    @ApiOperation("Get list of subscriber lists")
    public ResponseEntity<JsonRawResponse> getSubscriberLists(HttpServletRequest request, ApiHeaders headers, RequestParams params) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/lists", params, headers))
                .convertToWebResponse();
    }

    @GetMapping("/lists/{listName}")
    @Timed(name = "GET /lists/{listName}")
    @ApiOperation("Get content of subscriber list")
    public ResponseEntity<JsonRawResponse> getSpecificSubscriberList(HttpServletRequest request, ApiHeaders headers, @PathVariable String listName,
            RequestParams params) {
        String url = "/lists/" + listName;
        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, params, headers))
                .convertToWebResponse();
    }


    @ApiOperation("Delete subscriber list")
    @Timed(name = "DELETE /lists")
    @DeleteMapping("/lists/{listName}")
    public ResponseEntity<JsonRawResponse> deleteSubscriberList(ApiHeaders headers, RequestParams params, @PathVariable String listName) {
        String url = "/lists/"+listName;
        return databaseService
                .executeRequestLogic(new DatabaseRequest(url, params, headers))
                .convertToWebResponse();
    }

}
