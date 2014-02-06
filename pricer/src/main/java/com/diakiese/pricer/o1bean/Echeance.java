package com.diakiese.pricer.o1bean;
			
import org.joda.time.DateTime;


/**
 * Le remboursement d'une obligation se fait en plusieurs echeances.
 * Cette classe modélise une échéance.
 * */
public class Echeance implements Comparable<Echeance>{
	
	private Long echeanceId;
	private String codeObligation;  

	
	/**
	 * date à laquelle cette échéance doit être échue.
	 * ressemble à une date de prélèvement automatique
	 * */
	private DateTime date ;
			
	/**
	 * son rang dans l'ordre des remboursement
	 * */
	private Short rank;

	/**
	 * la valeur de la courbe des taux correspondant à cette échéance 
	 * */
	private Double taux;
	
	/**
	 * le coefficient correspondant à cette échéance
	 * */
	private Double coefficient;
	
	public Long getEcheanceId() {
		return echeanceId;
	}

	public void setEcheanceId(Long echeanceId) {
		this.echeanceId = echeanceId;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public Short getRank() {
		return rank;
	}

	public void setRank(Short rank) {
		this.rank = rank;
	}
			
	@Override
	public int compareTo(Echeance echeance) { 
		return this.rank - echeance.getRank();
	}
			
	
	public String getCodeObligation() {
		return codeObligation;
	}

	public void setCodeObligation(String codeObligation) {
		this.codeObligation = codeObligation;
	}

	public Double getTaux() {
		return taux;
	}

	public void setTaux(Double taux) {
		this.taux = taux;
	}

	public Double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}
	
	/**
	 * confère la formule 1/Math.pow[1+taux,coefficient]
	 * retourne l'intérêt sur cette échéance
	 * */
	public Double interet() {
		return 1/Math.pow(1 + getTaux(), getCoefficient()); 
	}
	

}
