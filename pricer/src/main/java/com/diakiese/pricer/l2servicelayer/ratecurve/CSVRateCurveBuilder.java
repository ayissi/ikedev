package com.diakiese.pricer.l2servicelayer.ratecurve;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVReader;

import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.RateCurveEntry;
import com.diakiese.pricer.o1bean.RateCurveWrapper;

public abstract class CSVRateCurveBuilder implements IRateCurveBuilder {

	final static Logger log = Logger.getLogger(CSVRateCurveBuilder.class);
	
	/**
	 * retourne une <b>RateCurveEntry</b>
	 * */
	public RateCurveEntry buildRateEntry(String[] tabKeys,List<String> tabPeriodZones){
		RateCurveEntry rateCurveEntry = new RateCurveEntry();														 
		rateCurveEntry.setKey(tabKeys[0]);				
		List<RateCoordinate> rateCoordinates = new ArrayList<RateCoordinate>(); 
		Double rate = new Double(0.0);							
		String periodZone = "";
		Double periodYear = 0.0;	
		for(int i=1;i<tabKeys.length;i++){
			periodZone = tabPeriodZones.get(i); 
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

	public  RateCurveWrapper createRateCurve(String rateFile) throws IOException {												 
		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();							
		Map<DateTime,List<RateCoordinate>> rateCurve = new LinkedHashMap<DateTime,List<RateCoordinate>>();
		RateCurveEntry rateCurveEntry = new RateCurveEntry();	
		CSVReader reader = null;
		try{																									
			reader = new CSVReader(new FileReader(rateFile),';');		 
//			reader = new CSVReader(new FileReader(rateFile),',');
			List<String[]> records = reader.readAll();
			List<String[]> datas = records.subList(1, records.size());
			String[]  periodZones = records.get(0);	
			ArrayList<String> periodZonesList = new ArrayList<String>();
			for(int i=0;i<periodZones.length;i++){
				if(!periodZones[i].isEmpty()){ 
				 periodZonesList.add(periodZones[i]); 	
				}		
			}		
//			log.info("NBRE RECORDS: " + records.size());
//			log.info("NBRE DATAS: " + datas.size());						  
			for(String[] data:datas){
				rateCurveEntry = buildRateEntry(data,periodZonesList); 		
				rateCurveEntry.initDate();
				rateCurve.put(rateCurveEntry.getDate(), rateCurveEntry.getRateCoordinates());
			}		
			rateCurveWrapper.setRateCoordinatesByDate(rateCurve);
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
			reader.close();
		}		
		return rateCurveWrapper;  
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
