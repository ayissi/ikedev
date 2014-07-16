package com.diakiese.pricer.l2servicelayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
					

import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.RateCurveWrapper;
		
public class BondPricerTauxFixeImpl implements IBondPricerTauxFixeService {
			
	final static Logger log = Logger.getLogger(BondPricerTauxFixeImpl.class);
	
	private Double tauxFixe ;
	private Bond bond ;
	private IRateCurveBuilder rateCurveBuilder ;	
	private Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate;
	private Iinterpolator interpolator = new LinearInterpolator();
	
	public Bond getBond() {
		return bond;
	}

	public void setBond(Bond bond) {
		this.bond = bond;
	}


	public Map<DateTime, List<RateCoordinate>> buildRateCoordinatesByDate() throws IOException {
		rateCurveBuilder = new CSVRateCurveBuilderImpl();  
		RateCurveWrapper rateCurveWrapper = rateCurveBuilder.createRateCurve();  
		return rateCurveWrapper.getRateCoordinatesByDate();
	}


	public void setRateCoordinatesByDate(
			Map<DateTime, List<RateCoordinate>> rateCoordinatesByDate) {
		this.rateCoordinatesByDate = rateCoordinatesByDate;
	}


	@Override
	public Double price(DateTime pricingDate) throws IOException {		
		if(this.rateCoordinatesByDate==null){
			this.rateCoordinatesByDate = buildRateCoordinatesByDate();
		}	 
		List<RateCoordinate> rateCoordinates = getAccurateRateCoordinates(rateCoordinatesByDate,pricingDate);							 
		double period = bond.getPeriodicity()/12.0;															  	
		Double ratioAlpha = getRatioAlpha(bond, pricingDate); 
		double price = 0;		    
		int incrementDeFlux = 0;		  						
		int nbreDeFluxRestant = getNombreDeFluxRestant(bond, pricingDate);																			  
		double rateInterpolated = 0.0; 
		while(incrementDeFlux < nbreDeFluxRestant -1){	
			//1- calculer le taux interpolé x+alpha ie , a ratioAlpha + incrementDeFlux*period !
			rateInterpolated = interpolator.interpolate(rateCoordinates,ratioAlpha + incrementDeFlux*period);//interpolation de taux au rangs alpha, 1+alpha,2+alpha ?	
			
			//2- calculer le flux indice "incrementDeFlux", et actualisé le prix de l'obligation
			price += getFlux(computeCoupon(tauxFixe,bond), rateInterpolated, ratioAlpha + incrementDeFlux*period); //Calcul du flux avec le taux au rang :(ratioAlpha + incrementDeFlux) et a la puissance ratioAlpha +nb*period
			incrementDeFlux++;
		}															 	
		rateInterpolated = interpolator.interpolate(rateCoordinates,ratioAlpha + incrementDeFlux*period);
		price = price + getLastFlux(computeCoupon(tauxFixe,bond), bond.getNominalAmount(),rateInterpolated, ratioAlpha + incrementDeFlux*period); 
	    return price;
	}


	 /**
	  * calcule le dernier flux
	  * */
	  private double getLastFlux(double coupon, double nominal, double rate,double puissance) {	
		double denominateur = Math.pow(1+rate, puissance); 
		return (coupon+nominal)/denominateur;
	  }
		

	 /**
	  * calcule le flux simple
	  * */
	  private  double getFlux(double coupon, double rate, double puissance) {
		double denominateur = Math.pow(1+rate, puissance); 
		return coupon/denominateur;
	  }
	
	  			
	   /**
	    * retourne la liste des RateCoordinates associée à pricingDate.
	    * Lorsque <b>pricingDate</b> n'a pas d'entrée dans <b>rateCoordinatesByDate</b> 
	    * la méthode retourne la premiere entree entrée qui précède  <b>pricingDate</b> 
	    * @param rateCoordinatesByDate la map qui modélise toute les courbes des taux
	    * @param dateEntry la date dont on souhaite recupérer la courbe de taux
	    * */
	   private  List<RateCoordinate> getAccurateRateCoordinates(Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate,DateTime dateEntry){
			List<RateCoordinate> rateCoordinates = rateCoordinatesByDate.get(dateEntry);
	       	DateTime accurateDay = dateEntry;																							
	       	rateCoordinates = rateCoordinatesByDate.get(dateEntry);
	       	//Si une date de facturation n'a pas d'entree ds la courbe des taux, alors prendre la première date la précédent qui a une entrée ds la courbe
	       	while(rateCoordinates==null){
	       		accurateDay = accurateDay.minusDays(1);
	       		rateCoordinates = rateCoordinatesByDate.get(accurateDay); 
	       	}
	       	return rateCoordinates;
	 }
	  
   	/**
   	 * retourne la valeur "alpha" de l'algo de pricing 
   	 * @param bond l'obligation
   	 * @param pricingDate la date de pricing 
   	 * */
	   private  Double getRatioAlpha(Bond bond, DateTime pricingDate){
	   DateTime dateProchainCoupon = getDateProchainCoupon(pricingDate,bond);
	   Double diffDays = new Double(Days.daysBetween(pricingDate, dateProchainCoupon).getDays());
	   Double daysPerYear = 360.0;
	   if(bond.getBondMaturity()>1){
		   daysPerYear = 365.0;
	    }
	  return diffDays/daysPerYear;
    }
	   	
	/**
    * retourne la date du prochain coupon associée à <b>bond</b> par rappord a
    * la date de pricing <b>pricingDate</d> 
    * */
	   private DateTime getDateProchainCoupon(DateTime pricingDate,Bond bond){
	   DateTime dateProchainCoupon = bond.getEmissionDate();
	   while(pricingDate.compareTo(dateProchainCoupon)>0){
		   dateProchainCoupon = dateProchainCoupon.plusMonths(bond.getPeriodicity());
	   }
	   return dateProchainCoupon;
   }
   
	/**
	 * retourne le nombre de flux restant de l'obligation, relativement à la date de pricing
	 * @param bond l'obligation
	 * @param pricingDate la date de pricing						    
	 * */
	   private int getNombreDeFluxRestant(Bond bond, DateTime pricingDate){
		int nbreFluxRestant =0;
		DateTime datePremierCoupon = bond.getEmissionDate().plusMonths(bond.getPeriodicity());
		for(DateTime billingDate = datePremierCoupon; billingDate.compareTo(bond.getEmissionDate().plusYears(bond.getBondMaturity()))<=0; billingDate = billingDate.plusMonths(bond.getPeriodicity())){
			if(billingDate.compareTo(pricingDate)>0){
				nbreFluxRestant ++;
			}
		}
		return nbreFluxRestant;  		
	}

	   private Double computeCoupon(Double tauxFacial, Bond bond){
		   Double periodicityInYear = new Double(bond.getPeriodicity())/12.0 ;
		   return tauxFacial*periodicityInYear*bond.getNominalAmount();
	   }
	
	@Override
	public void setRate(Double rate) {
	this.tauxFixe = rate ;

	}

}
