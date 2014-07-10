package com.diakiese.pricer.l2servicelayer;

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

public class RateCurveBuilderImpl implements IRateCurveBuilder {

	final static Logger log = Logger.getLogger(RateCurveBuilderImpl.class);
	final static String rateFile = "C:/taux2.csv";										




	public  RateCurveWrapper createRateCurve(String fileLocation) throws IOException {												 
		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();							
		Map<DateTime,List<RateCoordinate>> rateCurve = new LinkedHashMap<DateTime,List<RateCoordinate>>();
		RateCurveEntry rateCurveEntry = new RateCurveEntry();	
		CSVReader reader = null;
		try{						
			reader = new CSVReader(new FileReader(fileLocation),';'); 
			List<String[]> records = reader.readAll();
			List<String[]> datas = records.subList(1, records.size());
			String[]  periodZones = records.get(0);															
			log.info("NBRE RECORDS: " + records.size());
			log.info("NBRE DATAS: " + datas.size());
			int i=1;  
			for(String[] data:datas){
				rateCurveEntry = buildRateEntry(data,periodZones);		
				rateCurveEntry.initDate();
				rateCurve.put(rateCurveEntry.getDate(), rateCurveEntry.getRateCoordinates());
//				log.info("ENTRY: " + i +"\n");																  
//				for(int j=0;j<rateCurveEntry.getRateCoordinates().size();j++){
//					log.info("Element:" +(j+1)+" - " + "\tDate :" + rateCurveEntry.getDate() + "\tPeriodZone :" + rateCurveEntry.getRateCoordinates().get(j).getPeriodZone() + "\tPeriod Year: " +
//				            rateCurveEntry.getRateCoordinates().get(j).getPeriodYear() + "\tRate: " + rateCurveEntry.getRateCoordinates().get(j).getRate());	
//				}
//				log.info("\n");		  
				i++;
			}		
			rateCurveWrapper.setRateCurveDateTimed(rateCurve);
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
			reader.close();
		}		
		return rateCurveWrapper;  
	}
	


	/**
	 * retourne une <b>RateCurveEntry</b>
	 * 
	 * */
	public RateCurveEntry buildRateEntry(String[] tabKeys,String[] tabPeriodZones){
		RateCurveEntry rateCurveEntry = new RateCurveEntry();														 
		rateCurveEntry.setKey(tabKeys[0]);				
		List<RateCoordinate> rateCoordinates = new ArrayList<RateCoordinate>(); 
		Double rate = new Double(0.0);
		String periodZone = "";
		for(int i=1;i<tabKeys.length-1;i++){
			periodZone = tabPeriodZones[i];
			try{
			rate = new Double(tabKeys[i]);
			rateCoordinates.add(new RateCoordinate(periodZone,rate));
			}catch(Exception e){
			log.info("PeriodZone: " + periodZone);
			if(periodZone.length()>0){ 
				rateCoordinates.add(new RateCoordinate(periodZone));
			  }
			}
		}						
		log.info("\n");
		rateCurveEntry.setRateCoordinates(rateCoordinates);
		return rateCurveEntry;
	}
	
 
}
