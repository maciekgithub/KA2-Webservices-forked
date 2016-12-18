package pl.euler.bgs.restapi.web.management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.core.management.MaintenanceService;
import pl.euler.bgs.restapi.core.management.MaintenanceTriggerMode;

@Controller
@SuppressWarnings("unused")
@RequestMapping("/management")
public class MaintenanceController {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceController.class);

    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/maintenance", method = RequestMethod.POST)
    public void turnOn(@RequestParam(required = false, defaultValue = "NORMAL") MaintenanceTriggerMode mode) {
        log.info("Enabling maintenance mode: {}", mode);
        this.maintenanceService.turnOnMaintenanceMode(mode);
    }

    @ResponseBody
    @RequestMapping(value = "/maintenance", produces = "application/json")
    public Status getMaintenanceHealth() {
        boolean maintenanceModeEnabled = maintenanceService.isMaintenanceModeEnabled();
        return maintenanceModeEnabled ? new Status("ON") : new Status("OFF");
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/maintenance", method = RequestMethod.DELETE)
    public void turnOff() {
        this.maintenanceService.turnOffMaintenanceMode();
    }

    @ResponseBody
    @RequestMapping(value = "/health")
    public Status health() {
        return new Status("OK");
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/cache", method = RequestMethod.DELETE)
    public void clearCaches() {
        maintenanceService.clearAllCaches();
    }

    public enum LogFileMode {
        DOWNLOAD,
        DISPLAY;

        public boolean isDownloadMode() {
            return DOWNLOAD.equals(this);
        }
    }

    static class Status {
        private final String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
