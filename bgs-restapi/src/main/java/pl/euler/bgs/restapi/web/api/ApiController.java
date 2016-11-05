package pl.euler.bgs.restapi.web.api;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver;

import java.lang.annotation.*;

/**
 * Annotation for mark the /api endpoints.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping(RequestParamsResolver.API_PREFIX)
@Api(value = "BGS REST API", description = "BGS REST API Endpoints")
@interface ApiController {
}
