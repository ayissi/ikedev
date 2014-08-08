package com.diakiese.pricer.l2servicelayer.pricing;

import org.joda.time.DateTime;

public interface IBondPricerTauxFixeService extends IBondPricerService {
			
	void setFixRate(Double rate);

	 
}
