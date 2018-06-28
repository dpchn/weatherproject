package com.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weatherproject.AccuweatherInfo;

@Service
public class DayofDailyForecastsService {

	
	@Autowired
	CityService cityService;
	public JSONObject getDayForecast(String city, HttpServletRequest httpServletRequest) {
		boolean isAvailable = cityService.isCityAvailable(city);
		System.out.println(isAvailable);
		JSONObject object = new JSONObject();
		if (isAvailable) {
			object.put("message", "City is no available in list");
		} else {
			String cityCode = cityService.getCityCode(city.toLowerCase());
			System.out.println("City code " + cityCode);
			object = getTempInFarhenheit(cityCode);
			object.put("cityCode", cityCode);
			httpServletRequest.getSession().setAttribute("cityCode", cityCode);
		}

		return object;
	}

	private JSONObject extractInfoFromJSON(JSONObject jsonObject) {
		JSONArray jsonArray = (JSONArray) jsonObject.get("DailyForecasts");
		JSONObject temperatureJsonObject = (JSONObject) ((JSONObject) jsonArray.get(0)).get("Temperature");
		System.out.println(temperatureJsonObject);
		JSONObject tempData = new JSONObject();
		JSONObject minTemp = (JSONObject) temperatureJsonObject.get("Minimum");
		tempData.put("min", minTemp.get("Value"));
		JSONObject maxTemp = (JSONObject) temperatureJsonObject.get("Maximum");
		tempData.put("max", maxTemp.get("Value"));
		return tempData;

	}

	//Temperature in Farhenheit
	public JSONObject getTempInFarhenheit(String cityCode) {
		JSONObject tempData = extractInfoFromJSON(getInfo(cityCode));
		tempData.put("unit", " °F");
		tempData.put("type", "f");
		return tempData;
	}
	
	
	//Temperature in Celcius
	public JSONObject getTempInCelsius(String cityCode) {
		JSONObject tempData = extractInfoFromJSON(getInfo(cityCode));
		Float minTemp = Float.valueOf(tempData.get("min").toString());
		minTemp=(minTemp - 32)*5/9;
		Float maxTemp = Float.valueOf(tempData.get("max").toString());
		maxTemp=(maxTemp - 32)*5/9;
		tempData.put("min", minTemp);
		tempData.put("max", maxTemp);
		tempData.put("unit", " °C");
		tempData.put("type", "c");
		return tempData;
	}

	// Get Weather Report
	private JSONObject getInfo(String cityId) {
		final String apiKey = AccuweatherInfo.apiKey;
		String forcastUrl = "http://dataservice.accuweather.com/forecasts/v1/daily/1day/" + cityId + "?" + "apikey="
				+ apiKey;

		try {
			URL url = new URL(forcastUrl);
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

			// org.json.JSONObject data = new
			// org.json.JSONObject(response.toString());
			JSONParser parser = new JSONParser();
			JSONObject data = (JSONObject) parser.parse(response.toString());
			// System.out.println(data);
			connection.disconnect();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
