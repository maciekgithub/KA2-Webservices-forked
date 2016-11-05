package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import javaslang.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

@ApiController
@SuppressWarnings("unused")
public class WatchdogController {

    private final DatabaseService databaseService;

    @Autowired
    public WatchdogController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping(path = { "/wg", "/wg/{wgName}"})
    @Timed(name = "GET /wg/...")
    public ResponseEntity<JsonRawResponse> get(RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params)).convertToWebResponse();
    }

    @PostMapping(path = {"/wg", "/wg/{wgName}", "/wg/{wgName}/subscriptions", "/wg/{wgName}/subscriptions/{msisdn}"})
    @Timed(name = "POST /wg/...")
    public ResponseEntity<JsonRawResponse> post(RequestParams params, @RequestBody JsonNode json) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params, Option.of(json))).convertToWebResponse();
    }

    @PutMapping(path = {"/wg/{wgName}/start", "/wg/{wgName}/stop"})
    @Timed(name = "PUT /wg/{wgName}/start|stop")
    public ResponseEntity<JsonRawResponse> put(RequestParams params, @RequestBody(required = false) JsonNode json) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params, Option.of(json))).convertToWebResponse();
    }

    @DeleteMapping(path = { "/wg/{wgName}", "/wg/{wgName}/subscriptions/{msisdn}"})
    @Timed(name = "DELETE /wg/...")
    public ResponseEntity<JsonRawResponse> delete(RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params)).convertToWebResponse();
    }

}
