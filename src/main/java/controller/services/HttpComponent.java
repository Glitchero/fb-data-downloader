package controller.services;

import java.util.Map;

public interface HttpComponent {

	String get(String uri, Map<String, String> headers);
	
}
