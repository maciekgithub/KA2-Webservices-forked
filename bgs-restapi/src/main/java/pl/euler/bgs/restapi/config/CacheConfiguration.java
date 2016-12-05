package pl.euler.bgs.restapi.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String CACHE_AGENTS = "cache-agents";
    public static final String CACHE_AGENTS_ENDPOINTS = "cache-agents-endpoints";
    public static final String CACHE_REGISTERED_ENDPOINTS = "cache-registered-endpoints";

    private final AppProperties appProperties;

    @Autowired
    public CacheConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        cacheManager.setCacheBuilder(CacheBuilder
                                             .newBuilder()
                                             .expireAfterWrite(appProperties.getCache().getExpireAfter(), TimeUnit.SECONDS)
                                             .maximumSize(250));
        return cacheManager;
    }

}
