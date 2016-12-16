package pl.euler.bgs.restapi.config;

/**
 * List of profiles available on system.
 */
public interface Profiles {
    /** Production profile. Different configuration for log files */
    String PROD = "prod";
    /** Dev profile. All logs to the console */
    String DEV = "dev";
}
