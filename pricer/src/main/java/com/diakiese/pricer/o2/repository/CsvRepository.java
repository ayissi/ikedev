/**
 * 
 */
package com.diakiese.pricer.o2.repository;

/**
 * @author guy.belomo
 *
 */
public abstract class CsvRepository<T> {
	
	public abstract T create(T object); 
	
	public abstract T read(int id);
	
	public abstract void delete(T object);
	
	public abstract void update(T object);

}
