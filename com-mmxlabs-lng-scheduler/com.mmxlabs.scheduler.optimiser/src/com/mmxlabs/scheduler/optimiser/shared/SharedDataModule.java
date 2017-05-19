/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.shared;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.mmxlabs.scheduler.optimiser.shared.port.PortDataProviderModule;

/**
 * 
 * @author Simon Goodall
 */
public class SharedDataModule extends AbstractModule {

	@Override
	protected void configure() {

		install(new PortDataProviderModule());
		
		bind(SharedPortDistanceDataBuilder.class).in(Singleton.class);
	}
}
