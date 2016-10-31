package pl.euler.bgs.restapi.web.api;

import org.apache.http.entity.ContentType;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static java.util.Objects.isNull;

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
        String contentType = webRequest.getHeader("Content-Type");
        if (isNull(userAgent) || isNull(date) || isNull(contentType)) {
            throw new MissingHeaderException("There is no User-Agent / Date headers on the request!");
        }
        if (!ContentType.APPLICATION_JSON.getMimeType().equalsIgnoreCase(contentType)) {
            throw new IncorrectHeaderException(String.format("We accept only %s content type.", ContentType.APPLICATION_JSON.getMimeType()));
        }

        return new ApiHeaders(userAgent, date, contentType);
    }
}
