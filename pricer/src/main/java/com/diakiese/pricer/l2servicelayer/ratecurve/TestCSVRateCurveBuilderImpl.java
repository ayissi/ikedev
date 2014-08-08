package com.diakiese.pricer.l2servicelayer.ratecurve;

import org.apache.log4j.Logger;

public class TestCSVRateCurveBuilderImpl extends CSVRateCurveBuilder implements IRateCurveBuilder {
		
	final static Logger log = Logger.getLogger(TestCSVRateCurveBuilderImpl.class);
	final String rateFile = "C:/dev/taux6.csv";	
		
	@Override
	public String getFile() {
		return getRateFile();
	}

	public String getRateFile() {
		return rateFile;
	}

}
