package com.diakiese.pricer.l2servicelayer.ratecurve;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
	
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
		
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.RateCurveWrapper;
																				
public class ExcelRateCurveBuilderImpl implements IRateCurveBuilder {
			
	final static String rateCurveFile ="C:/taux2.xls";							  
		
	@Override
	public RateCurveWrapper createRateCurve(String fileloc) throws IOException {
		RateCurveWrapper rateCurveWrapper = new RateCurveWrapper();
		Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate = new LinkedHashMap<DateTime,List<RateCoordinate>>();
		try{
			FileInputStream file = new FileInputStream(fileloc); 
			HSSFWorkbook wb = new HSSFWorkbook(file);
		    HSSFSheet feuille = wb.getSheetAt(0);
		    	
		    // Créer un Itérateur sur la feuille
            Iterator<Row> rowIterator = feuille.iterator();
            //on saute la première ligne
            rowIterator.next();									
            while(rowIterator.hasNext()){
            	Row row = rowIterator.next();		
            	DateTime date = null;
            	List<RateCoordinate> rateCoordinates = new ArrayList<RateCoordinate>();
            	Double datePeriod = 0.0d;  
            
                // Lire les colonnes de chaque ligne
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()){ 
                	RateCoordinate rateCoordinate = new RateCoordinate();           
                    Cell cell = cellIterator.next();                    
                    switch(cell.getCellType()){
                        case Cell.CELL_TYPE_NUMERIC:      
                        	date =  new DateTime(cell.getDateCellValue().getTime()); 
                            break;
                        case Cell.CELL_TYPE_STRING:
                        	if (cell.getStringCellValue().trim().startsWith("0"))
                        	{
                        		double rate = Double.parseDouble(cell.getStringCellValue());
                        		datePeriod += 0.25;
                        		rateCoordinate.setRate(rate);
                        		rateCoordinate.setPeriodYear(datePeriod);
                        		rateCoordinates.add(rateCoordinate);
                        	}
                            break;
                    }
                }
                if(rateCoordinates.size()>0){
                   rateCoordinatesByDate.put(date, rateCoordinates);	
                }
             }				
            file.close(); 
		  }catch(Exception e){
			e.printStackTrace();
		  }
		rateCurveWrapper.setRateCoordinatesByDate(rateCoordinatesByDate);
		return rateCurveWrapper;
      }
		 	

	@Override
	public String getFile(){
		return rateCurveFile;
	}

}
