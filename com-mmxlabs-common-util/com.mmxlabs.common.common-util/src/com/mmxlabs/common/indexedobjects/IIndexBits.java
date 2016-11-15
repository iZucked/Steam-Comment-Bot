/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A thing which maps a boolean to IIndexedObjects.
 * 
 * @author hinton
 * 
 */
public interface IIndexBits<T extends IIndexedObject> {
	/**
	 * Returns true if set(element) has been called and clear(element) has not been called subsequently.
	 * 
	 * @param element
	 * @return
	 */
	boolean isSet(@NonNull T element);

	/**
	 * Set that an element is marked
	 * 
	 * @param element
	 */
	void set(@NonNull T element);

	/**
	 * Clear the mark against an element
	 * 
	 * @param element
	 */
	void clear(@NonNull T element);
}
