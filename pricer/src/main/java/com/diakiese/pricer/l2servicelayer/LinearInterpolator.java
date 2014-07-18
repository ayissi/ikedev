package com.diakiese.pricer.l2servicelayer;

import java.util.List;

import org.apache.log4j.Logger;

import com.diakiese.pricer.o1bean.RateCoordinate;

public class LinearInterpolator implements Iinterpolator {
			
	final static Logger log = Logger.getLogger(LinearInterpolator.class);  
	
	@Override
	public Double interpolate(RateCoordinate floorElt, RateCoordinate ceilElt,
			Double midPeriodYear) {
		Double d = new Double(0.0);		
		if(!ceilElt.getPeriodYear().equals(floorElt.getPeriodYear())&&(floorElt!=null)&&(ceilElt!=null)&&(floorElt.getRate()!=null)&&(ceilElt.getRate()!=null)){
			log.info("FLOOR PERIOD YEAR: " +floorElt.getPeriodYear()+ "\tCEIL PERIOD YEAR: " + ceilElt.getPeriodYear() +"\tMID PERIOD YEAR: " + midPeriodYear +"\tFLOOR RATE" + floorElt.getRate() + "\tCEIL RATE " + ceilElt.getRate());
			Double quotient = ceilElt.getPeriodYear()-floorElt.getPeriodYear();  
			Double d1 = ((ceilElt.getPeriodYear()-midPeriodYear)/(quotient))*floorElt.getRate();
			Double d2 = ((midPeriodYear-floorElt.getPeriodYear())/(quotient))*ceilElt.getRate();	
			d = d1+d2;
		}		
		return d; 
	}

	/**
	 * retourne le taux interpolé d'un RateCoordinate X 
	 * @param floorElt le RateCoordinate précedent X
	 * @param ceilElt  le RateCoordinate suivant X
	 * @param midPeriodYear la periodYear de X, borné par floorElt.periodYear et ceilElt.periodYear 
	 * */						  
	public  Double linearInterpolation(RateCoordinate floorElt, RateCoordinate ceilElt, Double midPeriodYear){
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
	   public RateCoordinate getFloorRateCoordinate(List<RateCoordinate> rateCoordinates, Double _periodYear){
		RateCoordinate rateCoordinate = new RateCoordinate();
		Double periodYearCent = _periodYear * 100.0;							
		Integer _period = periodYearCent.intValue();
		Integer position = _period/25;																										
		if(rateCoordinates==null ||(position>rateCoordinates.size())){
			return null;  
		}else if(position==rateCoordinates.size()){
			rateCoordinate = rateCoordinates.get(rateCoordinates.size()-1) ;
		}else if(position==0){
			rateCoordinate.setPeriodYear(new Double(0.0));
			rateCoordinate.setRate(new Double(0.0));      
		}else if(position>=1){  
			rateCoordinate = rateCoordinates.get(position-1) ;
		}		
		return rateCoordinate ; 
	}
	

	/**
	 * retourne le RateCoordinate X tel que X.periodYear suit _periodYear
	 * @param rateCoordinates la liste des (année,taux)
	 * @param _periodYear l'année dont on cherche le taux
	 * */
	   public RateCoordinate getCeilRateCoordinate(List<RateCoordinate> rateCoordinates, Double _periodYear){
		RateCoordinate rateCoordinate = new RateCoordinate();
		Double periodYearCent = _periodYear * 100.0;
		Integer _period = periodYearCent.intValue();
		Integer position = _period/25;														
		if((rateCoordinates==null)||(position>rateCoordinates.size())){
			return null;																		
		}else if(position==rateCoordinates.size()){ 
			rateCoordinate = rateCoordinates.get(rateCoordinates.size()-1) ;
		}else if(position==0){
			rateCoordinate = rateCoordinates.get(0) ;
		}else if(position>=1){  
			rateCoordinate = rateCoordinates.get(position);
		}					
		return rateCoordinate; 
	  }

	
	@Override
	public Double interpolate(List<RateCoordinate> rateCoordinates,
			Double midPeriodYear) {
		RateCoordinate floorCoordinate = getFloorRateCoordinate(rateCoordinates, midPeriodYear);
		RateCoordinate ceilCoordinate = getCeilRateCoordinate(rateCoordinates, midPeriodYear);
		if(floorCoordinate==null ||ceilCoordinate==null){
		return null; 	
		}else{
			return linearInterpolation(floorCoordinate,ceilCoordinate,midPeriodYear);	
		}
		
	}
	
}
