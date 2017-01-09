/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common;

/**
 * Interface defining a mechanism to transform an object of one type to another type.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 * @param <U>
 */
public interface ITransformer<T, U> {

	/**
	 * Transform object to another type. Returns null if unable to transform.
	 * 
	 * @param t
	 * @return
	 */
	U transform(T t);

}
