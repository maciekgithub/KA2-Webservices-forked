package pl.euler.bgs.restapi.web.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.core.management.MaintenanceHealthIndicator;

@RestController
@RequestMapping("/management")
public class MaintenanceController {
    private final MaintenanceService maintenanceService;
    private final MaintenanceHealthIndicator maintenanceHealthIndicator;
    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, MaintenanceHealthIndicator maintenanceHealthIndicator) {
        this.maintenanceService = maintenanceService;
        this.maintenanceHealthIndicator = maintenanceHealthIndicator;
    }

    @PostMapping("/maintenance")
    @ResponseStatus(HttpStatus.CREATED)
    public void turnOn() {
        this.maintenanceService.turnOnMaintenanceMode();
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
