package pl.euler.bgs.restapi.web.maintenance;

import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import javaslang.control.Option;
import javaslang.control.Try;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class MaintenanceService {
    // todo it's just temporary solution (single node only)
    private static volatile boolean MAINTENANCE_MODE = false;

    private final HikariDataSource unwrappedDataSource;

    @Autowired
    public MaintenanceService(DataSource dataSource) {
        Preconditions.checkState(dataSource instanceof HikariDataSource, "We don't support maintenance mode for not hikari pool!");
        this.unwrappedDataSource = Try.of(() -> dataSource.unwrap(HikariDataSource.class)).get();
    }

    void turnOnMaintenanceMode() {
        this.unwrappedDataSource.suspendPool();
        // the pool can be not initialized (no requests before) and then there is no field pool
        Option.of((HikariPool) new DirectFieldAccessor(unwrappedDataSource).getPropertyValue("pool")).forEach(HikariPool::softEvictConnections);
        MAINTENANCE_MODE = true;
    }

    void turnOffMaintenanceMode() {
        MAINTENANCE_MODE = false;
        this.unwrappedDataSource.resumePool();
    }

    public boolean isMaintenanceModeEnabled() {
        return MAINTENANCE_MODE;
    }

}
