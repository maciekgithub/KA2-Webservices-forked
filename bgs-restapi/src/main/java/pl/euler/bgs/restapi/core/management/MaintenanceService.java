package pl.euler.bgs.restapi.core.management;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import javaslang.control.Option;
import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class MaintenanceService {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceService.class);
    private static volatile boolean MAINTENANCE_MODE = false;

    private final HikariDataSource unwrappedDataSource;

    @Autowired
    public MaintenanceService(DataSource dataSource) {
        Preconditions.checkState(dataSource instanceof HikariDataSource, "We don't support maintenance mode for not hikari pool!");
        this.unwrappedDataSource = Try.of(() -> dataSource.unwrap(HikariDataSource.class)).get();
    }

    public void turnOnMaintenanceMode(MaintenanceTriggerMode mode) {
        log.info("Enabling maintenance mode with trigger: {}", mode);
        MAINTENANCE_MODE = true;
        if (MaintenanceTriggerMode.NORMAL.equals(mode)) {
            this.unwrappedDataSource.suspendPool(); // for immediate mode there was a problem with if with the aborting of ongoing requests
        }
        // the pool can be not initialized (no requests before) and then there is no field pool
        Option<HikariPool> poolOption = Option.of((HikariPool) new DirectFieldAccessor(unwrappedDataSource).getPropertyValue("pool"));
        poolOption.forEach(HikariPool::softEvictConnections);
    }

    public void turnOffMaintenanceMode() {
        MAINTENANCE_MODE = false;
        this.unwrappedDataSource.resumePool();
    }

    public boolean isMaintenanceModeEnabled() {
        return MAINTENANCE_MODE;
    }

}