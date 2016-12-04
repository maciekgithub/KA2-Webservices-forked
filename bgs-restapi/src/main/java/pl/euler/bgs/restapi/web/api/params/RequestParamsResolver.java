package pl.euler.bgs.restapi.web.api.params;

import com.google.common.base.Charsets;
import javaslang.control.Option;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import pl.euler.bgs.restapi.web.api.Endpoint;

import java.nio.charset.Charset;
import java.util.Base64;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.net.URLDecoder.decode;
import static java.util.Objects.isNull;
import static javaslang.control.Try.of;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.entity.ContentType.WILDCARD;

/**
 * Resolver which checks the required api headers for each request and retrieve optional parameters.
 */
public class RequestParamsResolver implements HandlerMethodArgumentResolver {
    public static final String API_PREFIX = "/api";

    private final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private final String BASIC_AUTHENTICATION_PREFIX = "Basic";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RequestParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String userAgent = StringUtils.upperCase(webRequest.getHeader("User-Agent"));
        String date = webRequest.getHeader("Date");
        String accept = webRequest.getHeader("Accept");
        String password = extractBasicAuthenticationPassword(webRequest);

        if (isNull(userAgent) || isNull(date) || isNull(password)) {
            throw new MissingHeaderException("There is no User-Agent / Date / Authorization headers on the request!");
        }

        if (!isJsonOrWildcardContentType(firstNonNull(accept, APPLICATION_JSON.getMimeType()))) {
            throw new IncorrectHeaderException(String.format("We support only %s as response type!", APPLICATION_JSON.getMimeType()));
        }

        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Option<String> requestParamsOption = of(() -> decode(nativeRequest.getQueryString(), Charsets.UTF_8.name())).toOption();
        HttpMethod httpMethod = HttpMethod.resolve(nativeRequest.getMethod());
        String requestUrl = Endpoint.getEndpointUrl(nativeRequest);
        String schema = nativeRequest.getScheme();

        ApiHeaders apiHeaders = new ApiHeaders(userAgent, date, accept, schema, password);

        return new RequestParams(requestUrl, httpMethod, apiHeaders, requestParamsOption);
    }

    private boolean isJsonOrWildcardContentType(String contentType) {
        return APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType) || WILDCARD.getMimeType().equalsIgnoreCase(contentType);
    }

    private String extractBasicAuthenticationPassword(NativeWebRequest webRequest) {
        String base64Credentials = null;
        String authorizationHeader = webRequest.getParameter(AUTHORIZATION_HEADER_NAME);

        if (!isNull(authorizationHeader) && isBasicAuthentication(authorizationHeader)) {
            //TODO the default assumption is that we got Base64 credentials - to confirm with OPL
            base64Credentials = authorizationHeader.substring(BASIC_AUTHENTICATION_PREFIX.length()).trim();
        }

        return base64Credentials;
    }

    private boolean isBasicAuthentication(String authorizationHeader) {
        return authorizationHeader.startsWith(BASIC_AUTHENTICATION_PREFIX);
    }

}
