/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.indexedobjects.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.indexedobjects.IIndexingContext;

/**
 * Implementation of {@link IIndexingContext} which only accepts registered
 * types. An {@link IllegalArgumentException} will be thrown for unrecognised
 * types.
 * 
 * @author Simon Goodall
 * @since 1.0.2
 */
@NonNullByDefault
public final class CheckingIndexingContext extends AbstractIndexingContext {

	/**
	 * This method is slow and clumsy, but we need it to avoid unexpected
	 * consequences of subclassing a type which is indexed and suddenly breaking all
	 * our indexed data structures.
	 * 
	 * @param type
	 * @return
	 */
	protected final AtomicInteger getLowestSuperclass(final Class<?> baseType) {
		{
			@Nullable
			Class<?> type = baseType;

			while (type != null) {
				if (indices.containsKey(type)) {
					if (type == Object.class) {
						break;
					}
					return indices.get(type);
				}
				type = type.getSuperclass();
			}
		}
		for (final Class<?> type : baseType.getInterfaces()) {
			if (indices.containsKey(type)) {
				if (type == Object.class) {
					throw new IllegalArgumentException("Error, baseType does not have a registered class: " + baseType.getSimpleName());
				}
				return indices.get(type);
			}
		}

		// Should never get here as Object.class is a registered type.
		throw new IllegalStateException("Error, baseType does not have a registered class: " + baseType.getSimpleName());
	}

	@Override
	public String toString() {
		return "CheckingIndexingContext [indices=" + indices + ", used=" + used + "]";
	}
}
