package pl.euler.bgs.restapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import pl.euler.bgs.restapi.config.AppProperties;
import pl.euler.bgs.restapi.config.SwaggerConfiguration;

@SpringBootApplication
@Import(SwaggerConfiguration.class)
@EnableConfigurationProperties({AppProperties.class})
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	public void configureJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
		jackson2ObjectMapperBuilder.modulesToInstall(new KotlinModule());
	}

}
