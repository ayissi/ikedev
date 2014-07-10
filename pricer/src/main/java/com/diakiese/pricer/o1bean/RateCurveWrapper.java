package com.diakiese.pricer.o1bean;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;


public class RateCurveWrapper {
	
	Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate ;

		
	public Map<DateTime, List<RateCoordinate>> getRateCurveDateTimed() {
		return rateCoordinatesByDate;
	}
	public void setRateCurveDateTimed(
			Map<DateTime, List<RateCoordinate>> rateCurveDateTimed) {
		this.rateCoordinatesByDate = rateCurveDateTimed;
	}
		
}
