package pl.euler.bgs.restapi.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import pl.euler.bgs.restapi.core.tracking.TrackingAspect;

@Configuration
@EnableAspectJAutoProxy
public class TrackingConfiguration {

    @Bean
    @ConditionalOnProperty(name = "details-tracking", prefix = "app.metrics.logs", havingValue = "true")
    public TrackingAspect trackingAspect() {
        return new TrackingAspect();
    }
}
