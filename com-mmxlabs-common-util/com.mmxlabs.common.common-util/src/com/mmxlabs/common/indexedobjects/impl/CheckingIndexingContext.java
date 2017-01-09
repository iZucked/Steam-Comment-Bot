/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mmxlabs.common.indexedobjects.IIndexingContext;

/**
 * Implementation of {@link IIndexingContext} which only accepts registered types. An {@link IllegalArgumentException} will be thrown for unrecognised types.
 * 
 * @author Simon Goodall
 * @since 1.0.2
 */
public final class CheckingIndexingContext implements IIndexingContext {

	/**
	 * The {@link Map} of indices; each entry maps the type to the next index for objects of that type or (or with that as their closest superclass).
	 */
	private final Map<Class<? extends Object>, AtomicInteger> indices = new HashMap<Class<? extends Object>, AtomicInteger>();

	/**
	 * Flag indicating whether or not this context has been used to generate an index.
	 */
	private boolean used = false;

	public CheckingIndexingContext() {
		super();
		// always track objects
		registerType(Object.class);
	}

	@Override
	public void registerType(final Class<? extends Object> type) {
		if (indices.containsKey(type)) {
			throw new RuntimeException(type + " has already been registered");
		}

		if (used) {
			throw new RuntimeException("This context has been used - no more types can be registered, for the sake of index consistency");
		}

		indices.put(type, new AtomicInteger(-1));
	}

	@Override
	public final int assignIndex(final Object indexedObject) {

		used = true;

		final AtomicInteger index = getLowestSuperclass(indexedObject.getClass());

		assert index != null;

		return index.incrementAndGet();
	}

	/**
	 * This method is slow and clumsy, but we need it to avoid unexpected consequences of subclassing a type which is indexed and suddenly breaking all our indexed data structures.
	 * 
	 * @param type
	 * @return
	 */
	private final AtomicInteger getLowestSuperclass(final Class<? extends Object> baseType) {

		Class<? extends Object> type = baseType;

		while (type != null) {
			if (indices.containsKey(type)) {
				if (type == Object.class) {
					throw new IllegalArgumentException("Error, baseType does not have a registered class: " + baseType.getSimpleName());
				}
				return indices.get(type);
			}
			type = type.getSuperclass();
		}

		// Should never get here as Object.class is a registered type.
		throw new IllegalStateException("Error, baseType does not have a registered class: " + baseType.getSimpleName());
	}

	@Override
	public String toString() {
		return "CheckingIndexingContext [indices=" + indices + ", used=" + used + "]";
	}
}
