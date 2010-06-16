package com.mmxlabs.scheduler.optimiser.fitness;

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
	 * Release any internal resources.
	 */
	void dispose();

}
