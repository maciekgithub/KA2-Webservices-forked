## Installation

```
bower install
gradlew clean build
cd build\libs
java -jar bgs-restapi-0.0.1-SNAPSHOT.jar
```

## Setup genapi and bgs-restapi

1. Run genapi locally (see the README.md on that repo in order to know how to do that) - it runs on http://localhost:3000
2. Run bgs-genapi locally - you could override the properties according to the [Spring Boot convention](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

## REST API DOCUMENTATION

REST API documentation is available by Swagger under [/api-docs](http://localhost:8080/api-docs) endpoint.