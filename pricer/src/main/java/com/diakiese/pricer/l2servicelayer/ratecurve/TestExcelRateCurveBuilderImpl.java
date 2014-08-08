package com.diakiese.pricer.l2servicelayer.ratecurve;
		
import java.io.IOException;

import com.diakiese.pricer.o1bean.RateCurveWrapper;

public class TestExcelRateCurveBuilderImpl implements IRateCurveBuilder {

	final static String TEST_RATE_FILE ="C:/dev/taux2.xls";
	
	@Override
	public RateCurveWrapper createRateCurve(String file) throws IOException {
		return null;
	}

	@Override
	public String getFile() {
		return TEST_RATE_FILE;
	}

}
