package com.diakiese.pricer.l1ihm;
															
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.diakiese.pricer.l2servicelayer.interpolation.LinearInterpolator;
import com.diakiese.pricer.l2servicelayer.pricing.BondPricerTauxFixeImpl;
import com.diakiese.pricer.l2servicelayer.pricing.BondPricerTauxVariableImpl;
import com.diakiese.pricer.l2servicelayer.ratecurve.ExcelRateCurveBuilderImpl;
import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o3utils.BondPricerUtils;
												

@SuppressWarnings("rawtypes")
public class BondPricerApplication extends Application {
			
	final static Logger log = Logger.getLogger(BondPricerApplication.class);						 							  
	private BondPricerTauxFixeImpl bondTauxFixePricer = new BondPricerTauxFixeImpl(new ExcelRateCurveBuilderImpl(), new LinearInterpolator());    									
	private BondPricerTauxVariableImpl bondTauxVariableImpl = new BondPricerTauxVariableImpl();
		
    @SuppressWarnings({ "unchecked"})
	@Override public void start(Stage stage) throws IOException {
    	DateTime emissionDate = new DateTime(1993,1,4,0,0,0);
    		
    	Bond.BondBuilder bondBuilder = new Bond().new BondBuilder(); 
		Bond bond = bondBuilder.withPeriodicity(6)																																		         
				.withEmissionDate(emissionDate) 
				.withPeriodicityInYear(0.5)
				.withMaturity(10) 
				.withNominalAmount(new Double(100))
				.build();
		
		stage.setTitle("ACENSI/GUY BELOMO/PRICER");
        
		final CategoryAxis xAxis = new CategoryAxis();	
        final NumberAxis yAxis = new NumberAxis();
        	
        xAxis.setLabel("Date: ");																								
        yAxis.setLabel("Prix de l'obligation: ");																		
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
 		XYChart.Series series1TauxFixe = new XYChart.Series();
 		XYChart.Series series2TauxVariable = new XYChart.Series();
 		XYChart.Series series3TauxFixeCouponCouru = new XYChart.Series();
        series1TauxFixe.setName("TF-Coupon Simple");					
        series2TauxVariable.setName("TAUX VARIABLE");  
        series3TauxFixeCouponCouru.setName("TF- Coupon Couru");        								
        lineChart.setTitle(bond.toString());													
        String xDate = "";        																						 
        Number yBondPriceTauxFixe = 0;  
        Number yBondPriceTauxFixe_CouponCouru = 0;
        Number yBondPriceTauxVariable = 0;		
        		
        bondTauxFixePricer.setBond(bond);
        bondTauxFixePricer.setFixRate(new Double(0.15));                  
        DateTime dateDebut = new DateTime(1993,1,4,0,0,0);											  
        DateTime dateFin = dateDebut.plusYears(bond.getBondMaturity());						
        	
        for(DateTime pricingDate=dateDebut ; dateFin.compareTo(pricingDate)>0 ; pricingDate=pricingDate.plusDays(1)){
        	xDate = BondPricerUtils.getDateStringFormat(pricingDate);
        	yBondPriceTauxFixe = bondTauxFixePricer.priceSimpleCoupon(pricingDate);  
        	yBondPriceTauxVariable = new Double(0.0);									  
        	yBondPriceTauxFixe_CouponCouru  = bondTauxFixePricer.priceCouponCouru(pricingDate);  
        	series1TauxFixe.getData().add(new XYChart.Data(xDate, yBondPriceTauxFixe));
        	series2TauxVariable.getData().add(new XYChart.Data(xDate, yBondPriceTauxVariable));
        	series3TauxFixeCouponCouru.getData().add(new XYChart.Data(xDate, yBondPriceTauxFixe_CouponCouru));
        	log.info("\tDate: " + xDate +"\tPrixTauxFixe: "+ yBondPriceTauxFixe + "\tPrixTauxVariable: " + yBondPriceTauxVariable);																	         
        }
        Scene scene  = new Scene(lineChart,1800,900);       
        lineChart.getData().addAll(series1TauxFixe);
        lineChart.getData().addAll(series2TauxVariable);
        lineChart.getData().addAll(series3TauxFixeCouponCouru);  
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
