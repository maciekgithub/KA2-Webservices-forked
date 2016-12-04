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
import pl.euler.bgs.restapi.core.security.*;
import pl.euler.bgs.restapi.web.api.params.RequestParams;
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver;
import pl.euler.bgs.restapi.web.common.BadRequestException;
import pl.euler.bgs.restapi.web.common.JsonRawResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Objects;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@SuppressWarnings("unused")
@RestController
@RequestMapping(RequestParamsResolver.API_PREFIX)
@Api(value = "BGS REST API", description = "BGS REST API Endpoints")
public class GenericApiController {
    private static final Logger log = LoggerFactory.getLogger(GenericApiController.class);

    private final DatabaseService databaseService;
    private final SecurityService securityService;

    @Autowired
    public GenericApiController(DatabaseService databaseService, SecurityService securityService) {
        this.databaseService = databaseService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/**", method = {GET, PUT, POST, DELETE})
    @Timed(name = "Generic API request...")
    public ResponseEntity<JsonRawResponse> getDictionaries(RequestParams params, @RequestBody(required = false) JsonNode json,
            HttpServletRequest request) {

        //todo agent backdoor-a (dostęp do wszystkich endpointów niezależnie od innych wpisów)

        Agent agent = securityService.getAgentDetails(params.getHeaders().getUserAgent());
        if (Objects.isNull(agent)) {
            throw new AgentAuthenticationException(UNAUTHORIZED, "Agent has not been authorized.");
        }

        SecurityRequest securityRequest = createSecurityRequest(params);
        authenticateAgent(agent, securityRequest);

        Collection<Endpoint> endpoints = databaseService.getRegisteredEndpoints();
        Endpoint matchedEndpoint = getMatchedEndpoint(request, endpoints);
        log.info("Matched endpoint {} for query {}", matchedEndpoint, request.getRequestURL());

        if (securityService.isAgentAuthorizedToInvokeEndpoint(agent, matchedEndpoint)) {
            throw new AgentAuthenticationException(UNAUTHORIZED, "Agent has no access to this endpoint.");
        }

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

    private SecurityRequest createSecurityRequest(RequestParams requestParams) {
//        return new SecurityRequest(requestParams.getHeaders().getSchema(), requestParams.getHeaders())
        return null;
    }

    private void authenticateAgent(Agent agent, SecurityRequest securityRequest) {
        AgentSecurityStatus agentSecurityStatus = securityService.authenticateAgent(agent, securityRequest);

        switch (agentSecurityStatus) {
            case INCORRECT_PASSWORD:
                throw new AgentAuthenticationException(FORBIDDEN, "Authentication failed. Check credentials.");
            case SSL_REQUIRED:
                throw new AgentAuthenticationException(FORBIDDEN, "Secure communication is required.");
        }
    }
}
