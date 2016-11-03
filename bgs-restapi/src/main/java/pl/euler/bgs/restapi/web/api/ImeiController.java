package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.euler.bgs.restapi.web.api.headers.ApiHeaders;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
@Api(value = "BGS REST API", description = "BGS REST API Endpoints")
public class ImeiController {

    private final DatabaseService databaseService;

    @Autowired
    public ImeiController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/imei")
    @Timed(name = "POST /imei")
    @ApiOperation("Notification from IMEI Tracking")
    public ResponseEntity<JsonRawResponse> imeiNotification(ApiHeaders headers, @RequestBody JsonNode json, RequestParams params) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/imei", params, headers, json.toString()))
                .convertToWebResponse();
    }

}
