package com.diakiese.pricer.l2servicelayer.pricing;

import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.TYPE_PRICING;

public interface IBondPricerService {
	
	Double price(DateTime pricingDate,TYPE_PRICING pricingType);

	Double priceCouponCouru(DateTime pricingDate);     
	
	Double priceSimpleCoupon(DateTime pricingDate); 
}
