package pl.euler.bgs.restapi.core.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static javaslang.collection.List.ofAll;

@Service
public class MaintenanceService {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceService.class);
    private static volatile boolean MAINTENANCE_MODE = false;

    private final CacheManager cacheManager;

    @Autowired
    public MaintenanceService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void turnOnMaintenanceMode(MaintenanceTriggerMode mode) {
        log.info("Enabling maintenance mode with trigger: {}", mode);
        MAINTENANCE_MODE = true;
    }

    /** Method clear all caches within application. */
    public void clearAllCaches() {
        log.info("Clearing all caches.");
        ofAll(cacheManager.getCacheNames())
                .map(cacheManager::getCache)
                .forEach(Cache::clear);
    }

    public void turnOffMaintenanceMode() {
        MAINTENANCE_MODE = false;
    }

    public static boolean isMaintenanceModeEnabled() {
        return MAINTENANCE_MODE;
    }

}
