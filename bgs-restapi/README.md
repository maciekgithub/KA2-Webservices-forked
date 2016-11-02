## Installation

```
bower install
gradlew clean build
cd build\libs
java -jar bgs-restapi-{VERSION}.jar
```

NOTE: The version above tag 0.1.0 needs database oracle connection to start up. If you need the version for mocking the genapi please
use the version from mentioned tag.

##### Integration testing

In order to run integration test use: `gradlew integrationTest`

##### Setup genapi and bgs-restapi

1. Run genapi locally (see the README.md on that repo in order to know how to do that) - it runs on http://localhost:3000
2. Run bgs-genapi locally - you could override the properties according to the [Spring Boot convention](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

## REST API DOCUMENTATION

REST API documentation is available by Swagger under [/api-docs](http://localhost:8080/api-docs) endpoint.

See also the [wiki docs](https://redmine.euler.pl/projects/dokumentacja/wiki/WS_API).

##### MANAGEMENT ENDPOINTS
```
POST    /management/maintenance         -> enable maintenance mode, authorization required
POST    /management/maintenance?mode=IMMEDIATE         -> enable maintenance mode, sessions killed by server, authorization required
DELETE  /management/maintenance         -> disable maintenance mode, authorization required
GET     /management/maintenance         -> get status about maintenance mode, authorization not required
GET     /management/health              -> status about application (up/down/maintenance, etc.), authorization required for details
GET     /management/info                -> build version information, authorization is not required
GET     /management/trace               -> tracing last 10 requests, authorization required
GET     /management/metrics             -> jvm parameters (mem, threads, etc.), authorization required
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

##### BUILD WAR

In order to build war file (tested on Glassfish 4.1.1) instead of executable JAR execute the build with the following command:

```
gradlew clean build -Pwar
```

In order to override the `application.properties` for configuration on server the easiest way to do it is a specify a environment property which
will point to the location with `application.properties`. The name of the property should be `SPRING_CONFIG_LOCATION` for example:
`SPRING_CONFIG_LOCATION=file:/etc/conf`.

[More about externalized configuration, section 24.3](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)