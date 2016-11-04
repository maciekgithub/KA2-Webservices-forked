package pl.euler.bgs.restapi;

import javaslang.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.euler.bgs.restapi.core.management.LoggingResourcesService;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(locations="classpath:log-test.properties")
public class LogFilesServiceTest {

    @Autowired
    LoggingResourcesService filesService;

    @Test
    public void shouldGetInfoAboutMainLogFileAppender() throws Exception {
        // when
        Option<File> mainBgsLogFile = filesService.getMainBgsLogFile();

        // then
        assertThat(mainBgsLogFile.isDefined());
    }

}
