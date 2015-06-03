/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.lso.LSOLoggingConstants;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintChecker;
import com.mmxlabs.scheduler.optimiser.fitness.impl.enumerator.EnumeratingSequenceScheduler;
import com.mmxlabs.scheduler.optimiser.lso.SequencesConstrainedMoveGeneratorUnit;

/**
 * A {@link Module} providing the data from {@link ILoggingDataStore} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class LoggingModule extends AbstractModule {

	private final Map<String, ILoggingDataStore> loggingDataStores = new HashMap<>();

	public LoggingModule(final LSOLogger lsoLogger) {
		this.loggingDataStores.put(LSOLoggingConstants.LSO_LOGGER, lsoLogger);
	}

	@Override
	protected void configure() {
		
		for (String name : loggingDataStores.keySet()) {
		    bind(ILoggingDataStore.class)
	        .annotatedWith(Names.named(name))
	        .toInstance(getLoggingDataStore(name));
		    if (getLoggingDataStore(name) instanceof LSOLogger) {
			    bind(LSOLogger.class)
		        .annotatedWith(Names.named(name))
		        .toInstance((LSOLogger) getLoggingDataStore(name));
		    }
		}
	}
	
	private ILoggingDataStore getLoggingDataStore(String name) {
		return loggingDataStores.get(name);
	}
}
