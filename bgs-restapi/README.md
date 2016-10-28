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
/proxy/gen/*                                    -> gen api internal endpoints
```

##### MANAGEMENT ENDPOINTS
```
POST DELETE /maintenance        -> maintenance mode (enable, disable), authorization required
GET /info                       -> build version information, authorization is not required
GET /trace                      -> tracing last 10 requests, authorization required
GET /health                     -> status about application (up/down/maintenance, etc.), authorization required for details
GET /metrics                    -> jvm parameters (mem, threads, etc.), authorization required
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

##### SSL SUPPORT

The application provides the SSL support. In order to enable SSL the profile `ssl` should be enabled. With this profile application starts
working on standard HTTPS port **443**. Currently mutual SSL authentication is not enabled.

The server certificate is signed by self generated and self signed CA so if you want avoid the untrusted certificate problem please import our CA authority
to trusted authorities on you machine. With this step you wan't see the issue that the server is untrusted. How to import CA on Chrome:

- Go to Chrome Settings.
- Click on "advanced settings"
- Under HTTPS/SSL click to "Manage Certificates"
- Go to "Trusted Root Certificate Authorities"
- Click to "Import"
- There will be a pop up window that will ask you if you want to install this certificate.

You will find server certificates files (private, csr, public) on `/etc/certificates` directory.

##### ACTIVE PROFILE

Default active profile is set to dev. This profile has set logging to console.

```
spring.profiles.active=dev
```

Suggested profile for production-ready system:
 
```
spring.profiles.active=prod,ssl
```
