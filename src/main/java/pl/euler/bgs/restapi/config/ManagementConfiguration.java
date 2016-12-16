package pl.euler.bgs.restapi.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.CompositeHealthIndicatorConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProviders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.euler.bgs.restapi.core.management.DataSourceHealthIndicator;
import pl.euler.bgs.restapi.core.management.MaintenanceService;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

@Configuration
public class ManagementConfiguration extends CompositeHealthIndicatorConfiguration<DataSourceHealthIndicator, DataSource>
        implements InitializingBean {

    private final Map<String, DataSource> dataSources;

    private final Collection<DataSourcePoolMetadataProvider> metadataProviders;

    private DataSourcePoolMetadataProvider poolMetadataProvider;

    private final MaintenanceService maintenanceService;

    @Autowired
    public ManagementConfiguration(
            ObjectProvider<Map<String, DataSource>> dataSourcesProvider,
            ObjectProvider<Collection<DataSourcePoolMetadataProvider>> metadataProvidersProvider, MaintenanceService maintenanceService) {
        this.dataSources = dataSourcesProvider.getIfAvailable();
        this.metadataProviders = metadataProvidersProvider.getIfAvailable();
        this.maintenanceService = maintenanceService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.poolMetadataProvider = new DataSourcePoolMetadataProviders(this.metadataProviders);
    }

    @Bean
    public HealthIndicator dbHealthIndicator() {
        return createHealthIndicator(this.dataSources);
    }

    @Override
    protected DataSourceHealthIndicator createHealthIndicator(DataSource source) {
        return new DataSourceHealthIndicator(source, getValidationQuery(source), maintenanceService);
    }

    private String getValidationQuery(DataSource source) {
        DataSourcePoolMetadata poolMetadata = this.poolMetadataProvider.getDataSourcePoolMetadata(source);
        return (poolMetadata == null ? null : poolMetadata.getValidationQuery());
    }
}
