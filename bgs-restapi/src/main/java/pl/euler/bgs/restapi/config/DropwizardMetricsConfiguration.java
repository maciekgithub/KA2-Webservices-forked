package pl.euler.bgs.restapi.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics(proxyTargetClass = true)
public class DropwizardMetricsConfiguration extends MetricsConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(DropwizardMetricsConfiguration.class);
    private final AppProperties appProperties;

    @Autowired
    public DropwizardMetricsConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        if (appProperties.getMetrics().getLogs().isSummaryTracking()) {
            log.info("Initializing Metrics Log reporting");

            final Slf4jReporter reporter = Slf4jReporter.forRegistry(metricRegistry)
                    .outputTo(LoggerFactory.getLogger("summary-tracking"))
                    .convertRatesTo(TimeUnit.SECONDS)
                    .convertDurationsTo(TimeUnit.MILLISECONDS)
                    .withLoggingLevel(Slf4jReporter.LoggingLevel.INFO)
                    .build();
            reporter.start(appProperties.getMetrics().getLogs().getReportFrequency(), TimeUnit.SECONDS);
        }
    }

}
