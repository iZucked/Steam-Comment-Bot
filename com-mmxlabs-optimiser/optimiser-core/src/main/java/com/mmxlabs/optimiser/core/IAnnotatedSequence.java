package com.mmxlabs.optimiser.core;

import com.mmxlabs.optimiser.core.fitness.IFitnessCore;

/**
 * The {@link IAnnotatedSequence} provides additional information to the
 * standard {@link ISequence} typically populated as a results of an
 * {@link IFitnessCore} evaluation. This could include things such as time,
 * distance and cost information.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public interface IAnnotatedSequence<T> {

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
	 * Release any internal resources.
	 */
	void dispose();
}
