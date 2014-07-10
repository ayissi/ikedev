/**
 * 
 */
package com.diakiese.pricer.o1bean;

import org.joda.time.DateTime;

import com.diakiese.pricer.o3utils.BondPricerUtils;



/**
 * Classe modélisant une obligation
 * @author guy.belomo
 *
 */
public class Bond {
	
	/**
	 * la periodicité exprimée en mois
	 * */
	private Integer periodicity ;
	
	/**
	 * la maturité exprimée en année
	 * */
	private Integer bondMaturity;
	
	/**
	 * 
	 * */
	private DateTime emissionDate;
	
	/**
	 * le montant nominal
	 * */
	private Double nominalAmount;
	
	/**
	 * le montant de coupon
	 * */
	private Double  coupondAmount;
	
	public Integer getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(Integer periodicity) {
		this.periodicity = periodicity;
	}
	public Integer getBondMaturity() {
		return bondMaturity;
	}
	public void setBondMaturity(Integer bondMaturity) {
		this.bondMaturity = bondMaturity;
	}
	public DateTime getEmissionDate() {
		return emissionDate;
	}
	public void setEmissionDate(DateTime emissionDate) {
		this.emissionDate = emissionDate;
	}
	public Double getNominalAmount() {
		return nominalAmount;
	}
	public void setNominalAmount(Double nominalAmount) {
		this.nominalAmount = nominalAmount;
	}
	public Double getCoupondAmount() {
		return coupondAmount;
	}
	public void setCoupondAmount(Double coupondAmount) {
		this.coupondAmount = coupondAmount;
	}


	public String toString(){
	
		String s = new StringBuilder().
				 append("Periodicité: ")
				.append(this.periodicity)
				.append(" mois")
				.append(" Maturité: ")
				.append(this.bondMaturity)
				.append(" ans")
				.append(" Date Emission: ")
				.append(BondPricerUtils.getDateStringFormat(this.emissionDate)) 
				.append(" Nominal: ")
				.append(this.nominalAmount).toString(); 
		return s; 
	}
	
	public Bond(){
		
	}
	private Bond(BondBuilder builder){
		this.bondMaturity = builder.bondMaturity;
		this.periodicity = builder.periodicity ;
		this.emissionDate = builder.emissionDate ;
		this.nominalAmount = builder.nominalAmount ;
		this.coupondAmount = builder.coupondAmount;
	}
	
	public class BondBuilder {
	
		private Integer periodicity ;
		private Integer bondMaturity;
		private DateTime emissionDate;
		private Double nominalAmount;
		private Double  coupondAmount;
				
		public BondBuilder withPeriodicity(Integer periodicity){
			this.periodicity = periodicity ;
			return this ;
		}
		
		public BondBuilder withMaturity(Integer maturity){
			this.bondMaturity = maturity ;
			return this;
		}
		
		public BondBuilder withEmissionDate(DateTime emissionDate){
			this.emissionDate = emissionDate ;
			return this;
		}
		
		public BondBuilder withNominalAmount(Double nominal){
			this.nominalAmount = nominal ;
			return this;
		}
		
		public BondBuilder withCouponAmount(Double coupon){
			this.coupondAmount = coupon ;
			return this ;
		}
		
		public Bond build(){
			return new Bond(this);
		}
	};
}
