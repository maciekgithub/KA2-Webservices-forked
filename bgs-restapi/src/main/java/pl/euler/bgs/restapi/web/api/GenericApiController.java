package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import javaslang.collection.List;
import javaslang.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver;
import pl.euler.bgs.restapi.web.common.BadRequestException;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@SuppressWarnings("unused")
@RequestMapping(RequestParamsResolver.GAPI_PREFIX)
public class GenericApiController {
    private static final Logger log = LoggerFactory.getLogger(GenericApiController.class);

    private final DatabaseService databaseService;

    @Autowired
    public GenericApiController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @RequestMapping(value = "/**", method = {GET, PUT, POST, DELETE})
    @Timed(name = "GAPI")
    public ResponseEntity<JsonRawResponse> getDictionaries(RequestParams params, @RequestBody(required = false) JsonNode json,
            HttpServletRequest request) {

        Collection<Endpoint> endpoints = databaseService.getRegisteredEndpoints(RequestParamsResolver.GAPI_PREFIX);
        Endpoint matchedEndpoint = getMatchedEndpoint(request, endpoints);
        log.info("Matched endpoint {} for query {}", matchedEndpoint, request.getRequestURL());

        Option<JsonNode> optionalJson = Option.of(json);
        if (matchedEndpoint.getHttpMethod().equals(HttpMethod.POST) && optionalJson.isEmpty()) {
            throw new BadRequestException("The json body is required for POST request!");
        }

        return databaseService.executeRequestLogic(new DatabaseRequest(params, optionalJson)).convertToWebResponse();
    }

    private Endpoint getMatchedEndpoint(HttpServletRequest request, Collection<Endpoint> endpoints) {
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());

        boolean endsWithSlash = Endpoint.isPathEndsWithSlash(request);
        // if request path ends with slash we'are adding the slash to the saved endpoint to match it
        List<Endpoint> matchedEndpoints = List.ofAll(endpoints)
                .filter(e -> new AntPathRequestMatcher(endsWithSlash ? e.getUrl() + "/" : e.getUrl(), e.getHttpMethod().name()).matches(request));

        if (matchedEndpoints.isEmpty()) {
            throw new BadRequestException("There is no matching endpoint for provided request!");
        }
        if (matchedEndpoints.length() > 1) {
            log.warn("There is more that one endpoint for query. Query: {}, matched endpoints: {}", request.getRequestURL(), matchedEndpoints);
            throw new BadRequestException("There is more that one matching endpoint for request. Matching endpoints: " + matchedEndpoints);
        }
        return matchedEndpoints.head();
    }

}
