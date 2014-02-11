/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.Map;

/**
 * Maps the annotations attached to a particular SequenceElement.
 * 
 * @author hinton
 * 
 */
public interface IAnnotations {
	/**
	 * Store an annotation object for the specified element under the given key.
	 * 
	 * @param element
	 * @param key
	 * @param value
	 */
	void setAnnotation(ISequenceElement element, String key, Object value);

	/**
	 * Return an additional data object tied to the given sequence element.
	 * 
	 * @param <U>
	 * @param key
	 * @param clz
	 * @return
	 */
	<U> U getAnnotation(ISequenceElement element, String key, Class<U> clz);

	/**
	 * Returns true if the given element has an annotation under the given key.
	 * 
	 * @param element
	 * @param key
	 * @return
	 */
	boolean hasAnnotation(ISequenceElement element, String key);

	/**
	 * Returns an {@link Iterable} over all the keys for which the given element has an annotation set.
	 * 
	 * @param element
	 * @return
	 */
	Iterable<String> getAnnotationNames(ISequenceElement element);

	Map<String, Object> getAnnotations(ISequenceElement element);
}
