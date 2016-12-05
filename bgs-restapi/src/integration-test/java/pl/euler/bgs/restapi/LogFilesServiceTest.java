package pl.euler.bgs.restapi;

import javaslang.control.Option;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.core.management.LoggingResourcesService;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations="classpath:logging-test.properties")
public class LogFilesServiceTest {

    @Autowired
    LoggingResourcesService filesService;

    @Test
    public void shouldGetInfoAboutMainLogFileAppender() throws Exception {
        // when
        Option<File> mainBgsLogFile = filesService.getLogFile("bgs.log");

        // then
        assertThat(mainBgsLogFile.isDefined());
    }

    @Test
    public void shouldGetTailOfTheLogFile() throws Exception {
        // setup
        Option<File> mainBgsLogFile = filesService.getLogFile("bgs.log");
        assertThat(mainBgsLogFile.isDefined());
        File logFile = mainBgsLogFile.get();

        // when
        String tailOfTheFile = filesService.getTailOfTheFile(logFile, 10);

        // then
        assertThat(tailOfTheFile).isNotEmpty();
    }

}
