package com.mmxlabs.common;

/**
 * Interface defining a mechanism to transform an object of one type to another
 * type.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 * @param <U>
 */
public interface ITransformer<T, U> {

	/**
	 * Transform object to another type.
	 * 
	 * @param t
	 * @return
	 */
	U transform(T t);

}
