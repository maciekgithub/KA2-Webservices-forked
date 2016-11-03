package pl.euler.bgs.restapi.config;

import com.google.common.base.Predicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.ant;

@Controller
@EnableSwagger2
@SuppressWarnings("unused")
@ConditionalOnProperty(value = "app.swagger.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfiguration {

    @Bean
    public Docket bgsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("bgs-api")
                .apiInfo(apiInfo())
                .select()
                .paths(bgsPaths())
                .apis(RequestHandlerSelectors.any())
                .build()
                .pathMapping("/")
                .genericModelSubstitutes(ResponseEntity.class)
                .enableUrlTemplating(true);
    }

    @SuppressWarnings({"Guava", "unchecked"})
    private Predicate<String> bgsPaths() {
        return ant("/api/**");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("BGS REST API")
                .description("REST API dla systemu BGS.")
                .build();
    }

    @GetMapping("/api-docs")
    public String swagger() {
        return "redirect:swagger-ui.html";
    }

}
