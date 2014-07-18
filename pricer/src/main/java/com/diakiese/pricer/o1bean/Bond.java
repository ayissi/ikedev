/**
 * 
 */
package com.diakiese.pricer.o1bean;
import org.joda.time.DateTime;

import com.diakiese.pricer.o3utils.BondPricerUtils;



/**
 * Classe modélisant une obligation à taux fixe
 * @author guy.belomo  
 */
public class Bond {
	
	/**
	 * la periodicité exprimée en mois
	 * */
	private Integer periodicityInMonths ;
	
	/**
	 * la maturité exprimée en année
	 * */
	private Integer bondMaturity;
	
	/**
	 * date d'emission 
	 * */
	private DateTime emissionDate;
	
	/**
	 * le montant nominal
	 * */
	private Double nominalAmount;

	private Double periodicityInYear ;
	
	public Double getPeriodicityInYear() {
		return periodicityInYear;
	}
	public void setPeriodicityInYear(Double periodicityInYear) {
		this.periodicityInYear = periodicityInYear;
	}
	public Integer getPeriodicity() {
		return periodicityInMonths;
	}
	public void setPeriodicity(Integer periodicity) {
		this.periodicityInMonths = periodicity;
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



	public String toString(){
	
		String s = new StringBuilder().
				 append("Periodicité: ")
				.append(this.periodicityInMonths)
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
	
	public Bond(){}
	
		
	private Bond(BondBuilder builder){
		this.bondMaturity = builder.bondMaturity;
		this.periodicityInMonths = builder.periodicityInMonths ;
		this.emissionDate = builder.emissionDate ;
		this.nominalAmount = builder.nominalAmount ;
		this.periodicityInYear = builder.periodicityInYear;	
	}


	public class BondBuilder {
		private Integer periodicityInMonths ;
		private Integer bondMaturity;
		private DateTime emissionDate;
		private Double nominalAmount;
		private Double periodicityInYear ;
		
		public BondBuilder withPeriodicity(Integer periodicity){
			this.periodicityInMonths = periodicity ;
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
		
		public BondBuilder withPeriodicityInYear(Double periodicityInYear){
			this.periodicityInYear = periodicityInYear;
			return this;  
		}

		public Bond build(){
			return new Bond(this);
		}
	};
}
