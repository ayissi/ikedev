package com.diakiese.pricer.l2servicelayer;
	
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.Interest;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o3utils.BondPricerUtils;

public class BondPricerImpl  {
						




//	public Double getBondPrice(Bond bond, DateTime pricingDate, Map<DateTime, List<RateCoordinate>> rateCoordinatesByDate){
//	
//		//les dates de paiement de coupons
//		List<DateTime> futuresBillingDates = BondPricerUtils.getFuturesBillingDates(bond.getPeriodicity(), bond.getBondMaturity(), bond.getEmissionDate(), pricingDate);
//		
//		//pour une date donnée, le montant du coupon associé
//		Map<DateTime,Double> fluxByBillingDate = BondPricerUtils.getFluxByBillingDates(bond,futuresBillingDates);
//		
//		Map<DateTime,Interest> interestByBillingDate = BondPricerUtils.getInterestByBillingDate(pricingDate,futuresBillingDates, rateCoordinatesByDate);
//	
//		return BondPricerUtils.getBondPrice(fluxByBillingDate, interestByBillingDate, futuresBillingDates);
//
//	}  
	

	/**
	 * Calcule le prix de l'obligation <b>bond</b>	à la date pricingDate, avec la courbe 
	 * de taux donnée par rateCoordinates	
	 * */
	public Double getBondPrice(Bond bond, DateTime pricingDate, Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate){						
		//les dates de paiement de coupons
		List<DateTime> futuresBillingDates = BondPricerUtils.getFuturesBillingDates(bond.getPeriodicity(), bond.getBondMaturity(), bond.getEmissionDate(), pricingDate);
		
		//pour une date donnée, le montant du coupon associé
		Map<DateTime,Double> fluxByBillingDate = BondPricerUtils.getFluxByBillingDates(bond,futuresBillingDates);
		
		Map<DateTime,Interest> interestByBillingDate = BondPricerUtils.getInterestByBillingDate(pricingDate,futuresBillingDates, rateCoordinatesByDate);
	
		return BondPricerUtils.getBondPrice(fluxByBillingDate, interestByBillingDate, futuresBillingDates);

	}
}
