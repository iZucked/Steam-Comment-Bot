/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

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
	public boolean isSet(T element);

	/**
	 * Set that an element is marked
	 * 
	 * @param element
	 */
	public void set(T element);

	/**
	 * Clear the mark against an element
	 * 
	 * @param element
	 */
	public void clear(T element);
}
