/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared.port;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixEditor;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.impl.IndexedMultiMatrixProvider;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.shared.SharedPortDistanceDataBuilder;

/**
 * 
 * @author Simon Goodall
 */
public class PortDataProviderModule extends AbstractModule {

	@Override
	protected void configure() {

		final IndexedMultiMatrixProvider<IPort, Integer> portDistanceProvider = new IndexedMultiMatrixProvider<IPort, Integer>();
		bind(new TypeLiteral<IMultiMatrixEditor<IPort, Integer>>() {
		}).toInstance(portDistanceProvider);

		bind(new TypeLiteral<IMultiMatrixProvider<IPort, Integer>>() {
		}).toInstance(portDistanceProvider);

		bind(new TypeLiteral<IndexedMultiMatrixProvider<IPort, Integer>>() {
		}).toInstance(portDistanceProvider);

		// final IPortProviderEditor portProvider = new DefaultPortProviderEditor();
		bind(IPortProvider.class).to(DefaultPortProviderEditor.class);
		bind(IPortProviderEditor.class).to(DefaultPortProviderEditor.class);
	}

	@Provides
	@Singleton
	private DefaultPortProviderEditor provideDefaultPortProvider() {
		return new DefaultPortProviderEditor();
	}
}
