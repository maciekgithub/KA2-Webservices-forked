package pl.euler.bgs.restapi.web.api.params;

import com.google.common.base.Charsets;
import javaslang.control.Try;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URLDecoder;

public class RequestParamsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RequestParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest nativeRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String requestParams = Try.of(() -> URLDecoder.decode(nativeRequest.getQueryString(), Charsets.UTF_8.name())).orElse("");
        HttpMethod httpMethod = HttpMethod.resolve(nativeRequest.getMethod());
        return new RequestParams(httpMethod, requestParams);
    }
}
