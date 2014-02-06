package com.diakiese.pricer.o1bean;
									
/**
 * L'emetteur d'une obligation fait plusieurs remboursements au détenteur de l'obligation.
 * Ce en raison d'un remboursement par echeance
 * Cette classe modélise un remboursement.
 * */
public class Remboursement {
	
	/**
	 * l'identification unique de l'obligation liée a ce remboursement 
	 * */
	private String cleObligation; 
	
	/**
	 * un remboursement est lié a une échéance
	 * */
	private Short numeroEcheance;
	
	/**
	 * le montant de ce remboursement 
	 * */
	private Double montant ;
	
	

	public String getCleObligation() {
		return cleObligation;
	}

	public void setCleObligation(String cleObligation) {
		this.cleObligation = cleObligation;
	}

	public Short getNumeroEcheance() {
		return numeroEcheance;
	}

	public void setNumeroEcheance(Short numeroEcheance) {
		this.numeroEcheance = numeroEcheance;
	}

	public Double getMontant() {
		return montant;
	}

	public void setMontant(Double montant) {
		this.montant = montant;
	}
	
}
