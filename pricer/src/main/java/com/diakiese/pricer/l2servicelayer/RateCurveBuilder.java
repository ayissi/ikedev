package com.diakiese.pricer.l2servicelayer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.RateCurveEntry;

public abstract class RateCurveBuilder implements IRateCurveBuilder {

	final static Logger log = Logger.getLogger(RateCurveBuilder.class);
	
	/**
	 * retourne une <b>RateCurveEntry</b>
	 * */
	public RateCurveEntry buildRateEntry(String[] tabKeys,String[] tabPeriodZones){
		RateCurveEntry rateCurveEntry = new RateCurveEntry();														 
		rateCurveEntry.setKey(tabKeys[0]);				
		List<RateCoordinate> rateCoordinates = new ArrayList<RateCoordinate>(); 
		Double rate = new Double(0.0);							
		String periodZone = "";
		Double periodYear = 0.0;	
		for(int i=1;i<tabKeys.length;i++){
			periodZone = tabPeriodZones[i];
			periodYear = buildPeriodYear(periodZone);
			if(periodZone.length()>0){  
				try{
					rate = new Double(tabKeys[i]);
					rateCoordinates.add(new RateCoordinate(periodYear,rate));
					}catch(Exception e){
						rateCoordinates.add(null); 
					}	
			}

		}						
//		log.info("\n");
		rateCurveEntry.setRateCoordinates(rateCoordinates);
		return rateCurveEntry;
	}
	

	public Double buildPeriodYear(String periodZone){ 
		Double d = new Double(0.0);	
		try{
			if(!periodZone.isEmpty()){
				String[] tabToMov_ZC = periodZone.split("ZC");
				String[] tabToMov_YR = tabToMov_ZC[1].split("YR");	
				d = new Double(tabToMov_YR[0])*0.01 ;	
			}
		}catch(Exception e){
			log.info("IS THIS A PERIOD_ZONE ? " + periodZone);
			e.printStackTrace();
		}		
		return d ; 
	}

}
