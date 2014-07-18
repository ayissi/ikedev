package com.diakiese.pricer.o3test;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.diakiese.pricer.l2servicelayer.CSVRateCurveBuilderImpl;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o3utils.BondPricerUtils;
import com.diakiese.pricer.o1bean.RateCurveWrapper;

			
public class BondPriceUtilsTest {

	List<RateCoordinate> rateCoordinates;
	RateCoordinate floorRateCoordinate ;
	RateCoordinate ceilRateCoordinate ;
	Double periodYear;
	BondPricerUtils bondUtils;
	final static String testFile = "C:/taux6.csv";	
		
//	@Before
//	public void setUp() throws IOException{
//		DateTime entryDate = new DateTime(new DateTime(1993,1,4,0,0,0));
//		CSVRateCurveBuilderImpl builder = new CSVRateCurveBuilderImpl();  
//		RateCurveWrapper rateCurveWrapper = builder.createRateCurve(); 
//		Map<DateTime,List<RateCoordinate>> rateCurve = rateCurveWrapper.getRateCoordinatesByDate();  
//		rateCoordinates = rateCurve.get(entryDate);
//		bondUtils = new BondPricerUtils();
//	}
//	
//
//	@SuppressWarnings("static-access")
//	@Test
//	public void testGetFloorRateCoordinate(){
//		//Given
//		 periodYear = 1.65 ;
//		 Double expectedFloorPeriodYear = new Double(1.5);
//		//When
//		 floorRateCoordinate = bondUtils.getFloorRateCoordinate(rateCoordinates,periodYear); 
//		//Then
//		assertThat(floorRateCoordinate.getPeriodYear()).isEqualTo(expectedFloorPeriodYear); 
//	}
//	
//
//
//	@SuppressWarnings("static-access")
//	@Test
//	public void testGetCeilRateCoordinate(){
//		//Given										
//		 periodYear = 0.95;	
//		 Double expectedCeilPeriodYear = new Double(1.0); 
//		 //When
//		 ceilRateCoordinate = bondUtils.getCeilRateCoordinate(rateCoordinates,periodYear);
//		 //Then
//		 assertThat(ceilRateCoordinate.getPeriodYear()).isEqualTo(expectedCeilPeriodYear);   
//	}
//	
//
//	
//	@SuppressWarnings("static-access")
//	@Test 
//	public void testLinearInterpolation(){
//		//Given
//		Double midPeriodYear = new Double(0.6);
//		Double expectedInterpolVal = new Double(60.0);
//		floorRateCoordinate = bondUtils.getFloorRateCoordinate(rateCoordinates,midPeriodYear);
//		ceilRateCoordinate = bondUtils.getCeilRateCoordinate(rateCoordinates,midPeriodYear); 
//			
//		//When
//		Double interpolatedVal = bondUtils.linearInterpolation(floorRateCoordinate, ceilRateCoordinate, midPeriodYear);
//
//		//Then
//		assertThat(interpolatedVal).isEqualTo(expectedInterpolVal);
//	}
//	
//			
//	@Test
//	public void testGetNombreDeFluxRestant(){
//		
//		//GIVEN 
//		Double d1 = new Double(0.0);
//		Double d2 = new Double(1);
//		//WHEN 
//		
//		
//		//THEN
//		assertThat(d2).isEqualTo(d1);
//	}
//	
//
//	@Test
//	public void testGetRateByBillingDate(){
//		
//		//GIVEN 
//		Double d1 = new Double(0.0);
//		Double d2 = new Double(1);
//		//WHEN 
//		
//		
//		//THEN
//		assertThat(d2).isEqualTo(d1);
//	}
//	
//
//	@Test
//	public void testGetAccurateRateCoordinates(){ 	
//		//GIVEN 
//		Double d1 = new Double(0.0);
//		Double d2 = new Double(1);
//		//WHEN 
//		
//		
//		//THEN
//		assertThat(d2).isEqualTo(d1);
//	}
//	
//	@Test
//	public void testGetRatioAlpha(){
//		
//		//GIVEN 
//		Double d1 = new Double(0.0);
//		Double d2 = new Double(1);
//		//WHEN 
//		
//		
//		//THEN
//		assertThat(d2).isEqualTo(d1);
//	}
//	
//	
//	@Test
//	public void testGetFlux(){ 	
//		//GIVEN 
//		Double d1 = new Double(0.0);
//		Double d2 = new Double(1);
//		//WHEN 
//		
//		
//		//THEN
//		assertThat(d2).isEqualTo(d1);
//	}

	
	@After
	public void tearDown(){
		rateCoordinates =null;
		floorRateCoordinate =null ;	
	}
	
}
