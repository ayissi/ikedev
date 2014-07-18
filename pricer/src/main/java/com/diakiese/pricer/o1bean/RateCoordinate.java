package com.diakiese.pricer.o1bean;

public class RateCoordinate {
			
	private Double periodYear;
	private Double rate ;


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

	public RateCoordinate(Double _periodYear,Double _rate){
		this.periodYear= _periodYear;
		this.rate = _rate;
	}
	
	public RateCoordinate(Double _periodYear){
		this.periodYear= _periodYear;
	}
		
	public RateCoordinate(){
			
	}
	
	@Override
	public boolean equals(Object o){
		boolean b = false;
		if(o instanceof RateCoordinate){
		    RateCoordinate that = (RateCoordinate)o;
			if((this.getPeriodYear().equals(that.getPeriodYear())&&(this.getRate().equals(that.getRate())))){
				b = true ; 
			}
		}	
		return b;
	}
	
}
