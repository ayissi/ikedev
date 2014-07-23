package com.diakiese.pricer.l2servicelayer;

import java.io.IOException;

import com.diakiese.pricer.o1bean.RateCurveWrapper;



public interface IRateCurveBuilder {
	
	RateCurveWrapper createRateCurve(String file) throws IOException; 
	
	String getFile();
}
