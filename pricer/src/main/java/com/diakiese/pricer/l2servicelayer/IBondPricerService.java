package com.diakiese.pricer.l2servicelayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.TYPE_PRICING;

public interface IBondPricerService {
	
	Double price(DateTime pricingDate,TYPE_PRICING pricingType);     
	
	
}
