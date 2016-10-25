package pl.euler.bgs.restapi.web.api;

import org.apache.http.entity.ContentType;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.google.common.base.MoreObjects.firstNonNull;
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
        if (isNull(userAgent) || isNull(date)) {
            throw new MissingHeaderException("There is no User-Agent / Date headers on the request!");
        }
        //todo accept only the application json
        String contentType = firstNonNull(webRequest.getHeader("Content-Type"), ContentType.APPLICATION_JSON.getMimeType());

        return new ApiHeaders(userAgent, date, contentType);
    }
}
