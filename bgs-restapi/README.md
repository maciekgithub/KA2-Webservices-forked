## Installation

```
bower install
gradlew clean build
cd build\libs
java -jar bgs-restapi-{VERSION}-SNAPSHOT.jar
```

NOTE: The version above tag 0.1.0 needs database oracle connection to start up. If you need the version for mocking the genapi please
use the version from mentioned tag.

## Setup genapi and bgs-restapi

1. Run genapi locally (see the README.md on that repo in order to know how to do that) - it runs on http://localhost:3000
2. Run bgs-genapi locally - you could override the properties according to the [Spring Boot convention](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

## DB PARAMETERS

Currently there are some default parameters for Oracle connection on `application.properties` within application.

```
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521
spring.datasource.username=bgs
spring.datasource.password=bgs
```

In order to override these parameters on runtime please put `application.properties` file with your database user on the same 
directory where the JAR file has been located.

## REST API DOCUMENTATION

REST API documentation is available by Swagger under [/api-docs](http://localhost:8080/api-docs) endpoint.