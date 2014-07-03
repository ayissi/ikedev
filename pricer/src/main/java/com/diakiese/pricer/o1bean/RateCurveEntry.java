/**
 * 
 */
package com.diakiese.pricer.o1bean;

import java.util.List;

/**
 * @author guy.belomo
 *
 */
public class RateCurveEntry {
	
	String key ;
	List<Double> value ;
	

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<Double> getValue() {
		return value;
	}
	public void setValue(List<Double> value) {
		this.value = value;
	}

	
	
}
