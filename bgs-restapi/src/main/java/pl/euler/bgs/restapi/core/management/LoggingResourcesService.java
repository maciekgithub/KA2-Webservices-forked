package pl.euler.bgs.restapi.core.management;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import com.google.common.base.Charsets;
import javaslang.collection.List;
import javaslang.control.Option;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LoggingResourcesService {
    private static final Logger log = LoggerFactory.getLogger(LoggingResourcesService.class);

    /**
     * Return file if was defined on logs configuration. Otherwise return empty Option.
     *
     * @return option of file
     */
    public Option<File> getLogFile(final String fileName) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        return List.ofAll(context.getLoggerList())
                .map(logger -> (Iterable<Appender<ILoggingEvent>>) logger::iteratorForAppenders)
                .flatMap(List::ofAll)
                .filter(appender -> appender instanceof FileAppender)
                .map(appender -> (FileAppender<?>) appender)
                .filter(appender -> FilenameUtils.getName(appender.getFile()).equalsIgnoreCase(fileName))
                .map(appender -> new File(appender.getFile()))
                .headOption();
    }

    public String getTailOfTheFile(@NotNull final File file, final int lines) {
        try {
            ReversedLinesFileReader fileReader = new ReversedLinesFileReader(file, Charsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            int counter = 0;
            String newLine;
            while ((newLine = fileReader.readLine()) != null && counter < lines) {
                sb.insert(0, newLine + System.getProperty("line.separator"));
                counter++;
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot access to log file in order to display file.");
        }
    }

}
