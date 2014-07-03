package com.diakiese.pricer.o3utils;

import java.io.IOException;



public interface IRateCurveBuilder {

	RateCurveWrapper createRateCurve(String filePath) throws IOException; 
}
