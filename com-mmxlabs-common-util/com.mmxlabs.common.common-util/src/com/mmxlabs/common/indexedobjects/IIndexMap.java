/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects;

import java.util.NoSuchElementException;

import org.eclipse.jdt.annotation.NonNull;

/**
 * A map from indexed objects to other objects, mainly for use in DCPs.
 * 
 * I haven't extended java.util.Map because most of the features of Map are not required here, and because this is conceptually quite separate and doesn't make any of the same guarantees about key
 * equality tests and the like.
 * 
 * (C) minimax labs 2010
 * 
 * @author hinton
 * 
 * @param <T>
 * @param <U>
 */
public interface IIndexMap<T extends IIndexedObject, U> {
	/**
	 * Return the U associated with the given T, or null if there is no U associated with that T.
	 * 
	 * @param key
	 * @return
	 */
	public U maybeGet(@NonNull T key);

	/**
	 * Return the U associated with the given T, or throw a {@link NoSuchElementException} if there is no U.
	 * 
	 * @param key
	 * @return
	 */
	public U get(@NonNull T key);

	/**
	 * Associate the value U with key T
	 * 
	 * @param key
	 * @param value
	 */
	public void set(@NonNull T key, U value);

	/**
	 * Get the values contained in this relation.
	 * 
	 * @return
	 */
	public Iterable<U> getValues();

	public void clear();
}
