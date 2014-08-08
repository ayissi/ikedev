package com.diakiese.pricer.l2servicelayer;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.diakiese.pricer.l2servicelayer.interpolation.Iinterpolator;
import com.diakiese.pricer.l2servicelayer.interpolation.LinearInterpolator;
import com.diakiese.pricer.l2servicelayer.pricing.BondPricerTauxFixeImpl;
import com.diakiese.pricer.l2servicelayer.ratecurve.ExcelRateCurveBuilderImpl;
import com.diakiese.pricer.l2servicelayer.ratecurve.IRateCurveBuilder;
import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;

	
public class BondPricerTauxFixeImplTest {

	static BondPricerTauxFixeImpl pricer ;
	static DateTime emissionDate;
	static Bond bond;  
	static Iinterpolator interpolator;
	static IRateCurveBuilder testRateCurveBuilder = new ExcelRateCurveBuilderImpl();

	@BeforeClass
	public static void prepareTestEnv(){				
		pricer = new BondPricerTauxFixeImpl(testRateCurveBuilder, new LinearInterpolator());													 
		emissionDate = new DateTime(1993,1,1,0,0,0);
    	Bond.BondBuilder bondBuilder = new Bond().new BondBuilder(); 
		bond = bondBuilder.withPeriodicity(3)
				.withPeriodicityInYear(0.25)
				.withEmissionDate(emissionDate) 
				.withMaturity(10) 
				.withNominalAmount(new Double(100))
				.build();
		pricer.setBond(bond);
		pricer.setFixRate(0.015);  
		interpolator = new LinearInterpolator();
		pricer.setInterpolator(interpolator);
	}
	

	@AfterClass
	public static void cleanTestEnv(){
		
	}


	@Test 
	public void testPriceSimpleCoupon(){
		//GIVEN
		DateTime pricingDate1 = new DateTime(2001,2,1,0,0,0);
		DateTime pricingDate2 = new DateTime(2002,12,1,0,0,0);
		
		//WHEN
		Double price1 = pricer.priceSimpleCoupon(pricingDate1);
		Double price2 = pricer.priceSimpleCoupon(pricingDate2);
		
		//THEN
		assertThat(price1).isNotNull();
		assertThat(price2).isNotNull();
		assertThat(price1).isNotEqualTo(0.0);
		assertThat(price2).isNotEqualTo(0.0);
		assertThat(price1).isNotEqualTo(price2);
	}
			
			
	@Test 
	public void testPriceCouponCouru(){
		//GIVEN
		DateTime pricingDate1 = new DateTime(2001,2,1,0,0,0);
		DateTime pricingDate2 = new DateTime(2002,12,1,0,0,0);
		
		//WHEN
		Double price1 = pricer.priceCouponCouru(pricingDate1);
		Double price2 = pricer.priceCouponCouru(pricingDate2);
		
		//THEN
		assertThat(price1).isNotNull();
		assertThat(price2).isNotNull();
		assertThat(price1).isNotEqualTo(0.0);
		assertThat(price2).isNotEqualTo(0.0);
		assertThat(price1).isNotEqualTo(price2);
	}
	
				
	@Test				
	public void testGetNombreFluxRestant(){
		//GIVEN
		DateTime pricingDate1 = new DateTime(1993,2,1,0,0,0);
		DateTime pricingDate2 = new DateTime(1993,12,1,0,0,0);
		
		Integer expectedNbre1 = 40 ;
		Integer expectedNbre2 = 37 ;
		
		//WHEN
		Integer result1 = pricer.getNombreDeFluxRestant(bond, pricingDate1);
		Integer result2 = pricer.getNombreDeFluxRestant(bond, pricingDate2);
		
		//THEN
		assertThat(result1).isEqualTo(expectedNbre1);
		assertThat(result2).isEqualTo(expectedNbre2);
		
	}
	

	@Test
	public void testGetJoursDepuisDerniereEcheance(){
		//GIVEN
		DateTime pricingDate = bond.getEmissionDate().plusDays((new Double(2*bond.getPeriodicityInYear()*365)).intValue()).plusDays(5);
		Double expected = new Double(5.0);	
		
		//WHEN
		Double result = pricer.getJoursDepuisDerniereEcheance(bond, pricingDate); 
		
		//THEN
		assertThat(result).isEqualTo(expected);
	}


	@Test 
	public void testGetAccurateCoordinates(){
		//GIVEN
		Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate = pricer.getRateCoordinatesByDate();
		DateTime lastValidDateEntry =  new DateTime(1994,2,28,0,0,0);
		DateTime fisrtEmptyEntry = new DateTime(1994,3,2,0,0,0);
		DateTime secondEmptyEntry = new DateTime(1994,3,7,0,0,0);
		DateTime thirdEmptyEntry = new DateTime(1994,3,6,0,0,0);
		
		List<RateCoordinate> expected = rateCoordinatesByDate.get(lastValidDateEntry);
			
		//WHEN
		List<RateCoordinate> result1 = pricer.getAccurateRateCoordinates(rateCoordinatesByDate, fisrtEmptyEntry);
		List<RateCoordinate> result2 = pricer.getAccurateRateCoordinates(rateCoordinatesByDate, secondEmptyEntry);
		List<RateCoordinate> result3 = pricer.getAccurateRateCoordinates(rateCoordinatesByDate, thirdEmptyEntry); 
			
		//THEN
		assertThat(result1).isEqualTo(expected);
		assertThat(result2).isEqualTo(expected);
		assertThat(result3).isEqualTo(expected);
	}
	
	
	@Before
	public void setUp(){
		
	}
	
	@Before
	public void setUp2(){
		
	}
	@After
	public void tearDown(){
		
	}

}
