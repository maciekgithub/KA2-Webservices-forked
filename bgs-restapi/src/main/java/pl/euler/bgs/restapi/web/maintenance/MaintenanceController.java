package pl.euler.bgs.restapi.web.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
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

}
