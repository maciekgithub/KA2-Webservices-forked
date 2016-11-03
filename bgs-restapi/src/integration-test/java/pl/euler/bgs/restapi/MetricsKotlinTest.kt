package pl.euler.bgs.restapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.env.Environment
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import pl.euler.bgs.restapi.config.AppProperties
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner::class)
@TestPropertySource(locations = arrayOf("classpath:test.properties") )
//@ActiveProfiles("test")
class MetricsKotlinTest {

    @Autowired
    internal var properties: AppProperties? = null

    @Autowired
    internal var objectMapper: ObjectMapper? = null

    @Autowired
    private lateinit var env : Environment

    @Autowired
    internal var restTemplate: TestRestTemplate? = null

    @LocalServerPort
    var serverPort: Int = 0


    @Test
    fun shouldCreateNewSubscription() {
        FuelManager.instance.baseHeaders = mapOf("User-Agent" to "kapitan", "Date" to "2012", "Accept" to "*/*")

        println(Arrays.toString(env.activeProfiles))
        val url = "http://localhost:$serverPort/api/dictionaries"
//        url.header
        val (request, response1, result) = url.httpGet(listOf("User-Agent" to "kapitan", "Date" to "2012")).responseString()
        println(result)


//        url.httpGet().responseString { request, response, result ->
//            println(response.httpResponseMessage)
//        }
        println(serverPort)
        //		String response = restTemplate.getForObject("/management/health", String.class);
        val response = restTemplate!!.getForObject("/api/dictionaries", String::class.java)
        println(response)
    }

}

