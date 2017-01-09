/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Maps the annotations attached to a particular SequenceElement.
 * 
 * @author hinton
 * 
 */
public interface IElementAnnotationsMap {
	/**
	 * Store an annotation object for the specified element under the given key.
	 * 
	 * @param element
	 * @param key
	 * @param annotation
	 */
	void setAnnotation(@NonNull ISequenceElement element, @NonNull String key, @NonNull IElementAnnotation annotation);

	/**
	 * Return an additional data object tied to the given sequence element.
	 * 
	 * @param <U>
	 * @param key
	 * @param clz
	 * @return
	 */
	@Nullable
	<U extends IElementAnnotation> U getAnnotation(@NonNull ISequenceElement element, @NonNull String key, @NonNull Class<U> clz);

	/**
	 * Returns true if the given element has an annotation under the given key.
	 * 
	 * @param element
	 * @param key
	 * @return
	 */
	boolean hasAnnotation(@NonNull ISequenceElement element, @NonNull String key);

	/**
	 * Returns an {@link Iterable} over all the keys for which the given element has an annotation set.
	 * 
	 * @param element
	 * @return
	 */
	@NonNull
	Iterable<String> getAnnotationNames(@NonNull ISequenceElement element);

	@NonNull
	Map<String, IElementAnnotation> getAnnotations(@NonNull ISequenceElement element);
}
