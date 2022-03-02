/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.inject.scopes;

import com.google.inject.AbstractModule;

public class NotInjectedScopeModule extends AbstractModule {
	@Override
	public void configure() {
		NotInjectedScopeImpl impl = new NotInjectedScopeImpl();

		// tell Guice about the scope
		bindScope(NotInjectedScope.class, impl);

		// make our scope instance injectable
		bind(NotInjectedScopeImpl.class).toInstance(impl);
	}
}