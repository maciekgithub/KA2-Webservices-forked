package pl.euler.genapi;

import spark.Spark;

public class App {
	public static void main(String[] args) {
		Spark.port(3000);
		new GenapiController(new GenapiService());
	}
}
