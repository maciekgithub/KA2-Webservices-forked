package pl.euler.bgs.restapi.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.euler.bgs.restapi.core.management.BgsCacheManager;

@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String CACHE_AGENTS = "cache-agents";
    public static final String CACHE_AGENTS_ENDPOINTS = "cache-agents-endpoints";
    public static final String CACHE_REGISTERED_ENDPOINTS = "cache-registered-endpoints";

    @Bean
    public CacheManager cacheManager() {
        return new BgsCacheManager();
    }

}
