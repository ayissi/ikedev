package com.diakiese.pricer.o3utils;
	
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RateCurveWrapper {
	
	Map<String,List<Double>> rateCurve ;

	public Map<String, List<Double>> getRateCurve() {
		return rateCurve;
	}


	public void setRateCurve(Map<String, List<Double>> rateCurve) {
		this.rateCurve = rateCurve;
	}
		
}
