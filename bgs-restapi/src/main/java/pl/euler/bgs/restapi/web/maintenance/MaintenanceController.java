package pl.euler.bgs.restapi.web.maintenance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.core.management.MaintenanceHealthIndicator;
import pl.euler.bgs.restapi.core.management.MaintenanceTriggerMode;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/management")
public class MaintenanceController {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceController.class);

    private final MaintenanceService maintenanceService;
    private final MaintenanceHealthIndicator maintenanceHealthIndicator;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, MaintenanceHealthIndicator maintenanceHealthIndicator) {
        this.maintenanceService = maintenanceService;
        this.maintenanceHealthIndicator = maintenanceHealthIndicator;
    }

    @PostMapping("/maintenance")
    @ResponseStatus(HttpStatus.CREATED)
    public void turnOn(@RequestParam(required = false, defaultValue = "NORMAL") MaintenanceTriggerMode mode) {
        log.info("Enabling maintenance mode: {}", mode);
        this.maintenanceService.turnOnMaintenanceMode(mode);
    }

    @DeleteMapping("/maintenance")
    public void turnOff() {
        this.maintenanceService.turnOffMaintenanceMode();
    }

    @GetMapping("/maintenance")
    public Health getMaintenanceHealth() {
        return maintenanceHealthIndicator.health();
    }

}
