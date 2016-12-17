package pl.euler.bgs.restapi.core.management;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class BgsCacheManager extends SimpleCacheManager {

    public BgsCacheManager() {
        setCaches(Collections.emptySet());
    }

    @Override
    protected Cache getMissingCache(String name) {
        return new GuavaCache(name, CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build());
    }

}
