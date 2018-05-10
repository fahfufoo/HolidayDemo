package com.modal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.utils.HttpUtils;

public class HolidayData {

	private final String KEY = "4bf2a60a-3c39-47be-898c-697452fb6ec7";
	private String HOST_API  = "https://holidayapi.com/v1/holidays";
	public String getHolidayByCountry(String code,int year){
		String url = HOST_API +"?key="+ KEY
				+ "&country=" + code 
				+ "&year=" + year;
		return HttpUtils.httpRequest(url);
//		if(code.equals("NO")){
//			return readLineFromFile("D://norway.txt", "UTF-8");
//		}else{
//			return null;
//		}
	}
	public String readLineFromFile(String fileName, String charsetName) {
		FileInputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		String tmp = "";
		try {			
			is = new FileInputStream(fileName);
			isr = new InputStreamReader(is, charsetName);
			br = new BufferedReader(isr);
			
			String line = null;
			while ((line = br.readLine()) != null) {
				tmp += line;
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) { try { br.close(); } catch (IOException e) {} }
			if(isr != null) { try { isr.close(); } catch (IOException e) {} }
			if(is != null) { try { is.close(); } catch (IOException e) {} }
		}	
		return tmp;
	}
}
