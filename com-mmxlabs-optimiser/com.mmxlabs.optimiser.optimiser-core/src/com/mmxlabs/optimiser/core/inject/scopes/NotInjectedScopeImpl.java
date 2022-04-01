/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.inject.scopes;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Simple Scope to forbid object use in the injector.
 */
public class NotInjectedScopeImpl implements Scope {

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
		return new Provider<T>() {
			@Override
			public T get() {
				throw new IllegalStateException("Object is marked as not injectable");
			}
		};
	}

}