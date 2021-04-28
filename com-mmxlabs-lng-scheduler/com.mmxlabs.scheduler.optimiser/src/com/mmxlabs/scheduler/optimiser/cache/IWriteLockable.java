/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.cache;

/**
 * Implementations of this interface can be made immutable by calling {@link #writeLock()}. Any further attempt to modify the item should throw a {@link WriteLockedException}. Implementations should
 * consider making any internal collections immutable too. This is intended to be used for items that form part of a cache and need to be changed whilst constructing, but allows us to ensure it is not
 * changed further once cached.
 * 
 * @author Simon Goodall
 *
 */
public interface IWriteLockable {

	/**
	 * Disallow any further changes to this object. This method may mutate the object itself (e.g. replacing mutable collections with immutable colections), but no further changes are permitted once
	 * this method completes.
	 * 
	 */
	void writeLock();

	/**
	 * Write lock the object is it is an instanceof {@link IWriteLockable}.
	 * 
	 * @param object
	 */
	static void writeLock(Object object) {
		if (object instanceof IWriteLockable) {
			((IWriteLockable) object).writeLock();
		}
	}

	/**
	 * Write lock any objects in the Iterable what are instances of {@link IWriteLockable}.
	 * 
	 * @param objects
	 */
	static void writeLock(Iterable<?> objects) {
		objects.forEach(IWriteLockable::writeLock);
	}
}
