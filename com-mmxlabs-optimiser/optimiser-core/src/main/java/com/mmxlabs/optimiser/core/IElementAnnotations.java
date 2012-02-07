/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

/**
 * Interface to store "additional info" objects based upon keys.
 * 
 * @author Simon Goodall
 * 
 */
public interface IElementAnnotations {

	/**
	 * Return the object stored under the given key. The object will be cast to the given class or a {@link ClassCastException} will be thrown.
	 * 
	 * @param <U>
	 * @param key
	 * @param clz
	 * @return
	 */
	<U> U get(String key, Class<U> clz);

	/**
	 * Store the object under the given key.
	 * 
	 * @param key
	 * @param value
	 */
	void put(String key, Object value);

	/**
	 * Returns true if the given key has an annotation stored for it.
	 * 
	 * @param key
	 * @return
	 */
	boolean containsKey(String key);

	/**
	 * Clean up refs
	 */
	void dispose();

}