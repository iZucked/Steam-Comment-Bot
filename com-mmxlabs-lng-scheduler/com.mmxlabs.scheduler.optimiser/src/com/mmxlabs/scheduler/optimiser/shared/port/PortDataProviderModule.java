/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mmxlabs.scheduler.optimiser.shared.port.impl.DefaultDistanceMatrixEditor;
import com.mmxlabs.scheduler.optimiser.shared.port.impl.DefaultPortProviderEditor;

/**
 * 
 * @author Simon Goodall
 */
public class PortDataProviderModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(DefaultDistanceMatrixEditor.class).in(Singleton.class);
		bind(IDistanceMatrixProvider.class).to(DefaultDistanceMatrixEditor.class);
		bind(IDistanceMatrixEditor.class).to(DefaultDistanceMatrixEditor.class);

		bind(DefaultPortProviderEditor.class).in(Singleton.class);
		bind(IPortProvider.class).to(DefaultPortProviderEditor.class);
		bind(IPortProviderEditor.class).to(DefaultPortProviderEditor.class);
	}
}
