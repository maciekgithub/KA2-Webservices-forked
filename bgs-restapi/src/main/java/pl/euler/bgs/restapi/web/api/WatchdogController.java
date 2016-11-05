package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import pl.euler.bgs.restapi.web.api.params.ApiHeaders;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

@ApiController
public class WatchdogController {

    private final DatabaseService databaseService;

    @Autowired
    public WatchdogController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/wg")
    @Timed(name = "GET /wg")
    public ResponseEntity<JsonRawResponse> getWatchdog(ApiHeaders headers, RequestParams params) {
        return databaseService
                .executeRequestLogic(new DatabaseRequest("/wg", params, headers))
                .convertToWebResponse();
    }

}
