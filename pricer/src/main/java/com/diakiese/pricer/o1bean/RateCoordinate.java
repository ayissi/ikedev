package com.diakiese.pricer.o1bean;

public class RateCoordinate {
			
	private String periodZone;
	private Double periodYear;
	private Double rate ;

	public String getPeriodZone() {
		return periodZone;
	}
		
	public void setPeriodZone(String periodZone) {
		this.periodZone = periodZone;
	}
		
	public Double getRate() {
		return rate;  
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public void setPeriodYear(Double _periodYear){
		this.periodYear = _periodYear ;
	}
	
	public Double getPeriodYear() {
		return periodYear;
	}

	public void initPeriodYear(){ 
		String[] tabToMov_ZC = getPeriodZone().split("ZC");
		String[] tabToMov_YR = tabToMov_ZC[1].split("YR");
		this.periodYear = new Double(tabToMov_YR[0])*0.01 ; 
	}
	
	public RateCoordinate(String _periodZone,Double _rate){
		this.periodZone = _periodZone;
		this.rate = _rate;
		initPeriodYear();
	}
	
	public RateCoordinate(String _periodZone){
		this.periodZone = _periodZone;
		this.rate = null;
		initPeriodYear(); 
	}
	
	public RateCoordinate(){
			
	}
	
	public RateCoordinate(Double _periodYear){
		this.periodYear = _periodYear;
	}
	
}
