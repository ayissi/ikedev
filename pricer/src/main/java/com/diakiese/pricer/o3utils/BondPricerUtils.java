package com.diakiese.pricer.o3utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
																
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;

                                                
public class BondPricerUtils {
								
	final static Logger log = Logger.getLogger(BondPricerUtils.class);
	/**
	 * retourne l'ensemble des dates des coupons associés à une Obligation 
	 * @param bondPeriodicity la periodicite de l'obligation (en mois)
	 * @param bondMaturity la maturité de l'obligation (en années)
	 * @param bondEmissionDate la date d'emission de l'obligation 
	 * */
	public static Set<DateTime> getBillingDates(int bondPeriodicity,int bondMaturity,DateTime bondEmissionDate){
		Set<DateTime> billingDates = new LinkedHashSet<DateTime>();
		DateTime datePremierCoupon = bondEmissionDate.plusMonths(bondPeriodicity);
		for(DateTime billingDate = datePremierCoupon; billingDate.compareTo(bondEmissionDate.plusYears(bondMaturity))<=0; billingDate = billingDate.plusMonths(bondPeriodicity)){
			billingDates.add(billingDate);
		}
		return billingDates;
	}
	
	
	/**
	 * retourne l'ensemble des dates des coupons futurs associés à une Obligation relativement à la date de pricing
	 * @param bondPeriodicity la periodicite de l'obligation (en mois)
	 * @param bondMaturity la maturité de l'obligation (en années)
	 * @param bondEmissionDate la date d'emission de l'obligation
	 * @param pricingDate la date de pricing 
	 * */
	public static Set<DateTime> getFuturesBillingDates_toRefactor(int bondPeriodicity,int bondMaturity,DateTime bondEmissionDate,DateTime pricingDate){
		Set<DateTime> billingDates = getBillingDates(bondPeriodicity, bondMaturity, bondEmissionDate);
		Set<DateTime> futuresBillingDates = new LinkedHashSet<DateTime>();
		for(DateTime date:billingDates){
			if(date.compareTo(pricingDate)>=0){
				futuresBillingDates.add(date);
			}
		}		
		return futuresBillingDates ;
	}
	

	/**
	 * retourne l'ensemble des dates des flux futurs associés à une Obligation relativement à la date de pricing
	 * @param bondPeriodicity la periodicite de l'obligation (en mois)
	 * @param bondMaturity la maturité de l'obligation (en années)
	 * @param bondEmissionDate la date d'emission de l'obligation
	 * @param pricingDate la date de pricing 
	 * */				
	public static List<DateTime> getFuturesBillingDates(int bondPeriodicity,int bondMaturity,DateTime bondEmissionDate,DateTime pricingDate){
		Set<DateTime> billingDates = getBillingDates(bondPeriodicity, bondMaturity, bondEmissionDate);
		List<DateTime> futuresBillingDates = new LinkedList<DateTime>();
		for(DateTime date:billingDates){
			if(date.compareTo(pricingDate)>=0){
				futuresBillingDates.add(date);
			}
		}		
		return futuresBillingDates ;
	}

	

	/**
	 * retourne (paymentDate-pricingDate)/365
	 * @param pricingDate la date de pricing
	 * @param paymentDate la date de paiement 
	 * */		
	public static Double getDurationRatio(DateTime pricingDate, DateTime paymentDate){
		int interval = Days.daysBetween(pricingDate, paymentDate).getDays();	
		Double d = new Double(interval);
		Double ratioDuration = d/360 ; 
//		log.info("Pricing Date : " + pricingDate.toString() +"\tPayment Date: " +paymentDate+ "\t ==> ecart:" + interval +"\t ==> ratioDuration: " + ratioDuration); 
		return ratioDuration ;		
	}
	

	

	/**
	 * dans la courbe des taux, la zone de periode est au format standard
	 * ZCxyzYR où xyz représente la portion d'année. Par exemple,
	 * ZC025YR signifie que c'est la zone de periode correspondant au 1/4 d'année
	 * Cette méthode prend en entrée une periode de zone(ZCxyzYR) et retourne la valeur 
	 * en année correspondant à xyz
	 * @param periodZone chaine de caractère correspondant à ZCxyzYR
	 * */
	public static Double getPeriodYear(String periodZone){
		String[] tabToMov_ZC = periodZone.split("ZC");
		String[] tabToMov_YR = tabToMov_ZC[1].split("YR");
		return new Double(tabToMov_YR[0])*0.01 ; 
	}


