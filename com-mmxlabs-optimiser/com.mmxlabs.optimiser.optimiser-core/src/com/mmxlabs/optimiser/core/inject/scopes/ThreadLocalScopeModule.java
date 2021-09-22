/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.inject.scopes;

import com.google.inject.AbstractModule;

public class ThreadLocalScopeModule extends AbstractModule {
	@Override
	public void configure() {
		ThreadLocalScopeImpl perChainUnitScope = new ThreadLocalScopeImpl();

		// tell Guice about the scope
		bindScope(ThreadLocalScope.class, perChainUnitScope);

		// make our scope instance injectable
		bind(ThreadLocalScopeImpl.class).toInstance(perChainUnitScope);
	}
}