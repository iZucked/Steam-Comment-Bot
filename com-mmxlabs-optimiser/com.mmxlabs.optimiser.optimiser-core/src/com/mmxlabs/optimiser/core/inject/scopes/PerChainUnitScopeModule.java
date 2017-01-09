/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.inject.scopes;

import com.google.inject.AbstractModule;

public class PerChainUnitScopeModule extends AbstractModule {
	@Override
	public void configure() {
		PerChainUnitScopeImpl perChainUnitScope = new PerChainUnitScopeImpl();

		// tell Guice about the scope
		bindScope(PerChainUnitScope.class, perChainUnitScope);

		// make our scope instance injectable
		bind(PerChainUnitScopeImpl.class).toInstance(perChainUnitScope);
	}
}