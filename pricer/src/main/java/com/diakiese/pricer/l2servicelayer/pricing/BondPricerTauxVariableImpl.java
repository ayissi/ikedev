package com.diakiese.pricer.l2servicelayer.pricing;


import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.TYPE_PRICING;
		
public class BondPricerTauxVariableImpl implements IBondPricerService{

    final Double CONSTANTE = new Double(25.0);

	@Override
	public Double price(DateTime pricingDate, TYPE_PRICING pricingType) {
		
		return CONSTANTE ;
	}

	@Override
	public Double priceCouponCouru(DateTime pricingDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double priceSimpleCoupon(DateTime pricingDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
