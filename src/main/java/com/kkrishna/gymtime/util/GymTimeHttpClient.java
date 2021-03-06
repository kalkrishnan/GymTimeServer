package com.kkrishna.gymtime.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

@Component
public class GymTimeHttpClient {
	private final HttpClient httpclient;

	@SuppressWarnings("deprecation")
	public GymTimeHttpClient() {
		httpclient = HttpClientBuilder.create().setConnectionTimeToLive(5, TimeUnit.SECONDS)
				.setSSLHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();
	}

	public String getResponse(String URL) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(URL.replace(" ", "%20"));
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};
		return httpclient.execute(httpget, responseHandler);
	}
}
