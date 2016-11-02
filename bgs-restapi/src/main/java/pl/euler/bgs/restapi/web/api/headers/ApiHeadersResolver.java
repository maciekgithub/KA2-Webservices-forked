package pl.euler.bgs.restapi.web.api.headers;

import com.google.common.base.MoreObjects;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static java.util.Objects.isNull;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;
import static org.apache.http.entity.ContentType.WILDCARD;

@Component
public class ApiHeadersResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(ApiHeaders.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        String userAgent = webRequest.getHeader("User-Agent");
        String date = webRequest.getHeader("Date");
        String accept = webRequest.getHeader("Accept");
        if (isNull(userAgent) || isNull(date)) {
            throw new MissingHeaderException("There is no User-Agent / Date headers on the request!");
        }
        String contentType = MoreObjects.firstNonNull(accept, APPLICATION_JSON.getMimeType());

        if (!isJsonOrWildcardContentType(contentType)) {
            throw new IncorrectHeaderException(String.format("We support only %s as response type!", APPLICATION_JSON.getMimeType()));
        }
        return new ApiHeaders(userAgent, date, accept);
    }

    private boolean isJsonOrWildcardContentType(String contentType) {
        return APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType) || WILDCARD.getMimeType().equalsIgnoreCase(contentType);
    }


}
