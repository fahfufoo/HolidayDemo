package com.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.modal.HolidayData;

@RestController
public class HolidayService {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private String chaset = "UTF-8";
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/holidayInfo")
	public @ResponseBody void HolidayAPI(HttpServletResponse response, @RequestParam("country1") String country1,
			@RequestParam("country2") String country2, @RequestParam("date") String dateTmp) throws IOException{
		
		JSONParser jParser = new JSONParser();
		JSONObject m = new JSONObject();
		
		response.setCharacterEncoding(chaset);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
			Date inputDate;
			try {
				inputDate = dateFormat.parse(dateTmp);
			
				Calendar cal = Calendar.getInstance();
				cal.setTime(inputDate);
				int year = cal.get(Calendar.YEAR);
				
			try{
				HolidayData modal = new HolidayData();
				
				String data1 = modal.getHolidayByCountry(country1, year);
				JSONObject jsonResult1 = getHolidaysResult(data1 != null?(JSONObject) jParser.parse(data1): null, m, out, country1);
				if(jsonResult1 == null){
					return;
				}
				
				String data2 = modal.getHolidayByCountry(country2, year);
				JSONObject jsonResult2 = getHolidaysResult(data2 != null?(JSONObject) jParser.parse(data2):null, m, out, country2);
				if(jsonResult2 == null){
					return;
				}
				
				SortedMap<String, Object> sortedHolidayMap1 = new TreeMap<String, Object>(
						new ObjectMapper().readValue(jsonResult1.toString(), HashMap.class));
				
				HashMap<String,Object> holidayMap2 =
				        new ObjectMapper().readValue(jsonResult2.toString(), HashMap.class);
				Set<String> keySet2 = holidayMap2.keySet();
				
				for(String d : sortedHolidayMap1.keySet()){
					Date date = dateFormat.parse(d);
					if(date.before(inputDate) 
							|| (!keySet2.contains(d))){
						continue;
					}
					String name1 = getHolidayName(jsonResult1.get(d));
					String name2 = getHolidayName(jsonResult2.get(d));
					
					m.put("date", d);
					m.put(country1, name1);
					m.put(country2, name2);
					break;
					
				}
				if(m.size() <= 0){
					m.put("message", "no holiday fall on the same day after " +  dateFormat.format(inputDate));
				}
			}catch(Exception e){
				m.put("error", "Internal error");
				e.printStackTrace();
			}
		} catch (java.text.ParseException e1) {
			m.put("error", "date format is wrong, please check and try again ") ;
			e1.printStackTrace();
		}
		out.write(m.toString());
		
	}
	@SuppressWarnings("unchecked")
	private JSONObject getHolidaysResult(JSONObject data,JSONObject m, PrintWriter out, String country){
		JSONObject result = null;
		if(data == null){
			m.put("error", "cannot get holiday data from " + country) ;
			out.write(m.toString());
		}else if(data.get("error") != null){
			m.put("error", (String) data.get("error"));
			out.write(m.toString());
		}else if (data.get("holidays") != null){
			result = (JSONObject)data.get("holidays");
		}
		return result;
	}
	
	private String getHolidayName(Object data) throws ParseException{
		JSONArray jsonArray = (JSONArray) data;
		String name = "";
		for(Object obj : jsonArray){
			JSONObject jObj = (JSONObject) obj;
			name += " ," + jObj.get("name");
		}
		
		return name.substring(2);
	}
}
