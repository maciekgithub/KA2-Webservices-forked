package pl.euler.bgs.restapi.web.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceHealthIndicator implements HealthIndicator {

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceHealthIndicator(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @Override
    public Health health() {
        boolean maintenanceModeEnabled = maintenanceService.isMaintenanceModeEnabled();
        return maintenanceModeEnabled ? Health.status("ON").build() : Health.status("OFF").build();
    }
}
