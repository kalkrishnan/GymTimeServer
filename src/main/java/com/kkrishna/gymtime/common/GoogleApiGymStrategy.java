package com.kkrishna.gymtime.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkrishna.gymtime.dao.GeneralTraffic;
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

	public List<Gym> searchGyms(String location) {
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

	private List<Gym> parseGyms(String response) {
		System.out.println(response);
		JsonElement jelement = new JsonParser().parse(response);
		JsonObject jobject = jelement.getAsJsonObject();
		JsonArray jarray = jobject.getAsJsonArray("results");
		List<Gym> gyms = new ArrayList<Gym>();
		for (JsonElement jsonElement : jarray) {

			jobject = jsonElement.getAsJsonObject();
			gyms.add(parseGym(jobject));

		}
		return gyms;
	}

	private Gym parseGym(JsonObject gymJson) {
		String address = gymJson.get("formatted_address").toString().replace("\"", "").replace(", United States", "");
		JsonObject locationObject = gymJson.get("geometry").getAsJsonObject().get("location").getAsJsonObject();
		String id = locationObject.get("lat").getAsString() + "_" + locationObject.get("lng").getAsString();
		String name = gymJson.get("name").toString().replace("\"", "");
		return new Gym(id, name, address, new ArrayList<Traffic>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -1056301818528097868L;

			{
				add(new GeneralTraffic(0.6));
			}
		});
	}

}
