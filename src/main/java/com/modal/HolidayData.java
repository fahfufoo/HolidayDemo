package com.modal;

import com.utils.HttpUtils;

public class HolidayData {

	private final String KEY = "4bf2a60a-3c39-47be-898c-697452fb6ec7";
	private String HOST_API  = "https://holidayapi.com/v1/holidays";
	public String getHolidayByCountry(String code,int year){
		String url = HOST_API +"?key="+ KEY
				+ "&country=" + code 
				+ "&year=" + year;
		return HttpUtils.httpRequest(url);
	}
}
