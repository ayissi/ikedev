package com.diakiese.pricer.o3utils;

import java.util.List;
import java.util.Map;
			
import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.RateCoordinate;

public class RateCurveWrapper {
	
	Map<DateTime,List<RateCoordinate>> rateCurveDateTimed ;

	Map<String,List<RateCoordinate>>  rateCurveString ;
		
	public Map<DateTime, List<RateCoordinate>> getRateCurveDateTimed() {
		return rateCurveDateTimed;
	}
	public void setRateCurveDateTimed(
			Map<DateTime, List<RateCoordinate>> rateCurveDateTimed) {
		this.rateCurveDateTimed = rateCurveDateTimed;
	}
	public Map<String, List<RateCoordinate>> getRateCurveString() {
		return rateCurveString;
	}
	public void setRateCurveString(Map<String, List<RateCoordinate>> rateCurveString) {
		this.rateCurveString = rateCurveString;
	}
		
}
