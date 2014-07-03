package com.diakiese.pricer.o3utils;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
//import org.acensi.bean.RateCurveWrapper;

import au.com.bytecode.opencsv.CSVReader;

import com.diakiese.pricer.o1bean.RateCurveEntry;

public class RateCurveBuilderImpl implements IRateCurveBuilder {
		
	final static Logger log = Logger.getLogger(RateCurveBuilderImpl.class);	
	final static String fileLocation = "C:/taux2.csv";										
	public  RateCurveWrapper createRateCurve(String filePath) throws IOException { 
		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();
		Map<String,List<Double>> rateCurve = new LinkedHashMap<String,List<Double>>();
		RateCurveEntry rateCurveEntry = new RateCurveEntry();
		CSVReader reader = null;
		try{						
			reader = new CSVReader(new FileReader(filePath),';'); 
			List<String[]> records = reader.readAll();
			List<String[]> datas = records.subList(1, records.size());
			log.info("NBRE RECORDS: " + records.size());
			log.info("NBRE DATAS: " + datas.size());
			for(String[] record:datas){
				rateCurveEntry = buildRateEntry(record);
				rateCurve.put(rateCurveEntry.getKey(), rateCurveEntry.getValue());
				rateCurveWrapper.setRateCurve(rateCurve);
			}
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
			reader.close();
		}		
		return rateCurveWrapper;  
	}
 

	public RateCurveEntry  buildRateEntry(String[] tab){
		RateCurveEntry rateCurveEntry = new RateCurveEntry();
		rateCurveEntry.setKey(tab[0]);
		List<Double> values = new LinkedList<Double>(); 
		Double d = new Double(0.0);
		for(int i=1;i<tab.length;i++){
			String val = tab[i];
			try{
			d = new Double(val) ;
			values.add(d); 
			}catch(Exception e){
				values.add(null);
			} 
		}
		rateCurveEntry.setValue(values);
		return rateCurveEntry;
		
	}
	


	public static void main(String[] args) throws IOException {
		RateCurveBuilderImpl builder = new RateCurveBuilderImpl();  
		RateCurveWrapper rateCurveWrapper = builder.createRateCurve(fileLocation);  
		Set<String> dates = rateCurveWrapper.getRateCurve().keySet(); 
		int i = 1; 																			
		for(String s:dates){ 
			log.info("Date "+i+ ": " + s + " TAUX: " + rateCurveWrapper.getRateCurve().get(s).get(0));  
			i++ ;
		}
	}
}
