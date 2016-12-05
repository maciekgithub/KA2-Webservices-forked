package pl.euler.bgs.restapi

import com.mashape.unirest.http.Unirest
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
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

    @LocalServerPort
    var serverPort: Int = 0

    var serverPath:String = ""

    @Before
    fun setUp() {
        serverPath = "http://localhost:$serverPort/api"
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
    fun shouldCreateNewSubscription() {

//        val get = Unirest.get("$serverPath/dictionaries").basicAuth("GEN", "wbs_agent").asJson().body
        val get = Unirest.get("$serverPath/dictionaries").header("Authorization", "Basic d2JzX2FnZW50").asJson().body

//		JSONObject object = Unirest.get("http://localhost:8052/api/dictionaries").asObject(JSONObject.class).getBody();
//		System.out.println(object);
        println(get)
    }

}

