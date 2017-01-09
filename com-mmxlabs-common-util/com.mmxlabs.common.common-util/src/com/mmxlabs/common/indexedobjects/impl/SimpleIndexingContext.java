/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.common.indexedobjects.IIndexingContext;

public final class SimpleIndexingContext implements IIndexingContext {

	private static final Logger log = LoggerFactory.getLogger(SimpleIndexingContext.class);

	/**
	 * The {@link Map} of indices; each entry maps the type to the next index for objects of that type or (or with that as their closest superclass).
	 */
	private final Map<Class<? extends Object>, AtomicInteger> indices = new HashMap<Class<? extends Object>, AtomicInteger>();
	/**
	 * A set to keep track of what types we have complained about indexing as plain Objects
	 */
	private final Set<Class<? extends Object>> warnedTypes = new HashSet<Class<? extends Object>>();

	/**
	 * Flag indicating whether or not this context has been used to generate an index.
	 */
	private boolean used = false;

	public SimpleIndexingContext() {
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
				if ((type == Object.class) && !warnedTypes.contains(baseType)) {
					log.warn("Warning: using object index for " + baseType.getSimpleName());
					warnedTypes.add(baseType);
				}
				return indices.get(type);
			}
			type = type.getSuperclass();
		}

		// Should never get here as Object.class is a registered type.
		throw new IllegalStateException("Error, baseType does not have a registered class");
	}

	@Override
	public String toString() {
		return "SimpleIndexingContext [indices=" + indices + ", warnedTypes=" + warnedTypes + ", used=" + used + "]";
	}
}
