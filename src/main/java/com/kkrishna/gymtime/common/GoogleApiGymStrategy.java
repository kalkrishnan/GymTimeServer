package com.kkrishna.gymtime.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kkrishna.gymtime.dao.Gym;
import com.kkrishna.gymtime.util.GymTimeHttpClient;

@Component
public class GoogleApiGymStrategy implements GymStrategy {

	@Value("${gymtime.googleapiurl}")
	String apiLocationUrl;

	@Value("${gymtime.googleapilatlongurl}")
	String apiLatLongUrl;

	@Value("${gymtime.googleapikey}")
	String apiKey;

	@Autowired
	private GymTimeHttpClient httpClient;

	public List<Gym> searchGyms(String location, int radius, String keyword, String type) {
		try {
			String url = apiLocationUrl + keyword + "&type="+type + "&radius=" + radius + "&location=" + location +"&key=" + apiKey;
			System.out.println(url);

			return parseGyms(httpClient.getResponse(url));

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Gym> searchGyms(String latLong, String radius) {
		try {
			String url = apiLatLongUrl + latLong.replace("_", ",") + "&radius=" + radius + "&type=establishment" + "&key="
					+ apiKey;
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
		String latlong = String.format("%.2f", locationObject.get("lat").getAsFloat()) + "_"
				+ String.format("%.2f", locationObject.get("lng").getAsFloat());
		String name = gymJson.get("name").toString().replace("\"", "");
		return Gym.builder().latLong(latlong).name(name).address(address).traffic(sampleTraffic()).build();
	}

	private Map<Integer, Integer> sampleTraffic() {
		return ImmutableMap.<Integer, Integer> builder().put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6)
				.build();
	}

}
