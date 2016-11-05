package pl.euler.bgs.restapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.euler.bgs.restapi.web.api.params.RequestParamsResolver;
import pl.euler.bgs.restapi.web.api.params.ApiHeadersResolver;

import java.util.List;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ApiHeadersResolver());
        argumentResolvers.add(new RequestParamsResolver());
    }

}