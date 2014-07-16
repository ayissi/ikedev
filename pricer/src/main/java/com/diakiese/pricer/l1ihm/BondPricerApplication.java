package com.diakiese.pricer.l1ihm;
															
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

import com.diakiese.pricer.l2servicelayer.BondPricerTauxFixeImpl;
import com.diakiese.pricer.l2servicelayer.BondPricerTauxVariableImpl;
import com.diakiese.pricer.o1bean.Bond;
import com.diakiese.pricer.o1bean.RateCoordinate;
import com.diakiese.pricer.o3utils.BondPricerUtils;
												

@SuppressWarnings("rawtypes")
public class BondPricerApplication extends Application {
				
	final static Logger log = Logger.getLogger(BondPricerApplication.class);
    
	private BondPricerTauxFixeImpl bondTauxFixePricer = new BondPricerTauxFixeImpl();
    
    private BondPricerTauxVariableImpl bondTauxVariableImpl = new BondPricerTauxVariableImpl();
	
    @SuppressWarnings({ "unchecked"})
	@Override public void start(Stage stage) throws IOException {
    	DateTime emissionDate = new DateTime(1993,1,4,0,0,0);
    		
    	Bond.BondBuilder bondBuilder = new Bond().new BondBuilder(); 
		Bond bond = bondBuilder.withPeriodicity(6)																																		         
				.withEmissionDate(emissionDate) 
				.withMaturity(10) 
				.withNominalAmount(new Double(100))
				.build();
		
		
 
		
		Map<DateTime,List<RateCoordinate>> rateCoordinatesByDate = bondTauxFixePricer.buildRateCoordinatesByDate();
		
		stage.setTitle("ACENSI / GUY BELOMO / PRICER");
        
		final CategoryAxis xAxis = new CategoryAxis();	
        final NumberAxis yAxis = new NumberAxis();
        	
        xAxis.setLabel("Date: ");																								
        yAxis.setLabel("Prix de l'obligation: ");																		
        final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
 		XYChart.Series series1TauxFixe = new XYChart.Series();
 		XYChart.Series series2TauxVariable = new XYChart.Series();
        series1TauxFixe.setName("TAUX FIXE");					
        series2TauxVariable.setName("TAUX VARIABLE");
        lineChart.setTitle(bond.toString());													
        String xDate = "";																						 
        Number yBondPriceTauxFixe = 0;		
        Number yBondPriceTauxVariable = 0;		
        

        bondTauxFixePricer.setBond(bond);
        bondTauxFixePricer.setRateCoordinatesByDate(rateCoordinatesByDate);
        DateTime dateDebut = new DateTime(1993,1,4,0,0,0);											  
        DateTime dateFin = dateDebut.plusYears(bond.getBondMaturity());						
        	
        for(DateTime pricingDate=dateDebut ; dateFin.compareTo(pricingDate)>0 ; pricingDate=pricingDate.plusDays(1)){
        	xDate = BondPricerUtils.getDateStringFormat(pricingDate);
        	yBondPriceTauxFixe = bondTauxFixePricer.price(pricingDate); 
        	yBondPriceTauxVariable = bondTauxVariableImpl.price(pricingDate);
        	series1TauxFixe.getData().add(new XYChart.Data(xDate, yBondPriceTauxFixe));
        	series2TauxVariable.getData().add(new XYChart.Data(xDate, yBondPriceTauxVariable));
        	log.info("\tDate: " + xDate +"\tPrixTauxFixe: "+ yBondPriceTauxFixe + "\tPrixTauxVariable: " + yBondPriceTauxVariable);																	         
        }
        
        Scene scene  = new Scene(lineChart,1800,900);       
        lineChart.getData().addAll(series1TauxFixe);
        lineChart.getData().addAll(series2TauxVariable);					
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
