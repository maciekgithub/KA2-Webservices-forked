package pl.euler.bgs.restapi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import pl.euler.bgs.restapi.internal.GenapiProperties;

@SpringBootApplication
@EnableConfigurationProperties(GenapiProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	public void configureJackson(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
		jackson2ObjectMapperBuilder.modulesToInstall(new KotlinModule());
	}

}
