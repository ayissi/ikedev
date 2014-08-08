package com.diakiese.pricer.l2servicelayer.ratecurve;

import org.apache.log4j.Logger;
														
public class CSVRateCurveBuilderImpl extends CSVRateCurveBuilder implements IRateCurveBuilder {
	
	final static Logger log = Logger.getLogger(CSVRateCurveBuilderImpl.class);
	final  String rateFile = "C:/taux2.csv";
	
	public String getRateFile() {
		return rateFile;
	}

	@Override
	public String getFile() {
		return getRateFile();
	}										
			
}
