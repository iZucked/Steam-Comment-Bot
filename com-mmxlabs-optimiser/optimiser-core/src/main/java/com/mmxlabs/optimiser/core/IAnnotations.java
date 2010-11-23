/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.optimiser.core;

/**
 * @author hinton
 *
 */
public interface IAnnotations<T> {
	/**
	 * Store an annotation object for the specified element under the given key.
	 * 
	 * @param element
	 * @param key
	 * @param value
	 */
	void setAnnotation(T element, String key, Object value);

	/**
	 * Return an additional data object tied to the given sequence element.
	 * 
	 * @param <U>
	 * @param key
	 * @param clz
	 * @return
	 */
	<U> U getAnnotation(T element, String key, Class<U> clz);

	/**
	 * Returns true if the given element has an annotation under the given key.
	 * 
	 * @param element
	 * @param key
	 * @return
	 */
	boolean hasAnnotation(T element, String key);

	/**
	 * Returns an iterable over all the keys for which the given element
	 * has an annotation set.
	 * 
	 * @param element
	 * @return
	 */
	Iterable<String> getAnnotations(T element);
}
