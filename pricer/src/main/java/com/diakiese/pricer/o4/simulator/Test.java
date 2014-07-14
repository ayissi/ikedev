package com.diakiese.pricer.o4.simulator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
														
import com.diakiese.pricer.l2servicelayer.BondPricerImpl;
import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.RateCurveWrapper;
import com.diakiese.pricer.o3utils.BondPricerUtils;
import com.diakiese.pricer.o3utils.RateCurveBuilderImpl;

public class Test {												
//	final static String rateFile = "C:/taux2.csv";				
	final static Logger log = Logger.getLogger(Test.class);
	private static String xDate;
	private static Double yBondPrice; 
	
	public static void main(String[] args) throws IOException{
		DateTime emissionDate = new DateTime(1993,1,4,0,0,0); 
    	Bond.BondBuilder bondBuilder = new Bond().new BondBuilder(); 
		Bond bond = bondBuilder.withPeriodicity(3)																																		         
				.withEmissionDate(emissionDate) 
				.withMaturity(20)  
				.withTauxFacial(0.05)
				.withNominalAmount(new Double(100))
				.build();
				
				
		RateCurveBuilderImpl builder = new RateCurveBuilderImpl();  
		RateCurveWrapper rateCurveWrapper = builder.createRateCurve2();   
			
		Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate = rateCurveWrapper.getRateCoordinatesByDate(); 
        
		BondPricerImpl bondPricer = new BondPricerImpl(); 
        DateTime dateDebut = new DateTime(1993,1,4,0,0,0);											  
        DateTime dateFin = dateDebut.plusYears(bond.getBondMaturity());	
        for(DateTime pricingDate=dateDebut ; dateFin.compareTo(pricingDate)>0 ; pricingDate=pricingDate.plusDays(1)){
        	xDate = BondPricerUtils.getDateStringFormat(pricingDate);
        	yBondPrice = bondPricer.price2(bond, pricingDate, rateCoordinatesByDate);	
        	log.info("X_: " + xDate + "\tY_:" + yBondPrice);																																	
        }
//		log.info("ZC025YR: " + getPeriodYear("ZC025YR")); 
	}
	

	public static Double getPeriodYear(String s){
		String[] tabToMov_ZC = s.split("ZC");
		String[] tabToMov_YR = tabToMov_ZC[1].split("YR");
		return new Double(tabToMov_YR[0])*0.01 ;
	}

}
