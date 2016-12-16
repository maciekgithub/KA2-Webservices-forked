package pl.euler.bgs.restapi

import com.mashape.unirest.http.Unirest
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import pl.euler.bgs.restapi.config.AppProperties

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = arrayOf("classpath:integration-test.properties") )
class AuthorizationTest {

    @Autowired
    lateinit var properties: AppProperties

    @Autowired
    lateinit var env : Environment

    @Autowired
    lateinit var securityProperties: SecurityProperties

    @LocalServerPort
    var serverPort: Int = 0

    var serverPath: String = ""
    var serverApiPath: String = ""

    @Before
    fun setUp() {
        serverPath = "http://localhost:$serverPort"
        serverApiPath = "http://localhost:$serverPort/api"
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun setup() {
            Unirest.setDefaultHeader("Accept", "*/*")
            Unirest.setDefaultHeader("Date", "2016")
            Unirest.setDefaultHeader("User-Agent", "GEN")
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            Unirest.shutdown()
        }
    }

    @Test
    fun shouldReturnFullInfoForAuthorizedUser() {
        // when
        val get = Unirest.get("$serverPath/management/health").basicAuth(securityProperties.user.name, securityProperties.user.password).asJson().body

        // then
        val dbStatus = get.`object`.getJSONObject("db").getString("status")
        assertThat(dbStatus).isEqualTo("UP")
    }

    @Test
    fun shouldReturnExceptionForUserWithoutPermissions() {
        // when
        val response = Unirest.post("$serverPath/management/maintenance").basicAuth("no", "exist").asJson()

        // then
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun shouldReturnBasicInfoForUnauthorizedUser() {
        val response = Unirest.get("$serverPath/management/maintenance").asJson().body

        // then
        assertThat(response.`object`.getString("status")).isEqualTo("OFF")
    }

    @Test
    fun shouldCorrectlyServiceMaintenanceUserRequestToGenericApi() {
        // when
        val response = Unirest.get("$serverApiPath/dictionaries").basicAuth(securityProperties.user.name, securityProperties.user.password).asJson().body

        // then
        assertThat(response.`object`.getString("response")).isEqualTo("OK")
    }

    @Test
    fun shouldReturnUnauthorizedForNonExistedAgent() {

        // when
        val response = Unirest.get("$serverApiPath/dictionaries").basicAuth("no", "exist").asJson()

        // then
        assertThat(response.status).isEqualTo(HttpStatus.UNAUTHORIZED.value())
    }


}

