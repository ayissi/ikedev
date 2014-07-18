package com.diakiese.pricer.l2servicelayer;

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

public class TestRateCurveBuilderImpl extends RateCurveBuilder implements IRateCurveBuilder {
		
	final static Logger log = Logger.getLogger(TestRateCurveBuilderImpl.class);
	final static String rateFile = "C:/dev/taux6.csv";	
		
	@Override 
	public  RateCurveWrapper createRateCurve() throws IOException {												 
		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();							
		Map<DateTime,List<RateCoordinate>> rateCurve = new LinkedHashMap<DateTime,List<RateCoordinate>>();
		RateCurveEntry rateCurveEntry = new RateCurveEntry();	
		CSVReader reader = null;
		try{						
			reader = new CSVReader(new FileReader(rateFile),';'); 
			List<String[]> records = reader.readAll();
			List<String[]> datas = records.subList(1, records.size());
			String[]  periodZones = records.get(0);	
		
			for(String[] data:datas){
				rateCurveEntry = buildRateEntry(data,periodZones);		
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
	
}
