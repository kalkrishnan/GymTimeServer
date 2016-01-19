package com.kkrishna.gymtime.common;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkrishna.gymtime.dao.Gym;
import com.kkrishna.gymtime.dao.Traffic;
import com.kkrishna.gymtime.util.GymTimeHttpClient;

@Component
public class GoogleApiGymStrategy implements GymStrategy {

	@Value("${gymtime.googleapiurl}")
	String apiUrl;

	@Value("${gymtime.googleapikey}")
	String apiKey;

	@Autowired
	private GymTimeHttpClient httpClient;

	public Gym searchGyms(String location) {
		try {
			String url = apiUrl + location + "+gym" + "&key=" + apiKey;
			return parseGyms(httpClient.getResponse(url));

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Gym parseGyms(String response) {
		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("results");

		for (JsonElement jsonElement : jarray) {

			jobject = jsonElement.getAsJsonObject();
			String address = jobject.get("formatted_address").toString().replace("\"", "");
			JsonObject locationObject = jobject.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
			String id = locationObject.get("lat").getAsString() + "|" + locationObject.get("lng").getAsString();
			String name = jobject.get("name").toString().replace("\"", "");
			return new Gym(id, name, address, null);

		}
		return null;
	}

}
