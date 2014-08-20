package com.diakiese.pricer.l2bean;

public class Bond {

	private Integer periodicity;
	private String emissionDate ;
	private Integer maturity ;
	private String dateDebut ;
	private String dateFin ;
	private Integer nominal ;
	
	public Integer getPeriodicity() {
		return periodicity;
	}
	public void setPeriodicity(Integer periodicity) {
		this.periodicity = periodicity;
	}
	public String getEmissionDate() {
		return emissionDate;
	}
	public void setEmissionDate(String emissionDate) {
		this.emissionDate = emissionDate;
	}
	public Integer getMaturity() {
		return maturity;
	}
	public void setMaturity(Integer maturity) {
		this.maturity = maturity;
	}
	public String getDateDebut() {
		return dateDebut;
	}
	public void setDateDebut(String dateDebut) {
		this.dateDebut = dateDebut;
	}
	public String getDateFin() {
		return dateFin;
	}
	public void setDateFin(String dateFin) {
		this.dateFin = dateFin;
	}
	public Integer getNominal() {
		return nominal;
	}
	public void setNominal(Integer nominal) {
		this.nominal = nominal;
	}
	
	
}
