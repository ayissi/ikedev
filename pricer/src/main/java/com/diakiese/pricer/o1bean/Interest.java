package com.diakiese.pricer.o1bean;

public class Interest {
	
	private Double ratioDate ;
	
	private Double rate ;
	
	public Double getRatioDate() {
		return ratioDate;
	}
	public void setRatioDate(Double ratioDate) {
		this.ratioDate = ratioDate;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	public Double getInterest(){
		return 1/Math.pow((1+rate),ratioDate);
	}
	

}
