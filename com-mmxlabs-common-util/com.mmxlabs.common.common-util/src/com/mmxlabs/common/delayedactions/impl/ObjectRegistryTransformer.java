/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.common.delayedactions.impl;

import com.mmxlabs.common.ITransformer;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.delayedactions.ObjectRegistry;

public final class ObjectRegistryTransformer<U> implements ITransformer<Pair<Class<U>, Object>, U> {

	private final ObjectRegistry registry;

	public ObjectRegistryTransformer(final ObjectRegistry registry) {
		this.registry = registry;
	}

	@Override
	public U transform(final Pair<Class<U>, Object> pair) {
		return registry.getValue(pair.getFirst(), pair.getSecond());
	}
}
