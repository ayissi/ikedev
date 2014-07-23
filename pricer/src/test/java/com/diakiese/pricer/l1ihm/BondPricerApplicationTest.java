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

import com.diakiese.pricer.l2servicelayer.BondPricerTauxFixeImpl;
import com.diakiese.pricer.l2servicelayer.BondPricerTauxVariableImpl;
import com.diakiese.pricer.l2servicelayer.Iinterpolator;
import com.diakiese.pricer.l2servicelayer.LinearInterpolator;
import com.diakiese.pricer.l2servicelayer.TestCSVRateCurveBuilderImpl;
import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.TYPE_PRICING;
import com.diakiese.pricer.o3utils.BondPricerUtils;

public class BondPricerApplicationTest extends Application {
							
	final static Logger log = Logger.getLogger(BondPricerApplicationTest.class);
    
//	private BondPricerTauxFixeImpl bondTauxFixePricer = new BondPricerTauxFixeImpl(new TestRateCurveBuilderImpl());
    
    private BondPricerTauxVariableImpl bondTauxVariableImpl = new BondPricerTauxVariableImpl();
	
    @SuppressWarnings({ "unchecked", "rawtypes"})
	@Override public void start(Stage stage) throws IOException {
    	DateTime emissionDate = new DateTime(1993,1,4,0,0,0);
    	BondPricerTauxFixeImpl bondTauxFixePricer = new BondPricerTauxFixeImpl(new TestCSVRateCurveBuilderImpl());
    	
    	Bond.BondBuilder bondBuilder = new Bond().new BondBuilder(); 
		Bond bond = bondBuilder.withPeriodicity(6)																																		         
				.withEmissionDate(emissionDate) 
				.withPeriodicityInYear(0.5) 
				.withMaturity(10)				 
				.withNominalAmount(new Double(100))
				.build();
		
		bondTauxFixePricer.setBond(bond);
		bondTauxFixePricer.setFixRate(0.15);
		Iinterpolator interpolator = new LinearInterpolator();
		bondTauxFixePricer.setInterpolator(interpolator); 
			
		stage.setTitle("ACENSI / GUY BELOMO / PRICER");
        
		final CategoryAxis xAxis = new CategoryAxis();	
        final NumberAxis yAxis = new NumberAxis();
        	
        xAxis.setLabel("Date: ");																								
        yAxis.setLabel("Prix de l'obligation: ");																		
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
 		XYChart.Series series1TauxFixe = new XYChart.Series();
 		XYChart.Series series2TauxVariable = new XYChart.Series();
 		XYChart.Series series3TauxFixeCouponCouru = new XYChart.Series();
        series1TauxFixe.setName("TAUX FIXE");					
        series2TauxVariable.setName("TAUX VARIABLE");  
        series3TauxFixeCouponCouru.setName("COUPON_COURU");      
        lineChart.setTitle(bond.toString());													
        String xDate = "";																						 
        Number yBondPriceTauxFixe = 0;
        Number yBondPriceTauxFixe_CouponCouru = 0;   
        Number yBondPriceTauxVariable = 0;		
        	
        DateTime dateDebut = new DateTime(1993,1,8,0,0,0);											  
        DateTime dateFin = dateDebut.plusYears(bond.getBondMaturity());	
//        DateTime dateFin = new DateTime(1994,8,20,10,0,0); 

        for(DateTime pricingDate=dateDebut ; dateFin.compareTo(pricingDate)>0 ; pricingDate=pricingDate.plusDays(1)){
        	xDate = BondPricerUtils.getDateStringFormat(pricingDate);
        	yBondPriceTauxFixe = bondTauxFixePricer.price(pricingDate,TYPE_PRICING.COUPON_SIMPLE); 
//        	yBondPriceTauxVariable = new Double(128.0);
        	yBondPriceTauxFixe_CouponCouru = bondTauxFixePricer.price(pricingDate,TYPE_PRICING.COUPON_COURU); 
//        	if(yBondPriceTauxFixe!= new Double(0.0)){ }		   
        	series1TauxFixe.getData().add(new XYChart.Data(xDate, yBondPriceTauxFixe));
        	series2TauxVariable.getData().add(new XYChart.Data(xDate, yBondPriceTauxVariable)); 
        	series3TauxFixeCouponCouru.getData().add(new XYChart.Data(xDate, yBondPriceTauxFixe_CouponCouru)); 
        	log.info("\tDate: " + xDate +"\tPrix_COUPON_SIMPLE: "+ yBondPriceTauxFixe + "\tPrixT_COUPON_COURU: " + yBondPriceTauxFixe_CouponCouru);																	         
        }
        
        Scene scene  = new Scene(lineChart,1800,900);				       
        lineChart.getData().addAll(series1TauxFixe);
//        lineChart.getData().addAll(series2TauxVariable);	
        lineChart.getData().addAll(series3TauxFixeCouponCouru);
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }

}
