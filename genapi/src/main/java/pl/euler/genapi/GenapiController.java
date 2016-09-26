package pl.euler.genapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static pl.euler.genapi.JsonUtil.json;
import static spark.Spark.*;


public class GenapiController {
	private static final Logger log = LoggerFactory.getLogger(GenapiController.class);

	public GenapiController(final GenapiService service) {

		post("/v2/msisdns/subscriptions", (req, res) -> {
			String clientId = req.queryParams("client_id");
			String msisdn = req.queryParams("msisdn");
			return service.activateSubscription(msisdn, clientId);
		} , json());

		delete("/v2/msisdns/:msisdn/subscriptions/:client_id", (req, res) -> {
			String msisdn = req.params(":msisdn");
			String clientId = req.params(":client_id");
			return service.deactivateSubscription(msisdn, clientId);
		}, json());

		get("/v2/msisdns/:msisdn/subscriptions", (req, res) -> service.getSubscriptions(req.params(":msisdn")), json());

		get("/v2/msisdns/:msisdn/subscriptions/:client_id",
			(req, res) -> service.isSubscriptionExist(req.params(":msisdn"), req.params(":client_id")), json());

		get("/v2/subscriptions/:client_id", (req, res) -> service.getMsisdnsForSubscription(req.params(":client_id")), json());

		get("/v2/msisdns/subscriptions/:client_id/teryt/:teryt",
			(req, res) -> service.getMsisdnsForSubscriptionByArea(req.params(":client_id"), req.params(":teryt")), json());

		after((req, res) -> res.type("application/json"));

		exception(IllegalArgumentException.class, (e, req, res) -> {
			res.status(400);
			res.body(JsonUtil.toJson(new ResponseError(e)));
		});
	}
}
