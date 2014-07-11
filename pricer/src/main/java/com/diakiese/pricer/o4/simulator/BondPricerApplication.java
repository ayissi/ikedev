package com.diakiese.pricer.o4.simulator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.diakiese.pricer.l2servicelayer.BondPricerImpl;
import com.diakiese.pricer.l2servicelayer.RateCurveBuilderImpl;
import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o1bean.RateCurveWrapper;
import com.diakiese.pricer.o3utils.BondPricerUtils;
	
	
@SuppressWarnings("rawtypes")
public class BondPricerApplication extends Application {
    final static String rateFile = "C:/taux2.csv";	
	
	final static Logger log = Logger.getLogger(BondPricerApplication.class);
    @SuppressWarnings({ "unchecked"})
	@Override public void start(Stage stage) throws IOException {
    	DateTime emissionDate = new DateTime(1993,1,4,0,0,0);
//    	DateTime pricingDate = new DateTime(new DateTime(1995,5,17,0,0,0));
    		
    	Bond.BondBuilder bondBuilder = new Bond().new BondBuilder(); 
		Bond bond = bondBuilder.withPeriodicity(3)																																		         
				.withEmissionDate(emissionDate) 
				.withMaturity(10)  
				.withTauxFacial(0.05)
				.withNominalAmount(new Double(100))
				.build();
				

		RateCurveBuilderImpl builder = new RateCurveBuilderImpl();  
		RateCurveWrapper rateCurveWrapper = builder.createRateCurve(rateFile);   
		
		Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate = rateCurveWrapper.getRateCoordinatesByDate();  
//		List<DateTime> plannedBillingDates = BondPricerUtils.getFuturesBillingDates(bond.getPeriodicity(), bond.getBondMaturity(), bond.getEmissionDate(),pricingDate);

		stage.setTitle("ACENSI / GUY BELOMO / PRICER");
        
		final CategoryAxis xAxis = new CategoryAxis();
        	
        final NumberAxis yAxis = new NumberAxis();
         xAxis.setLabel("Date: ");
         yAxis.setLabel("Prix de l'obligation: ");		
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
 		XYChart.Series series1 = new XYChart.Series();
        series1.setName("Evolution du prix des obligations durant la periode de son paiement");	
        lineChart.setTitle(bond.toString());													
        String xDate = "";							 
        Number yBondPrice = 0;
     
        BondPricerImpl bondPricer = new BondPricerImpl(); 
        DateTime dateDebut = new DateTime(1993,1,4,0,0,0);											  
        DateTime dateFin = dateDebut.plusYears(bond.getBondMaturity());						

        for(DateTime pricingDate=dateDebut ; dateFin.compareTo(pricingDate)>0 ; pricingDate=pricingDate.plusDays(1)){
        	xDate = BondPricerUtils.getDateStringFormat(pricingDate);
        	yBondPrice = bondPricer.price(bond, pricingDate, rateCoordinatesByDate); 
        	series1.getData().add(new XYChart.Data(xDate, yBondPrice));
        }
        
        Scene scene  = new Scene(lineChart,1800,900);       
        lineChart.getData().addAll(series1);
 
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
