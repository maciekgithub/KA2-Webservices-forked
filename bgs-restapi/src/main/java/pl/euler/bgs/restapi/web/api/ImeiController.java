package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiOperation;
import javaslang.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

@ApiController
@SuppressWarnings("unused")
public class ImeiController {

    private final DatabaseService databaseService;

    @Autowired
    public ImeiController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/imei")
    @Timed(name = "POST /imei")
    @ApiOperation("Notification from IMEI Tracking")
    public ResponseEntity<JsonRawResponse> imeiNotification(@RequestBody JsonNode json, RequestParams params) {
        return databaseService.executeRequestLogic(new DatabaseRequest(params, Option.of(json))).convertToWebResponse();
    }

}