	/**
	 * retourne la date au format jj/mm/yyyy
	 * @param date la date
	 * */
	public static String getDateStringFormat(DateTime date){
		String laDate = new StringBuilder().append(date.getDayOfMonth()).append("/").append(date.getMonthOfYear()).append("/").append(date.getYear()).toString();
		return laDate;
	}
		

	/**
	 * retourne le nombre de flux restant de l'obligation, relativement à la date de pricing
	 * @param bond l'obligation
	 * @param pricingDate la date de pricing						    
	 * */
	public static int getNombreDeFluxRestant(Bond bond, DateTime pricingDate){
		int nbreFluxRestant =0;
		DateTime datePremierCoupon = bond.getEmissionDate().plusMonths(bond.getPeriodicity());
		for(DateTime billingDate = datePremierCoupon; billingDate.compareTo(bond.getEmissionDate().plusYears(bond.getBondMaturity()))<=0; billingDate = billingDate.plusMonths(bond.getPeriodicity())){
			if(billingDate.compareTo(pricingDate)>0){
				nbreFluxRestant ++;
			}
		}
		return nbreFluxRestant;  		
	}
					
	/**
	 * retourne le taux interpolé d'un RateCoordinate X 
	 * @param floorElt le RateCoordinate précedent X
	 * @param ceilElt  le RateCoordinate suivant X
	 * @param midPeriodYear la periodYear de X, borné par floorElt.periodYear et ceilElt.periodYear 
	 * */				  
	public static Double linearInterpolation(RateCoordinate floorElt, RateCoordinate ceilElt, Double midPeriodYear){
		Double d = new Double(0.0);		
		if(!ceilElt.getPeriodYear().equals(floorElt.getPeriodYear())&&(floorElt!=null)&&(ceilElt!=null)&&(floorElt.getRate()!=null)&&(ceilElt.getRate()!=null)){
//			log.info("FLOOR PERIOD YEAR: " +floorElt.getPeriodYear()+ "\tCEIL PERIOD YEAR: " + ceilElt.getPeriodYear() +"\tMID PERIOD YEAR: " + midPeriodYear +"\tFLOOR RATE" + floorElt.getRate() + "\tCEIL RATE " + ceilElt.getRate());
			Double quotient = ceilElt.getPeriodYear()-floorElt.getPeriodYear();  
			Double d1 = ((ceilElt.getPeriodYear()-midPeriodYear)/(quotient))*floorElt.getRate();
			Double d2 = ((midPeriodYear-floorElt.getPeriodYear())/(quotient))*ceilElt.getRate();	
			d = d1+d2;
		}		
		return d; 
	}

		
	/**
	 * retourne le RateCoordinate X tel que X.periodYear précède _periodYear
	 * @param rateCoordinates la liste des (année,taux)
	 * @param _periodYear l'année dont on cherche le taux
	 * */										
	public static RateCoordinate getFloorRateCoordinate(List<RateCoordinate> rateCoordinates, Double _periodYear){
		RateCoordinate rateCoordinate = new RateCoordinate();
		Double periodYearCent = _periodYear * 100.0;							
		Integer _period = periodYearCent.intValue();
		Integer position = _period/25;														
//		log.info("\tTaille: " + rateCoordinates.size()  +"\tPosition: " + position );
		if((rateCoordinates==null)||(position==0)||(position> rateCoordinates.size()-1)){
			rateCoordinate.setRate(0.0);
			rateCoordinate.setPeriodYear(_periodYear); 
		}else{															
			rateCoordinate = rateCoordinates.get(position-1) ;
		}
		return rateCoordinate ; 
	}
	

	/**
	 * retourne le RateCoordinate X tel que X.periodYear suit _periodYear
	 * @param rateCoordinates la liste des (année,taux)
	 * @param _periodYear l'année dont on cherche le taux
	 * */
	public static RateCoordinate getCeilRateCoordinate(List<RateCoordinate> rateCoordinates, Double _periodYear){
		RateCoordinate rateCoordinate = new RateCoordinate();
		Double periodYearCent = _periodYear * 100.0;
		Integer _period = periodYearCent.intValue();
		Integer position = _period/25;
		if((rateCoordinates==null)||(position==0)||(position> rateCoordinates.size()-1)){ 
			rateCoordinate.setRate(0.0);
			rateCoordinate.setPeriodYear(_periodYear);
		}else{  
			rateCoordinate = rateCoordinates.get(position) ;
		}
		return rateCoordinate; 
	}

	

