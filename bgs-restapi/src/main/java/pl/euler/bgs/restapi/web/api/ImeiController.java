package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
@Api(value = "BGS REST API", description = "BGS REST API Endpoints")
public class ImeiController {

    @PostMapping("/imei")
    @Timed(name = "POST /imei")
    @ApiOperation("Notification from IMEI Tracking")
    public void imeiNotification() {
        //todo implement
    }

}
