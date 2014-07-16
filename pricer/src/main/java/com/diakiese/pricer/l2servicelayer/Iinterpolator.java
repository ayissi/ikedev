package com.diakiese.pricer.l2servicelayer;

import java.util.List;

import com.diakiese.pricer.o1bean.RateCoordinate;

public interface Iinterpolator {
	/**
	 * retourne le taux interpolé d'un RateCoordinate X ayant comme date <b>midPeriodYear</b>
	 * @param floorElt le RateCoordinate précedent X
	 * @param ceilElt  le RateCoordinate suivant X
	 * @param midPeriodYear la periodYear de X, borné par floorElt.periodYear et ceilElt.periodYear 
	 * */						  
	public  Double interpolate(RateCoordinate floorElt, RateCoordinate ceilElt, Double midPeriodYear);
	
	/**
	 * retourne le taux interpolé d'un RateCoordinate X ayant comme date <b>midPeriodYear</b>
	 * @param floorElt le RateCoordinate précedent X
	 * @param ceilElt  le RateCoordinate suivant X
	 * @param midPeriodYear la periodYear de X, borné par floorElt.periodYear et ceilElt.periodYear 
	 * */						  
	public  Double interpolate(List<RateCoordinate> rateCoordinates, Double midPeriodYear); 
}