	/**
	 * calcule et retourne la valeur du taux correspondant à <b>_periodYear</b>	
	 * @param rateCoordinates les paramètres de la LINE_CHART obtenu de la courbe des taux sur une date,
	 *        c'est de cette LINE_CHART que sera calculé le TAUX.   
	 * @param periodYear la "date" dont il n'existe pas le taux sur LINE_CHART 
	 * */
	public static Double  computeInterpolatedRate(List<RateCoordinate> rateCoordinates, Double periodYear){ 		
		RateCoordinate floorRateCoordinate = getFloorRateCoordinate(rateCoordinates, periodYear);
		RateCoordinate ceilRateCoordinate = getCeilRateCoordinate(rateCoordinates, periodYear);
		return linearInterpolation(floorRateCoordinate, ceilRateCoordinate, periodYear);   
	}


	/**
	 * retourne les dates effectives des taux sur une courbe de taux.
	 * En effet lorsque sur une date de flux <b>D</b>, il n'ya pas d'entrée dans la courbe de taux,
	 * je change cette date <b>D</b> par la première date la précédent et ayant une entrée dans dans courbe des taux.
	 * @param rateCoordinatesByDate la courbe des taux
	 * @param plannedBillingDates les dates de flux plannifiées 
	 * */
   public static List<DateTime>  getAccurateBillingDates(Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate ,List<DateTime> plannedBillingDates){
	   List<DateTime> 	accurateBillingDates = new ArrayList<DateTime>();
       List<RateCoordinate> rateCoordinates;
       for(DateTime date :plannedBillingDates){
       	DateTime accurateDay = date;																							
       	rateCoordinates = rateCoordinatesByDate.get(date);
       	//Si une date de facturation n'a pas d'entree ds la courbe des taux, alors prendre la première date la précédent qui a une entrée ds la courbe
       	while(rateCoordinates==null){
       		accurateDay = accurateDay.minusDays(1);
       		rateCoordinates = rateCoordinatesByDate.get(accurateDay); 
       	}
       	accurateBillingDates.add(accurateDay);
       }  
	   return accurateBillingDates;	   
   }
   
   
   /**
    * retourne la liste des RateCoordinate associé à pricingDate
    * */
   public static List<RateCoordinate> getAccurateRateCoordinates(Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate,DateTime pricingDate){
		List<RateCoordinate> rateCoordinates = rateCoordinatesByDate.get(pricingDate);
       	DateTime accurateDay = pricingDate;																							
       	rateCoordinates = rateCoordinatesByDate.get(pricingDate);
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
   public static Double getRatioAlpha(Bond bond, DateTime pricingDate){
	  DateTime dateProchainCoupon = getDateProchainCoupon(pricingDate,bond);
	  Double diffDays = new Double(Days.daysBetween(pricingDate, dateProchainCoupon).getDays());
	  Double daysPerYear = 360.0;
	  if(bond.getBondMaturity()>1){
		  daysPerYear = 365.0;
	  }
	  return diffDays/daysPerYear;
   }
   

	/**
	 * calcule le flux...
	 * */
	public static double getFlux(double coupon, double rate, double puissance) {
		double denominateur = Math.pow(1+rate, puissance); 
		return coupon/denominateur;
	}
			
	//calculate the flux
	public  double getLastFlux(double coupon, double nominal, double rate,double puissance) {	
		double denominateur = Math.pow(1+rate, puissance); 
		return (coupon+nominal)/denominateur;
	}
 
	/**
    * retourne la date du prochain coupon associée à <b>bond</b> par rappord a
    * la date de pricing <b>pricingDate</d> 
    * */
   public static DateTime getDateProchainCoupon(DateTime pricingDate,Bond bond){
	   DateTime dateProchainCoupon = bond.getEmissionDate();
	   while(pricingDate.compareTo(dateProchainCoupon)>0){
		   dateProchainCoupon = dateProchainCoupon.plusMonths(bond.getPeriodicity());
	   }
	   return dateProchainCoupon;
   }
   

}
