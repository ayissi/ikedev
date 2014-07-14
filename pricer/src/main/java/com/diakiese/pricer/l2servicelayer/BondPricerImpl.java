package com.diakiese.pricer.l2servicelayer;
	
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
	



import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.Interest;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o3utils.BondPricerUtils;

public class BondPricerImpl  {
						
	final static Logger log = Logger.getLogger(BondPricerImpl.class);
			
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
	
	

	/**
	 * retourne le prix de l'obligation b, à la date pricingDate
	 * */
	public Double price(Bond bond, DateTime pricingDate,Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate){
		
		List<RateCoordinate> rateCoordinates = BondPricerUtils.getAccurateRateCoordinates(rateCoordinatesByDate,pricingDate); 
       	
       	long daysRestant = Days.daysBetween(pricingDate, bond.getEmissionDate().plusYears(bond.getBondMaturity())).getDays(); 
       
		double periodRestant = 0.0;
		if(bond.getBondMaturity() > 1) {
			periodRestant = daysRestant/365.0f;
		}else {
			periodRestant = daysRestant/360.0f;
		}
		double period = bond.getPeriodicity()/12.0;	
		Double coupon = bond.getCoupon(); 
		Double ratioAlpha = BondPricerUtils.getRatioAlpha(bond, pricingDate);
	    int nb = 0;
		double price = 0;
		double difference = (ratioAlpha + nb*period) - periodRestant;
		double rateInterpolated =0.0;
		RateCoordinate ceilRateCoordinate ;  
		RateCoordinate floorRateCoordinate;
		while(difference < -0.01){ 
			floorRateCoordinate = BondPricerUtils.getFloorRateCoordinate(rateCoordinates,ratioAlpha + nb*period); 
			ceilRateCoordinate = BondPricerUtils.getCeilRateCoordinate(rateCoordinates, ratioAlpha + nb*period);
			rateInterpolated = BondPricerUtils.linearInterpolation(floorRateCoordinate, ceilRateCoordinate, ratioAlpha);
			price += BondPricerUtils.getFlux(coupon, rateInterpolated, ratioAlpha + nb*period); 
			nb++;
			difference = (ratioAlpha + nb*period) - periodRestant;
		} 
		floorRateCoordinate = BondPricerUtils.getFloorRateCoordinate(rateCoordinates,ratioAlpha + nb*period); 
		ceilRateCoordinate  = BondPricerUtils.getCeilRateCoordinate(rateCoordinates, ratioAlpha + nb*period);
		rateInterpolated = BondPricerUtils.linearInterpolation(floorRateCoordinate, ceilRateCoordinate, ratioAlpha); 
		price = price + BondPricerUtils.getLastFlux(coupon, bond.getNominalAmount(),rateInterpolated,  ratioAlpha + nb*period);
		log.info("X: "+ pricingDate +"Y: " +price);
	 return price;
	}
	
	/**
	 * retourne le prix de l'obligation b, à la date pricingDate
	 * */
	public Double price2(Bond bond, DateTime pricingDate,Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate){	
		 List<RateCoordinate> rateCoordinates = BondPricerUtils.getAccurateRateCoordinates(rateCoordinatesByDate,pricingDate);      	
  
		Double coupon = bond.getCoupon(); 
		Double ratioAlpha = BondPricerUtils.getRatioAlpha(bond, pricingDate);
		double price = 0;
		int incrementDeFlux = 0;
		int nbreDeFluxRestant = BondPricerUtils.getNombreDeFluxRestant(bond, pricingDate);
		double rateInterpolated =0.0;
		RateCoordinate ceilRateCoordinate ;  
		RateCoordinate floorRateCoordinate;
		while(incrementDeFlux < nbreDeFluxRestant -1){ 
			ratioAlpha = ratioAlpha + incrementDeFlux;
			floorRateCoordinate = BondPricerUtils.getFloorRateCoordinate(rateCoordinates,ratioAlpha); 
			ceilRateCoordinate = BondPricerUtils.getCeilRateCoordinate(rateCoordinates,ratioAlpha);
			rateInterpolated = BondPricerUtils.linearInterpolation(floorRateCoordinate, ceilRateCoordinate, ratioAlpha);
			price += BondPricerUtils.getFlux(coupon, rateInterpolated, ratioAlpha); 
			incrementDeFlux++;
		}
		ratioAlpha = ratioAlpha + incrementDeFlux;								
		floorRateCoordinate = BondPricerUtils.getFloorRateCoordinate(rateCoordinates,ratioAlpha); 
		ceilRateCoordinate  = BondPricerUtils.getCeilRateCoordinate(rateCoordinates,ratioAlpha);
		rateInterpolated = BondPricerUtils.linearInterpolation(floorRateCoordinate, ceilRateCoordinate, ratioAlpha); 
		price = price + BondPricerUtils.getLastFlux(coupon, bond.getNominalAmount(),rateInterpolated,ratioAlpha);
//		log.info("X: "+ pricingDate +"Y: " +price);  
	 return price;
	}

}
