/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.impl;

import java.util.Map;

import com.mmxlabs.common.ITransformer;

/**
 * {@link ITransformer} implementation which uses a {@link Map} to transform a known set of types.
 * 
 * @author Simon Goodall
 * 
 */
public final class MappingTransformer<T, U> implements ITransformer<T, U> {

	private final Map<T, U> mapping;

	public MappingTransformer(final Map<T, U> mapping) {
		this.mapping = mapping;
	}

	@SuppressWarnings("null")
	@Override
	public U transform(final T t) {
		if (mapping.containsKey(t)) {
			return mapping.get(t);
		}
		return (U) null;
	}

	/**
	 * Returns the {@link Map} used in this {@link MappingTransformer}.
	 * 
	 * @return
	 */
	public Map<T, U> getMapping() {
		return mapping;
	}
}
