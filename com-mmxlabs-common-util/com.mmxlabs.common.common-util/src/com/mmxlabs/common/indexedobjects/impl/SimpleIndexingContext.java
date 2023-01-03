/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public final class SimpleIndexingContext extends AbstractIndexingContext {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleIndexingContext.class);

	/**
	 * A set to keep track of what types we have complained about indexing as plain
	 * Objects
	 */
	private final Set<Class<?>> warnedTypes = new HashSet<>();

	protected final AtomicInteger getLowestSuperclass(final Class<?> baseType) {
		{
			Class<?> type = baseType;

			while (type != null) {
				if (indices.containsKey(type)) {
					if ((type == Object.class) && !warnedTypes.contains(baseType)) {
						LOG.warn("Warning: using object index for {}", baseType.getSimpleName());
						warnedTypes.add(baseType);
					}
					return indices.get(type);
				}
				type = type.getSuperclass();
			}
		}
		for (final Class<?> type : baseType.getInterfaces()) {

			if (indices.containsKey(type)) {
				if ((type == Object.class) && !warnedTypes.contains(baseType)) {
					LOG.warn("Warning: using object index for {}", baseType.getSimpleName());
					warnedTypes.add(baseType);
				}
				return indices.get(type);
			}
		}

		// Should never get here as Object.class is a registered type.
		throw new IllegalStateException("Error, baseType does not have a registered class");
	}

	@Override
	public String toString() {
		return "SimpleIndexingContext [indices=" + indices + ", warnedTypes=" + warnedTypes + ", used=" + used + "]";
	}
}
