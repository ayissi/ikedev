package com.diakiese.pricer.l2servicelayer;
         
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.TYPE_PRICING;
import com.diakiese.pricer.o3utils.BondPricerUtils;
		
public class BondPricerTauxFixeImpl implements IBondPricerTauxFixeService {
			
	final static Logger log = Logger.getLogger(BondPricerTauxFixeImpl.class);
	
	private Double tauxFixe ;
	private Bond bond ;
	private Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate;
	private Iinterpolator interpolator;
	
	public void setInterpolator(Iinterpolator interpolator) {
		this.interpolator = interpolator;
	}

	public Map<DateTime, List<RateCoordinate>> getRateCoordinatesByDate() {
		return rateCoordinatesByDate;
	}

	public Bond getBond() {
		return bond;
	}

	public void setBond(Bond bond) {
		this.bond = bond;
	}


	public BondPricerTauxFixeImpl(IRateCurveBuilder rateCurveBuilder){
		try{
			this.rateCoordinatesByDate = rateCurveBuilder.createRateCurve().getRateCoordinatesByDate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Double price(DateTime pricingDate,TYPE_PRICING typePricing) {
		Double thePrice = new Double(0.0);
		switch(typePricing){
		case  COUPON_SIMPLE:
			thePrice = priceSimpleCoupon(pricingDate);
			break;
		case COUPON_COURU : 
			thePrice = priceCouponCouru(pricingDate);
			break;
		default:
			break; 
		}
		return thePrice;
	}

	public Double priceSimpleCoupon(DateTime pricingDate){
		double price = 0;  
		List<RateCoordinate> rateCoordinates = getAccurateRateCoordinates(rateCoordinatesByDate,pricingDate);	
		if(rateCoordinates.size()>0){ 
			double period = bond.getPeriodicity()/12.0;															  	
			Double ratioAlpha = getRatioAlpha(bond, pricingDate);   
			int incrementDeFlux = 0;		  						
			int nbreDeFluxRestant = getNombreDeFluxRestant(bond, pricingDate);																			  
			double rateInterpolated = 0.0; 
			while(incrementDeFlux <= nbreDeFluxRestant -1){	
				//1- calculer le taux interpolé x+alpha ie , a ratioAlpha + incrementDeFlux*period !
				rateInterpolated = interpolator.interpolate(rateCoordinates,ratioAlpha + incrementDeFlux*bond.getPeriodicityInYear());//interpolation de taux au rangs alpha, 1+alpha,2+alpha ?	
			   	
				//2- calculer le flux indice "incrementDeFlux", et actualisé le prix de l'obligation
				price += getFlux(computeCoupon(getTauxFixe(),bond), rateInterpolated, ratioAlpha + incrementDeFlux*period); //Calcul du flux avec le taux au rang :(ratioAlpha + incrementDeFlux) et a la puissance ratioAlpha +nb*period
//				log.info("DATE: " + BondPricerUtils.getDateStringFormat(pricingDate) +"\tRATIO ALPHA: " + ratioAlpha + "\tPUISSANCE: "+ ratioAlpha + incrementDeFlux*period +"\tNBRE_FLUX_RESTANT: " + nbreDeFluxRestant +"\tINCR_FLUX: " + incrementDeFlux + "\tPRIX ACTUALISE: " + price);												 
				incrementDeFlux++;
			}															 	
			rateInterpolated = interpolator.interpolate(rateCoordinates,ratioAlpha + incrementDeFlux*period);
			price = price + getLastFlux(computeCoupon(getTauxFixe(),bond), bond.getNominalAmount(),rateInterpolated, ratioAlpha + incrementDeFlux*period); 
//			log.info("DATE: " + BondPricerUtils.getDateStringFormat(pricingDate) +"\tRATIO ALPHA: " + ratioAlpha +"\tPUISSANCE: "+ ratioAlpha + incrementDeFlux*period +"\tPRIX_FINAL: "  + price + "\tNBRE_FLUX_TOTAL: " +nbreDeFluxRestant + "\n");         
		}
	    return price;
	}
	

	public Double priceCouponCouru(DateTime pricingDate){
		double price = 0;  
		List<RateCoordinate> rateCoordinates = getAccurateRateCoordinates(rateCoordinatesByDate,pricingDate);	
		if(rateCoordinates.size()>0){ 
			double period = bond.getPeriodicity()/12.0;															  	
			Double ratioAlpha = getRatioAlpha(bond, pricingDate);   
			int incrementDeFlux = 0;		  						
			int nbreDeFluxRestant = getNombreDeFluxRestant(bond, pricingDate);																			  
			double rateInterpolated = 0.0; 
			while(incrementDeFlux <= nbreDeFluxRestant -1){	
				//1- calculer le taux interpolé x+alpha ie , a ratioAlpha + incrementDeFlux*period !
				rateInterpolated = interpolator.interpolate(rateCoordinates,ratioAlpha + incrementDeFlux*period);//interpolation de taux au rangs alpha, 1+alpha,2+alpha ?	
			   	
				//2- calculer le flux indice "incrementDeFlux", et actualisé le prix de l'obligation
				price += getFlux(computeCouponCouru(bond,pricingDate), rateInterpolated, ratioAlpha + incrementDeFlux*period); //Calcul du flux avec le taux au rang :(ratioAlpha + incrementDeFlux) et a la puissance ratioAlpha +nb*period
//				log.info("DATE: " + BondPricerUtils.getDateStringFormat(pricingDate) +"\tRATIO ALPHA: " + ratioAlpha + "\tPUISSANCE: "+ ratioAlpha + incrementDeFlux*period +"\tNBRE_FLUX_RESTANT: " + nbreDeFluxRestant +"\tINCR_FLUX: " + incrementDeFlux + "\tPRIX ACTUALISE: " + price);												 
				incrementDeFlux++;
			}															 	
			rateInterpolated = interpolator.interpolate(rateCoordinates,ratioAlpha + incrementDeFlux*period);
			price = price + getLastFlux(computeCouponCouru(bond,pricingDate), bond.getNominalAmount(),rateInterpolated, ratioAlpha + incrementDeFlux*period); 
//			log.info("DATE: " + BondPricerUtils.getDateStringFormat(pricingDate) +"\tRATIO ALPHA: " + ratioAlpha +"\tPUISSANCE: "+ ratioAlpha + incrementDeFlux*period +"\tPRIX_FINAL: "  + price + "\tNBRE_FLUX_TOTAL: " +nbreDeFluxRestant + "\n");         
		}
	    return price;
	}

	
	
	/**
	 * retourne la valeur du coupon couru a la date de pricing
	 * @param bond
	 * @param pricingDate 
	 * */
	public Double computeCouponCouru(Bond bond, DateTime pricingDate){
		Double montantCoupon = computeCoupon(getTauxFixe(),bond);
		Double elapsingDays = getJoursDepuisDerniereEcheance(bond, pricingDate);
		Double joursSurPeriodicite = elapsingDays/(bond.getPeriodicityInYear()*365) ;
		return montantCoupon*joursSurPeriodicite; 		
	}
	

	/**
	 * retourne le nombre de jours écoulés depuis la dernière échéance
	 * @param bond l'obligation
	 * @param pricingDate la date de pricing
	 * */
	public Double getJoursDepuisDerniereEcheance(Bond bond, DateTime pricingDate){
		DateTime dateEmission = bond.getEmissionDate();
		DateTime dateDerniereEcheance = dateEmission;
		Double periodicityInYear = bond.getPeriodicityInYear(); 
		Double periodicityInDays = periodicityInYear*365;
		while(pricingDate.compareTo(dateDerniereEcheance)>0){  
			dateDerniereEcheance = dateDerniereEcheance.plusDays(periodicityInDays.intValue()); 
		}
		dateDerniereEcheance = dateDerniereEcheance.minusDays(periodicityInDays.intValue()); 
		int diff = Days.daysBetween(dateDerniereEcheance, pricingDate).getDays();
		return new Double(diff);
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
	   public List<RateCoordinate> getAccurateRateCoordinates(Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate,DateTime dateEntry){
		   List<RateCoordinate> rateCoordinates = new ArrayList<RateCoordinate>();
		   if(rateCoordinatesByDate!=null){
		       	DateTime accurateDay = dateEntry;																							
		       	rateCoordinates = rateCoordinatesByDate.get(dateEntry);
		       	if(rateCoordinates==null){
		       		do{
			       		accurateDay = accurateDay.minusDays(1);		
			       		rateCoordinates = rateCoordinatesByDate.get(accurateDay); 
		       		}while((rateCoordinates==null) ||(rateCoordinates.get(0)==null));
		       	}else if(rateCoordinates.get(0)==null){
			       	while(rateCoordinates.get(0)==null){
			       		accurateDay = accurateDay.minusDays(1);		
			       		rateCoordinates = rateCoordinatesByDate.get(accurateDay);
			       		while(rateCoordinates==null){
				       		accurateDay = accurateDay.minusDays(1);		
				       		rateCoordinates = rateCoordinatesByDate.get(accurateDay); 
			       		}
		       	}
		   }
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
   
	public Double getTauxFixe() {
		return tauxFixe;
	}

	/**
	 * retourne le nombre de flux restant de l'obligation, relativement à la date de pricing
	 * @param bond l'obligation
	 * @param pricingDate la date de pricing						    
	 * */ 
	   public int getNombreDeFluxRestant(Bond bond, DateTime pricingDate){
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
	public void setFixRate(Double rate) {
	this.tauxFixe = rate ;
	}

}
