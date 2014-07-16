package com.diakiese.pricer.o3utils;

import java.io.FileReader;
import java.io.IOException;
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
		
	final static String fileLocation = "C:/taux2.csv";										
	
//	public  RateCurveWrapper createRateCurve1(String filePath) throws IOException { 
//		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();
//		Map<String,List<Double>> rateCurve = new LinkedHashMap<String,List<Double>>();
//		RateCurveEntry rateCurveEntry = new RateCurveEntry();
//		CSVReader reader = null;
//		try{						
//			reader = new CSVReader(new FileReader(filePath),';'); 
//			List<String[]> records = reader.readAll();
//			List<String[]> datas = records.subList(1, records.size());
//			log.info("NBRE RECORDS: " + records.size());
//			log.info("NBRE DATAS: " + datas.size());
//			for(String[] record:datas){
//				rateCurveEntry = buildRateEntry(record);
//				rateCurve.put(rateCurveEntry.getKey(), rateCurveEntry.getValue());
//			}
//			rateCurveWrapper.setRateCurve(rateCurve);
//		}catch(Exception e){
//			e.printStackTrace();		
//		}finally{
//			reader.close();
//		}		
//		return rateCurveWrapper;  
//	}
 

//	public  RateCurveWrapper createRateCurve() throws IOException {												 
//		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();							
//		Map<DateTime,List<Double>> rateCurve = new LinkedHashMap<DateTime,List<Double>>();
//		Map<String,List<Double>> rateCurveString = new LinkedHashMap<String,List<Double>>();
//		RateCurveEntry rateCurveEntry = new RateCurveEntry();
//		
//		CSVReader reader = null;
//		try{						
//			reader = new CSVReader(new FileReader(fileLocation),';'); 
//			List<String[]> records = reader.readAll();
//			List<String[]> datas = records.subList(1, records.size());
//			log.info("NBRE RECORDS: " + records.size());
//			log.info("NBRE DATAS: " + datas.size());
//			int i=1;
//			for(String[] record:datas){
//				rateCurveEntry = buildRateEntry(record);
//				rateCurveEntry.initDate();
//				rateCurve.put(rateCurveEntry.getDate(), rateCurveEntry.getValue());
//				rateCurveString.put(rateCurveEntry.getKey(),rateCurveEntry.getValue());
////				log.info("DATE " +i+" - " + rateCurveEntry.getDate());	
//				i++;
//			}
//			rateCurveWrapper.setRateCurveDateTimed(rateCurve);
//			rateCurveWrapper.setRateCurveString(rateCurveString);    
//		}catch(Exception e){
//			e.printStackTrace();		
//		}finally{
//			reader.close();
//		}		
//		return rateCurveWrapper;  
//	}


	public  com.diakiese.pricer.o1bean.RateCurveWrapper createRateCurve2() throws IOException {												 
		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();							
//		Map<DateTime,List<Double>> rateCurve = new LinkedHashMap<DateTime,List<Double>>();  
		Map<String,List<RateCoordinate>> rateCurve = new LinkedHashMap<String,List<RateCoordinate>>();
		Map<DateTime,List<RateCoordinate>> rateCurve2 = new LinkedHashMap<DateTime,List<RateCoordinate>>();
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
//				rateCurveEntry = buildRateEntry(data,periodZones);		
				rateCurveEntry.initDate();
//				rateCurve.put(rateCurveEntry.getKey(), rateCurveEntry.getRateCoordinates());
				rateCurve2.put(rateCurveEntry.getDate(), rateCurveEntry.getRateCoordinates());
//				log.info("ENTRY: " + i +"\n");														  
//				for(int j=0;j<rateCurveEntry.getRateCoordinates().size();j++){
//					log.info("Element:" +(j+1)+" - " + "\tDate :" + rateCurveEntry.getDate() + "\tPeriodZone :" + rateCurveEntry.getRateCoordinates().get(j).getPeriodZone() + "\tPeriod Year: " +
//				            rateCurveEntry.getRateCoordinates().get(j).getPeriodYear() + "\tRate: " + rateCurveEntry.getRateCoordinates().get(j).getRate());	
//				}
//				log.info("\n");  
				i++;
			}		
			rateCurveWrapper.setRateCoordinatesByDate(rateCurve2);    
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
			reader.close();
		}		
		return rateCurveWrapper;  
	}
	

//	public RateCurveEntry buildRateEntry(String[] tabKeys,String[] tabPeriodZones){
//		RateCurveEntry rateCurveEntry = new RateCurveEntry();								 
//		rateCurveEntry.setKey(tabKeys[0]);				
//		List<RateCoordinate> rateCoordinates = new ArrayList<RateCoordinate>(); 
//		Double rate = new Double(0.0);
//		String periodZone = "";
//		for(int i=1;i<tabKeys.length-1;i++){
//			periodZone = tabPeriodZones[i];
//			try{
//			rate = new Double(tabKeys[i]);
//			rateCoordinates.add(new RateCoordinate(periodZone,rate));
//			}catch(Exception e){
//			log.info("PeriodZone: " + periodZone);
//			rateCoordinates.add(new RateCoordinate(periodZone));
//			}
//		}						
//		log.info("\n");
//		rateCurveEntry.setRateCoordinates(rateCoordinates);
//		return rateCurveEntry;
//	}
	

	public static void main(String[] args) throws IOException {
		RateCurveBuilderImpl builder = new RateCurveBuilderImpl();  
		RateCurveWrapper rateCurveWrapper = builder.createRateCurve2();  
//		Set<String> dates = rateCurveWrapper.getRateCurve().keySet(); 
		int i = 1;	 																			
//		for(String s:dates){		 
//			log.info("\t\tDate "+ i + ": " + s + "\tTAUX_1: " + rateCurveWrapper.getRateCurve().get(s).get(0)+ "\tTAUX_2: " + rateCurveWrapper.getRateCurve().get(s).get(1));  
//			i++ ;
//		} 
//		DateTime dateEmission = new DateTime(2011,5,18,0,0,0);		
//		DateTime pricingDate = new DateTime(2013,10,4,0,0,0); 								
//		Set<DateTime> billingDates = BondPricerUtils.getBillingDates(6,6,dateEmission);
//		BondPricerUtils.displayDates(billingDates);
//		BondPricerUtils.displayDates(BondPricerUtils.getFuturesBillingDates_toRefactor(6, 6, dateEmission, pricingDate));
	}
			
}
