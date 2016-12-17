package pl.euler.bgs.restapi.web.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.euler.bgs.restapi.web.api.params.RequestParams;

@Controller
public class TestNotificationsController {
    private static final Logger log = LoggerFactory.getLogger(TestNotificationsController.class);


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/testnotification", method = RequestMethod.POST)
    public void logNotification(RequestParams params, @RequestBody JsonNode json) {
        log.info("Test notification. Request params: {}, JSON: {}", params, json);
    }

}
