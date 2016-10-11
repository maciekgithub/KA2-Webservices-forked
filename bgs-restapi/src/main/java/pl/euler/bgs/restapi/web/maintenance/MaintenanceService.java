package pl.euler.bgs.restapi.web.maintenance;

import org.springframework.stereotype.Service;

@Service
public class MaintenanceService {
    // todo it's just temporary solution (single node only)
    private static volatile boolean MAINTENANCE_MODE = false;

    void turnOnMaintenanceMode() {
        MAINTENANCE_MODE = true;
    }

    void turnOffMaintenanceMode() {
        MAINTENANCE_MODE = false;
    }

    boolean isMaintenanceModeEnabled() {
        return MAINTENANCE_MODE;
    }

}
