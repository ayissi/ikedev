package com.diakiese.pricer.l2servicelayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.RateCoordinate;
		
public class BondPricerTauxVariableImpl implements IBondPricerService{

	@Override
	public Double price(DateTime pricingDate) throws IOException {
		Double toModif = new Double(25.0);
		return toModif;
	}

	@Override
	public Map<DateTime, List<RateCoordinate>> buildRateCoordinatesByDate()
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
