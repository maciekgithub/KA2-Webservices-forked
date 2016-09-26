package pl.euler.genapi;

import com.google.gson.Gson;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import spark.Spark;
import spark.utils.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GenApiControllerIntegrationTest {

	@BeforeClass
	public static void beforeClass() {
		App.main(null);
	}

	@AfterClass
	public static void afterClass() {
		Spark.stop();
	}

	@Ignore
	@Test
	public void shouldCreateNewSubscription() {
		TestResponse res = request("POST", "/v2/msisdns/subscriptions?msisdn=m1&client_id=c1");
		Map<String, String> json = res.json();
		assertEquals(200, res.status);
		assertEquals(true, json.get("created_new_subscription"));
	}

	private TestResponse request(String method, String path) {
		try {
			URL url = new URL("http://localhost:3000" + path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(method);
			connection.setDoOutput(true);
			connection.connect();
			String body = IOUtils.toString(connection.getInputStream());
			return new TestResponse(connection.getResponseCode(), body);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Sending request failed: " + e.getMessage());
			return null;
		}
	}

	private static class TestResponse {
		public final String body;
		public final int status;

		public TestResponse(int status, String body) {
			this.status = status;
			this.body = body;
		}

		public Map<String,String> json() {
			return new Gson().fromJson(body, HashMap.class);
		}
	}
}
