package com.diakiese.pricer.o4.simulator;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o3utils.RateCurveBuilderImpl;

public class Test {
				
	final static Logger log = Logger.getLogger(Test.class); 
	public static void main(String[] args) throws IOException{
		RateCurveBuilderImpl rateCurveBuilder = new RateCurveBuilderImpl();
		Bond.BondBuilder builder = new Bond().new BondBuilder(); 
		Bond bond = builder.withPeriodicity(6)											         
				.withEmissionDate(new DateTime(2011,1,1,0,0,0))
				.withMaturity(2)
				.withCouponAmount(new Double(1.5))
				.withNominalAmount(new Double(100))
				.build();
		
//		DateTime pricingDate = new DateTime(2011,1,1,0,0,0);
//		
//		Map<DateTime,List<Double>> rateCurve = rateCurveBuilder.createRateCurve().getRateCurveDateTimed(); 
//	   
//		BondPricerImpl bondPricer = new BondPricerImpl();
//		log.info("bond price: " + bondPricer.getBondPrice(bond, pricingDate, rateCurve)); 
		log.info("ZC025YR: " + getPeriodYear("ZC025YR")); 
	}
	

	public static Double getPeriodYear(String s){
		String[] tabToMov_ZC = s.split("ZC");
		String[] tabToMov_YR = tabToMov_ZC[1].split("YR");
		return new Double(tabToMov_YR[0])*0.01 ;
	}

}
