package com.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.api.service.DayofDailyForecastsService;

@Controller
@RequestMapping("/api")
public class ForecastController {

	@Autowired
	DayofDailyForecastsService forecastService;
	
	@RequestMapping
	public String getPage(){
		return "city";
	}
	
	@GetMapping("/forecast")
	public ModelAndView getForecast(HttpServletRequest request, @RequestParam("city") String city){
		JSONObject jsonObject = forecastService.getDayForecast(city, request);
		ModelAndView view = new ModelAndView("display");
		request.getSession().setAttribute("data", jsonObject);
		view.addObject("data",jsonObject);
		return view;
	}
	
	@GetMapping("/forecast/inCelcius")
	public ModelAndView getInCelcius(HttpServletRequest request){
		JSONObject jsonObject =null;
		String cityCode = request.getSession().getAttribute("cityCode").toString();
		ModelAndView view = new ModelAndView("display");
		jsonObject = forecastService.getTempInCelsius(cityCode);
		view.addObject("data",jsonObject);
		return view;
	}
	@GetMapping("/forecast/inFarhenheit")
	public ModelAndView getInFarhenheit(HttpServletRequest request){
		JSONObject jsonObject =null;
		String cityCode = request.getSession().getAttribute("cityCode").toString();
		jsonObject = forecastService.getTempInFarhenheit(cityCode);
		ModelAndView view = new ModelAndView("display");
		view.addObject("data",jsonObject);
		return view;
	}
}
