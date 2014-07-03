package com.diakiese.pricer.o3utils;

import java.io.File;
import java.io.FileInputStream;

public class Test {
			
	public  RateCurveWrapper createRateCurve(String filePath) {
		try{
			FileInputStream file = new FileInputStream(new File(filePath));
	        //Create Workbook instance holding reference to .xlsx file
//	        XSSFWorkbook xWorkbook = new XSSFWorkbook(file);
//	        HSSFWorkbook hWorkbook = new HSSFWorkbook(file);
//	        xWorkbook.toString();
//	        int numberOfNames = hWorkbook.getNumberOfNames();   
		}catch(Exception e){
			e.printStackTrace(); 
		}finally{
				
		}
		return null;
	}

	
	public static void main(String[] args){
		RateCurveBuilderImpl builder = new RateCurveBuilderImpl();
	}
		
}
