package controller.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class HttpClient {

	
private static final String DATA_ENCODING = "UTF-8";

	
	private static CloseableHttpClient buildClient() {
	      //HttpClientBuilder builder = HttpClientBuilder.create();

	      int timeout = 60;
	      RequestConfig config = RequestConfig.custom()
	              .setSocketTimeout(timeout * 1000)
	              .setConnectionRequestTimeout(timeout * 1000)
	              .setConnectTimeout(timeout * 1000).build();


	      return HttpClients.custom().setDefaultRequestConfig(config).build(); //builder.build();

	   }
	
	
	   public static String get(final String url) {
		      String json = "";
		      try {
		         CloseableHttpClient httpClient = buildClient();
		         HttpGet request = new HttpGet(url);
		         request.addHeader("content-type", "application/json");
		         CloseableHttpResponse response = httpClient.execute(request);
		         if (response.getEntity() != null) {
		            json = EntityUtils.toString(response.getEntity(), DATA_ENCODING);
		         }
		         //logger.info("spark[tryReadAlgorithmModuleStatus]: "+json);
		      }
		      catch (Exception ex2) {
		         json = ex2.getMessage();
		      }

		      return json;
		   }
	   
	
	   
	   public static String get(final String url, final Map<String, String> headers) {
		      String json = "";
		      try {
		         CloseableHttpClient httpClient = buildClient();
		         HttpGet request = new HttpGet(url);
		         for (Map.Entry<String, String> entry : headers.entrySet()) {
		            request.addHeader(entry.getKey(), entry.getValue());
		         }
		         CloseableHttpResponse response = httpClient.execute(request);
		         if (response.getEntity() != null) {
		            json = EntityUtils.toString(response.getEntity(), DATA_ENCODING);
		         }
		         //logger.info("spark[tryReadAlgorithmModuleStatus]: "+json);
		      }
		      catch (Exception ex2) {
		         json = ex2.getMessage();
		      }

		      return json;
		   }
	   
	   
	   
}
