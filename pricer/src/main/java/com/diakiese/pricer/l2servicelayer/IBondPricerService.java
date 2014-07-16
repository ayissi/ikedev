package com.diakiese.pricer.l2servicelayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.RateCoordinate;

public interface IBondPricerService {
	
	Double price(DateTime pricingDate) throws IOException;     
	
	Map<DateTime, List<RateCoordinate>> buildRateCoordinatesByDate() throws IOException; 
}
