package pl.euler.bgs.restapi.core.management;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import javaslang.collection.List;
import javaslang.control.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LoggingResourcesService implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(LoggingResourcesService.class);
    private static final String MAIN_APPENDER_NAME = "BGS-FILE";

    /**
     * Return {@link #MAIN_APPENDER_NAME} Appender file if was defined for configuration.
     *
     * @return option of file
     */
    public Option<File> getMainBgsLogFile() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        return List.ofAll(context.getLoggerList())
                .map(logger -> (Iterable<Appender<ILoggingEvent>>) logger::iteratorForAppenders)
                .flatMap(List::ofAll)
                .filter(appender -> appender instanceof FileAppender && MAIN_APPENDER_NAME.equalsIgnoreCase(appender.getName()))
                .map(appender -> {
                    FileAppender<?> fileAppender = (FileAppender<?>) appender;
                    return new File(fileAppender.getFile());
                })
                .headOption();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        getMainBgsLogFile().forEach(file -> log.info("Detected file log. Path: {}", file.getAbsolutePath()));
    }
}
