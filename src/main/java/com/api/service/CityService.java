package com.api.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.api.dao.CityDAO;
import com.api.model.Weather;

import weatherproject.AccuweatherInfo;

@Service
public class CityService {


	public boolean isCityAvailable(String city) {
		/*CityDAO dao = new CityDAO();                   These to line to verify from DB whether City is available or not
		return dao.isCityAvailable(city);*/
		return getCity(city.toLowerCase()) == null;   // This line to verify from JSON file whether City is available or not
														//Use any of any from these two verification
	}

	
	//Get city code from accuweather api
	public String getCityCode(String city) {
		final String apiKey = AccuweatherInfo.apiKey;
		String cityUrl = "http://dataservice.accuweather.com/"
				+ "locations/v1/cities/search?apikey=" + apiKey + "&q="
				+ city;

		try {
			URL url = new URL(cityUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			if (connection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

			String output;
			StringBuffer response = new StringBuffer();
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				response.append(output);
			}
			
			org.json.JSONArray jsonArray = new org.json.JSONArray(response.toString());
			org.json.JSONObject data = (org.json.JSONObject) jsonArray.get(0);
			connection.disconnect();
			return data.get("Key").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	
	
	//To verify city is available in list or not in JSON file
	private JSONObject getCity(String city) {
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) parser.parse(new FileReader("src/main/java/com/api/service/city.json"));
			for (Object object : jsonArray) {
				JSONObject data = (JSONObject) object;

				if (data.get("city").equals(city)) {
					return data;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
