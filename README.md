## BUILD AND DEPLOY TO GLASSFISH

```
mvn clean install package
cp target/bgsapi.war /you/glassfish/deployment/path
```

## REST API DOCUMENTATION

See the [wiki docs](https://redmine.euler.pl/projects/dokumentacja/wiki/WS_API).

##### MANAGEMENT ENDPOINTS

```
POST    /management/maintenance                     -> enable maintenance mode, authorization required
POST    /management/maintenance?mode=IMMEDIATE      -> enable maintenance mode, sessions killed by server, authorization required
DELETE  /management/maintenance                     -> disable maintenance mode, authorization required
GET     /management/maintenance                     -> get status about maintenance mode, authorization not required
GET     /management/health                          -> status about application
DELETE  /management/cache                           -> clear all caches, authorization required
```

## APPLICATION CONFIGURATION

The application requires defined JDBC pool on Glassfish server and uses the `jdbc/bgs` JNDI name to obtain data source.

The JNDI name uses by application could by overriden by system property `bgs.datasource.jndi`.

The Oracle JDBC driver is not shipped within the application and should by provided by Glassfish server.

##### ORACLE FAILOVER SUPPORT

In order to enable failover feature on Oracle you have to specify the connection by the following way:

```
spring.datasource.url= jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=OFF)(FAILOVER=ON)(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521))(CONNECT_DATA=(SERVICE_NAME=xe)))
```

##### LOGGING

Application uses the SLF4J to JUL logging (default for Glassfish). In this case the entire logging configuration could be made on Glasfish server.

Config file: domain/config/logging.properties
```
org.springframework=INFO
pl.euler=INFO
```
##### SSL SUPPORT

Should be configured per Glassfish server.

##### Create connection pools

```
asadmin> create-jdbc-connection-pool --restype javax.sql.DataSource --datasourceclassname oracle.jdbc.pool.OracleDataSource --property "user=bgs:password=bgs: url=jdbc\\:oracle\\:thin\\:@localhost\\:1521\\:XE" Oracle
asadmin> create-jdbc-resource --connectionpoolid OraclePool --target pmdcluster jdbc/bgs
```

##### Example Apache Benchmark request

```
ab -c50 -n100 -A wsbgs:wsbgs -H "Date:123" -H "User-Agent:wsbgs" http://localhost:8080/bgs/api/dictionaries
```