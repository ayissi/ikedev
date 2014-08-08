package com.diakiese.pricer.l2servicelayer.interpolation;

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
			log.info("FLOOR PERIOD YEAR: " +floorElt.getPeriodYear()+ "\tCEIL PERIOD YEAR: " + ceilElt.getPeriodYear() +"\tMID PERIOD YEAR: " + midPeriodYear +"\tFLOOR RATE" + floorElt.getRate() + "\tCEIL RATE " + ceilElt.getRate());
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
			Double midPeriodYear){
		Double d = 0.0;   
		RateCoordinate floorCoordinate = getFloorRateCoordinate(rateCoordinates, midPeriodYear);
		RateCoordinate ceilCoordinate = getCeilRateCoordinate(rateCoordinates, midPeriodYear);
		if(!(floorCoordinate==null ||ceilCoordinate==null)){
//		return d;														  	
//		}else{
			d= linearInterpolation(floorCoordinate,ceilCoordinate,midPeriodYear);	
		}
		return d ;
	}
	

	/**
	 * retourne un tableau qui contient deux élements, 
	 * l'element RateCoordinate dont l'abcisse précède <b>alpha</b> et l'élement RateCoordinate
	 * dont l'abcisse suit alpha.  
	 * */
	private  RateCoordinate[] getRates(double alpha, List<RateCoordinate> rateCurve){
		RateCoordinate[] result = new RateCoordinate[2];
		RateCoordinate before = null;
		for(RateCoordinate rate : rateCurve) {
			if(rate.getPeriodYear() > alpha) {
				result[1] = rate;
				result[0] = before;
				break;
			} 
			before = rate;
		}
		if(rateCurve != null && rateCurve.size() > 0 && result[0] == null) {
			result[0] = rateCurve.get(rateCurve.size()-1);
		}
		return result;
	}
	
	
	private double interpolate(RateCoordinate[] rates, double alpha){
		double result =0;
		RateCoordinate before = rates[0];
		RateCoordinate after = rates[1];
		try{
			if(before == null && after != null) { 
				result = after.getRate();
			}else if(after == null && before != null) {
				result = after.getRate();   											
			}else if(before != null && after != null){ 
				result = before.getRate() + (alpha - before.getPeriodYear()) * (after.getRate() - before.getRate()) / (after.getPeriodYear() - before.getPeriodYear());
			}
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
		return result;
	}

		
	@Override
	public Double interpolate2(List<RateCoordinate> rateCoordinates,
			Double midPeriodYear){        					      
		RateCoordinate[] rates = getRates(midPeriodYear, rateCoordinates);								
		return interpolate(rates,midPeriodYear);  
	}
	
}
