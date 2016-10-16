## Installation

```
bower install
gradlew clean build
cd build\libs
java -jar bgs-restapi-{VERSION}.jar
```

NOTE: The version above tag 0.1.0 needs database oracle connection to start up. If you need the version for mocking the genapi please
use the version from mentioned tag.

##### Setup genapi and bgs-restapi

1. Run genapi locally (see the README.md on that repo in order to know how to do that) - it runs on http://localhost:3000
2. Run bgs-genapi locally - you could override the properties according to the [Spring Boot convention](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

## REST API DOCUMENTATION

REST API documentation is available by Swagger under [/api-docs](http://localhost:8080/api-docs) endpoint.

##### Important endpoints:

```
/api/metric, /api/dictionary, /api/watchdog     -> metrics endpoints
/geo/*                                          -> gen api internal endpoints
```

##### HEALTH CHECK ENDPOINTS
```
/trace  -> tracing last 10 requests
/health -> status about application (up/down/maintenance, etc.)
/stats  -> jvm parameters (mem, threads, etc.)
/info   -> build version information
/maintenance -> POST, DELETE - enable disable maintenance mode
```

## APPLICATION CONFIGURATION

In order to set configuration parameters for specific instance please put `application.properties` file on the same 
directory where the JAR file has been located.

##### DB PARAMETERS

Currently there are some default parameters for Oracle connection within application.

```
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521
spring.datasource.username=bgs
spring.datasource.password=bgs
```

You can reconfigure them for your user.

##### LOGGING

```
logging.path=/x/y/z #define custom logging directory path
logging.config=/xyz/logback.xml # define own log configuration (logback)
```

In order to override file names use following system properties (according with logback docs):

```
MAIN_LOG_FILE -> file path & name for main log file
LOG_FILE_SUMMARY_TRACKING -> file path & name for services statistics
LOG_FILE_DETAILS_TRACKING -> file path & name for details tracking
```

##### ACTIVE PROFILE

Default active profile is set to dev. This profile add the logging to console. Suggested profile for production-ready system is `prod`.

```
spring.profiles.active=dev
```