package com.diakiese.pricer.o1bean;

/**
 * Represente une coordonnée de la courbe des taux.
 * Avec en abcisse <b>xPeriodYear</b> et en ordonées <b>yRate</b>
 * 
 * */
public class RateCoordinate {
		
	private Double xPeriodYear;
	private Double yRate ;


	public Double getRate() {
		return yRate;  
	}

	public void setRate(Double rate) {
		this.yRate = rate;
	}

	public void setPeriodYear(Double _periodYear){
		this.xPeriodYear = _periodYear ;
	}
	
	public Double getPeriodYear() {
		return xPeriodYear;
	}

	public RateCoordinate(Double _periodYear,Double _rate){
		this.xPeriodYear= _periodYear;
		this.yRate = _rate;
	}
	
	public RateCoordinate(Double _periodYear){
		this.xPeriodYear= _periodYear;
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
