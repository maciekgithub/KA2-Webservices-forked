package pl.euler.bgs.restapi.web.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.euler.bgs.restapi.web.api.params.RequestParams;

@RestController
public class TestNotificationsController {
    private static final Logger log = LoggerFactory.getLogger(TestNotificationsController.class);

    @PostMapping("/testnotification")
    @ResponseStatus(HttpStatus.OK)
    public void logNotification(RequestParams params, @RequestBody JsonNode json) {
        log.info("Test notification. Request params: {}, JSON: {}", params, json);
    }

}