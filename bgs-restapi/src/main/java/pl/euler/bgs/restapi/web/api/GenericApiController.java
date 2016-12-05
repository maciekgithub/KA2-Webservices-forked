package pl.euler.bgs.restapi.web.api;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
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
import pl.euler.bgs.restapi.core.acl.EndpointsRepository;
import pl.euler.bgs.restapi.core.security.*;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver;
import pl.euler.bgs.restapi.web.common.BadRequestException;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(RequestParamsResolver.API_PREFIX)
@Api(value = "BGS REST API", description = "BGS REST API Endpoints")
public class GenericApiController {
    private static final Logger log = LoggerFactory.getLogger(GenericApiController.class);

    private final GenericApiService apiService;
    private final SecurityService securityService;
    private final EndpointsRepository endpointsRepository;

    @Autowired
    public GenericApiController(GenericApiService apiService, SecurityService securityService, EndpointsRepository endpointsRepository) {
        this.apiService = apiService;
        this.securityService = securityService;
        this.endpointsRepository = endpointsRepository;
    }

    @RequestMapping(value = "/**", method = {GET, PUT, POST, DELETE})
    @Timed(name = "Generic API request...")
    public ResponseEntity<JsonRawResponse> getDictionaries(RequestParams params, @RequestBody(required = false) JsonNode json,
            HttpServletRequest request) {

        Collection<Endpoint> endpoints = endpointsRepository.getRegisteredEndpoints();
        Endpoint matchedEndpoint = getMatchedEndpoint(request, endpoints);
        log.info("Matched endpoint {} for query {}", matchedEndpoint, request.getRequestURL());

        Option<JsonNode> optionalJson = Option.of(json);
        if (matchedEndpoint.getHttpMethod().equals(HttpMethod.POST) && optionalJson.isEmpty()) {
            throw new BadRequestException("The json body is required for POST request!");
        }

        securityService.authenticate(params, matchedEndpoint);

        return apiService.executeRequestLogic(new DatabaseRequest(params, optionalJson)).convertToWebResponse();
    }

    //todo move to generic api service
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
