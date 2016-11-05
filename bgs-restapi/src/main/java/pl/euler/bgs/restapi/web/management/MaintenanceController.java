package pl.euler.bgs.restapi.web.management;

import javaslang.control.Option;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.euler.bgs.restapi.core.management.LoggingResourcesService;
import pl.euler.bgs.restapi.core.management.MaintenanceHealthIndicator;
import pl.euler.bgs.restapi.core.management.MaintenanceService;
import pl.euler.bgs.restapi.core.management.MaintenanceTriggerMode;
import pl.euler.bgs.restapi.web.common.HttpCodeException;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/management")
public class MaintenanceController {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceController.class);

    private final MaintenanceService maintenanceService;
    private final MaintenanceHealthIndicator maintenanceHealthIndicator;
    private final LoggingResourcesService loggingResourcesService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, MaintenanceHealthIndicator maintenanceHealthIndicator,
            LoggingResourcesService loggingResourcesService) {
        this.maintenanceService = maintenanceService;
        this.maintenanceHealthIndicator = maintenanceHealthIndicator;
        this.loggingResourcesService = loggingResourcesService;
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

    @GetMapping(value = "/log/{fileName:.+}")
    @ResponseBody
    public HttpEntity<?> getFile(@PathVariable String fileName, HttpServletResponse response,
            @RequestParam(required = false, defaultValue = "DOWNLOAD") LogFileMode mode,
            @RequestParam(required = false, defaultValue = "200") Integer lines) {

        if (!FilenameUtils.isExtension(fileName, "log")) {
            log.info("Automatically adding extension to file: {}", fileName);
            fileName += ".log";
        }

        Option<File> fileOption = loggingResourcesService.getLogFile(fileName);
        if (fileOption.isEmpty()) {
            throw new HttpCodeException(HttpStatus.BAD_REQUEST, "There is no defined file with provided file log: " + fileName);
        }
        response.setContentType(ContentType.TEXT_PLAIN.getMimeType());
        File logFile = fileOption.get();

        if (mode.isDownloadMode()) {
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        } else {
            String tailContent = loggingResourcesService.getTailOfTheFile(logFile, lines);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "text/plain");
            return new HttpEntity<>(tailContent, httpHeaders);
        }
        return new HttpEntity<>(new FileSystemResource(logFile));
    }

    public enum LogFileMode {
        DOWNLOAD,
        DISPLAY;

        public boolean isDownloadMode() {
            return DOWNLOAD.equals(this);
        }
    }

}
