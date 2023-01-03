/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.common.indexedobjects.IIndexingContext;

@NonNullByDefault
public abstract class AbstractIndexingContext implements IIndexingContext {

	/**
	 * The {@link Map} of indices; each entry maps the type to the next index for
	 * objects of that type or (or with that as their closest superclass).
	 */
	protected final Map<Class<?>, AtomicInteger> indices = new HashMap<>();

	/**
	 * Flag indicating whether or not this context has been used to generate an
	 * index.
	 */
	protected boolean used = false;

	protected AbstractIndexingContext() {
		super();
		// always track objects
		registerType(Object.class);
	}

	@Override
	public void registerType(final Class<?> type) {
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
	 * This method is slow and clumsy, but we need it to avoid unexpected
	 * consequences of subclassing a type which is indexed and suddenly breaking all
	 * our indexed data structures.
	 * 
	 * @param type
	 * @return
	 */
	protected abstract AtomicInteger getLowestSuperclass(final Class<?> baseType);

}
