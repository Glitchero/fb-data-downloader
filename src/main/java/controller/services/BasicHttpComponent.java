package controller.services;

import java.util.Map;

import controller.util.HttpClient;


public class BasicHttpComponent implements HttpComponent{

	public String get(String uri, Map<String, String> headers) {
		return HttpClient.get(uri, headers);
	}

}

