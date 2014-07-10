/**
 * 
 */
package com.diakiese.pricer.o1bean;

import java.util.List;

import org.joda.time.DateTime;

/**
 * @author guy.belomo 
 */         
public class RateCurveEntry {
	
	private String key ;
	private DateTime date;
	private List<RateCoordinate> rateCoordinates;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public List<RateCoordinate> getRateCoordinates() {
		return rateCoordinates;
	}
	public void setRateCoordinates(List<RateCoordinate> rateCoordinates) {
		this.rateCoordinates = rateCoordinates;
	}
	
	public void initDate(){
		String ddMMyyyy [] = getKey().split("/");
	    this.date = new DateTime(new Integer(ddMMyyyy[2]),new Integer(ddMMyyyy[1]),new Integer(ddMMyyyy[0]),0,0);  	
	}
}
