package pl.euler.bgs.restapi.metrics;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.euler.bgs.restapi.web.common.JsonResponse;

@RestController
public class MetricsController {

    @PostMapping(value = "/metrics")
    public ResponseEntity<JsonResponse> getMetrics(@RequestBody JsonNode json) {
        //todo execute db procedure and retrieve the result
        return new ResponseEntity<>(new JsonResponse(json.toString()), HttpStatus.OK);
    }

}
