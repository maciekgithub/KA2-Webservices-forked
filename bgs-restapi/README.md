## INSTALLATION

```
bower install
gradlew clean build
cd build\libs
java -jar bgs-restapi-{VERSION}.jar
```

##### Integration testing

In order to run integration test use: `gradlew integrationTest`

##### Setup genapi and bgs-restapi

1. Run genapi locally (see the README.md on that repo in order to know how to do that) - it runs on http://localhost:3000
2. Run bgs-genapi locally - you could override the properties according to the [Spring Boot convention](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

## REST API DOCUMENTATION

See the [wiki docs](https://redmine.euler.pl/projects/dokumentacja/wiki/WS_API).

##### MANAGEMENT ENDPOINTS

```
POST    /management/maintenance                     -> enable maintenance mode, authorization required
POST    /management/maintenance?mode=IMMEDIATE      -> enable maintenance mode, sessions killed by server, authorization required
DELETE  /management/maintenance                     -> disable maintenance mode, authorization required
GET     /management/maintenance                     -> get status about maintenance mode, authorization not required
GET     /management/health                          -> status about application (up/down/maintenance, etc.), authorization required for details
GET     /management/info                            -> build version information, authorization is not required
GET     /management/trace                           -> tracing last 10 requests, authorization required
GET     /management/metrics                         -> jvm parameters (mem, threads, etc.), authorization required
DELETE  /management/cache                           -> clear all caches, authorization required
GET     /management/log/{fileName}                  -> downlaod log file from last 24H, ie. GET /management/log/bgs.log or /management/log/bgs (without suffix .log will work to)
GET     /management/log/bgs?mode=display&lines=150  -> display in browser last 150 lines of log file (for example from bgs.log file), default values for parameters: lines=200, mode=download
```

## APPLICATION CONFIGURATION & DEPLOYMENT

In order to set configuration parameters for specific instance please put `application.properties` file on the same 
directory where the JAR file has been located.

##### ACTIVE PROFILE

Default active profile is set to dev. This profile has set logging to console and has disabled some features like Swagger docs
and details tracking.

```
spring.profiles.active=dev
```

Suggested profile for production-ready system (with enabled all features):
 
```
spring.profiles.active=prod
```

##### DB PARAMETERS

Currently there are some default parameters for Oracle connection within application.

```
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521
spring.datasource.username=bgs
spring.datasource.password=bgs
```

You can reconfigure them for your user and environment.

##### ORACLE FAILOVER SUPPORT

In order to enable failover feature on Oracle you have to specify the connection by the following way:

```
spring.datasource.url= jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=OFF)(FAILOVER=ON)(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=xe)))
```

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

There are some additional properties to enable/disable logging features:

```
app.metrics.logs.details-tracking=true      -> should we log all input and output parameters for methods marked by @Tracked withing application
app.metrics.logs.summary-tracking=true      -> enable/disable performance statistics for methods marked by @Timed withing application 
app.metrics.logs.report-frequency=60        -> interval for summary tracking statistics in seconds
```

##### SSL SUPPORT

The application provides the SSL support. In order to enable SSL the additional profile `ssl` should be enabled. With this profile application starts
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

##### BUILD WAR & DEPLOY TO GLASSFISH

In order to build war file (tested on Glassfish 4.1.1) instead of executable JAR execute the build with the following command:

```
gradlew clean build -Pwar
```

In order to override the `application.properties` for configuration on server the easiest way to do it is a specify a JVM property which
will point to the location with `application.properties`. The name of the property should be `spring.config.location` for example:
`spring.config.location=d:/conf/`.

On Glassfish you can configure JVM properties on the Configurations -> server-config -> JMV Settings -> JVM Options and you can add:

```
-Dspring.config.location=d:/tmp/
-Dspring.profiles.active=prod
```

The active profiles could be also defined of course on prepared specific `application.properties` file under provided config location.

[More about externalized configuration, section 24.3](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)