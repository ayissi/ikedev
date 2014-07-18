package com.diakiese.pricer.l2servicelayer;
import static org.fest.assertions.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
																



import com.diakiese.pricer.o1bean.RateCoordinate;

public class LinearInterpolatorTest {
	final static Logger log = Logger.getLogger(LinearInterpolatorTest.class);	
	private TestRateCurveBuilderImpl testRateCurveBuilderImpl=null;
	
	Map<DateTime, List<RateCoordinate>> rateCurvesByDate ;
	DateTime pricingDate ;
	Double periodYear;	
	private LinearInterpolator testeur = null ;							

	@Before
	public void setUp() throws IOException{ 
	    log.info("SETUP  DOWNNNN");
		if((testeur==null)&&(testRateCurveBuilderImpl==null)&&(rateCurvesByDate==null)){
			this.testRateCurveBuilderImpl = new TestRateCurveBuilderImpl();             
			this.rateCurvesByDate = testRateCurveBuilderImpl.createRateCurve().getRateCoordinatesByDate(); 
			this.testeur = new LinearInterpolator();
		}

	}


	  /* TESTING getFloorRateCoordinate */ 

	//@Test
	public void testGetCeilRateCoordinate_pos_sup_1(){ 
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(1.82);
	
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(2.0));
		expected.setRate(new Double(200)); 
		
		//WHEN
		RateCoordinate result = this.testeur.getCeilRateCoordinate(rateCoordinates, periodYear);
		
		//THEN
		assertThat(result).isEqualTo(expected);	
	}
	
	//@Test
	public void testGetCeilRateCoordinate_pos_zero(){
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(0.12);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(0.25));
		expected.setRate(new Double(25)); 
		//WHEN
		RateCoordinate result = this.testeur.getCeilRateCoordinate(rateCoordinates, periodYear);
		
		//THEN	
		assertThat(result).isEqualTo(expected);
	}
	

	//@Test
	public void testGetCeilRateCoordinate_pos_sup_to_size(){
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(2.75);

		//WHEN
		RateCoordinate result = this.testeur.getCeilRateCoordinate(rateCoordinates, periodYear);	
		
		//THEN	
		assertThat(result).isEqualTo(null);
	}

	  /* TESTING getFloorRateCoordinate */
	
	//@Test
	public void testGetCeilRateCoordinate_rateCoordinates_equals_null(){
		//GIVEN
		pricingDate = new DateTime(1993,1,2,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(2.75);
		//WHEN
		RateCoordinate result = this.testeur.getCeilRateCoordinate(rateCoordinates, periodYear);	
			
		//THEN
		assertThat(result).isEqualTo(null);
	}
	

//	//@Test
	public void testGetCeilRateCoordinate_rateCoordinates_equals_size(){
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(2.12);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(2.0));
		expected.setRate(new Double(200)); 
		//WHEN
		RateCoordinate result = this.testeur.getCeilRateCoordinate(rateCoordinates, periodYear);

		//THEN	
		assertThat(result.getPeriodYear()).isEqualTo(expected.getPeriodYear());
	}

    /* TESTING getFloorRateCoordinate */
	//@Test
	public void testGetFloorRateCoordinate_pos_sup_un(){
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(1.12);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(1.0));
		expected.setRate(new Double(100)); 
		
		//WHEN
		RateCoordinate result = this.testeur.getFloorRateCoordinate(rateCoordinates, periodYear);
		
		//THEN	
		assertThat(result).isEqualTo(expected);
	}
	

	//@Test
	public void testGetFloorRateCoordinate_pos_zero(){
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(0.12);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(0.0));
		expected.setRate(new Double(0.0)); 
	
		//WHEN
		RateCoordinate result = this.testeur.getFloorRateCoordinate(rateCoordinates, periodYear);	
		
		//THEN	
		assertThat(result).isEqualTo(expected);
	}
	
	//@Test
	public void testGetFloorRateCoordinate_pos_sup_to_size(){
		//GIVEN
		pricingDate = new DateTime(1993,1,4,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(3.28);

		//WHEN
		RateCoordinate result = this.testeur.getFloorRateCoordinate(rateCoordinates, periodYear);
		
		//THEN
		assertThat(result).isEqualTo(null);
	}
	

	//@Test
	public void testGetFloorRateCoordinate_rateCoordinates_equals_null(){
		//GIVEN
		pricingDate = new DateTime(1993,1,2,0,0,0);
		List<RateCoordinate> rateCoordinates = rateCurvesByDate.get(pricingDate);
		periodYear = new Double(3.28);
		//WHEN
		RateCoordinate result = this.testeur.getFloorRateCoordinate(rateCoordinates, periodYear);
		
		//THEN
		assertThat(result).isEqualTo(null);
	}
	
	/* TESTING linearInterpolation */
	
	@Test 
	public void testLinearInterpolation(){
		//GIVEN
		RateCoordinate floor = new RateCoordinate();
		floor.setPeriodYear(new Double(0.75));
		floor.setRate(new Double(75));
		
		RateCoordinate ceil = new RateCoordinate();
		ceil.setPeriodYear(new Double(1.25));
		ceil.setRate(new Double(125));
		
		periodYear = new Double(1.0);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(1.0));
		expected.setRate(new Double(100));
		
		//WHEN
		Double result = this.testeur.linearInterpolation(floor, ceil,periodYear);
		
		//THEN
		assertThat(result).isEqualTo(expected.getRate());
	}

	
	/* TESTING  interpolate */
	@Test 
	public void testinterpolation_1(){
		//GIVEN
		RateCoordinate floor = new RateCoordinate();
		floor.setPeriodYear(new Double(0.75));
		floor.setRate(new Double(75));
		
		RateCoordinate ceil = new RateCoordinate();
		ceil.setPeriodYear(new Double(1.25));
		ceil.setRate(new Double(125));
		
		periodYear = new Double(1.0);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(1.0));
		expected.setRate(new Double(100));
		
		//WHEN
		Double result = this.testeur.interpolate(floor, ceil,periodYear);
		
		//THEN
		assertThat(result).isEqualTo(expected.getRate());
	}
	
	
	//@Test 
	public void testinterpolation_2(){
		//GIVEN
		RateCoordinate floor = new RateCoordinate();
		floor.setPeriodYear(new Double(0.75));
		floor.setRate(new Double(75));
		
		RateCoordinate ceil = new RateCoordinate();
		ceil.setPeriodYear(new Double(1.25));
		ceil.setRate(new Double(125));
		
		periodYear = new Double(1.0);
		RateCoordinate expected = new RateCoordinate();
		expected.setPeriodYear(new Double(1.0));
		expected.setRate(new Double(100));

		//WHEN
		Double result = this.testeur.interpolate(rateCurvesByDate.get(new DateTime(1993,1,4,0,0,0)), periodYear) ;	
		
		//THEN
		assertThat(result).isEqualTo(expected.getRate());
	}
	
		
	@After
	public void tearDown(){
         log.info("TEAR DOWNNNN");
	}
	
}
