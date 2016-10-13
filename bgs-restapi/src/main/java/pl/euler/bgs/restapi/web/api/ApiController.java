package pl.euler.bgs.restapi.web.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.euler.bgs.restapi.db.common.DatabaseResponse;
import pl.euler.bgs.restapi.db.common.DatabaseService;
import pl.euler.bgs.restapi.web.common.JsonResponse;

@RestController
@SuppressWarnings("unused")
public class ApiController {

    private final DatabaseService databaseService;

    @Autowired
    public ApiController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping(value = {"/metric", "/dictionary", "watchdog"})
    public ResponseEntity<JsonResponse> getMetrics(@RequestBody JsonNode json) {
        DatabaseResponse dbResponse = databaseService.executeRequestLogic("/metric", json.toString());
        return dbResponse.convertToWebResponse();
    }

}
