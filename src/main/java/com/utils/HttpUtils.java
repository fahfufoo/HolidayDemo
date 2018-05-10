package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;


public class HttpUtils {

	private static String charset = "UTF-8";
	public static String httpRequest(String url) {
		
		URL u = null;
	  	HttpURLConnection con = null;
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			
			InputStream is = null;
			if(con.getResponseCode() != 200){
				is = con.getErrorStream();
			}else{
				is = con.getInputStream();
			}
				
			try (
				InputStreamReader isr = new InputStreamReader(is, charset);
				BufferedReader brd = new BufferedReader(isr);) {
				StringBuilder rtn = new StringBuilder(5000);
				String line = "";
				while ((line = brd.readLine()) != null) {
					rtn.append(line);
				}
				return rtn.toString();
	    	}catch(IOException e){
	    		e.printStackTrace();
	    	}
				
		} catch (ConnectException e) {
			String message = "Connection timed out : " + url;
			System.out.print(message);
			return "{\"error\":\" "+message+"}";
		} catch (SocketTimeoutException e) {
			String message = "Socket timed out : " + url;
			System.out.print(message);
			return "{\"error\":\" "+message+"}";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(con != null) {
				con.disconnect();
			}
		}
		return null;
	}
}
