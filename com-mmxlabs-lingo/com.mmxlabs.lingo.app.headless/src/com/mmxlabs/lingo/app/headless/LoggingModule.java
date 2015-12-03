/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

/**
 * A {@link Module} providing the data from {@link ILoggingDataStore} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class LoggingModule extends AbstractModule {

	private final Map<String, LSOLogger> phaseToLoggerMap;
	private final AbstractRunnerHook runnerHook;
	private final int reportingInterval;

	public LoggingModule(final Map<String, LSOLogger> phaseToLoggerMap, final AbstractRunnerHook runnerHook, final int reportingInterval) {
		this.phaseToLoggerMap = phaseToLoggerMap;
		this.runnerHook = runnerHook;
		this.reportingInterval = reportingInterval;
	}

	@Override
	protected void configure() {

	}

	@Provides
	private ILoggingDataStore providerILoggingDataStore(@NonNull final LSOLogger logger) {
		return logger;
	}

	@Provides
	private LSOLogger providerLSOLogger(@NonNull final Injector injector) {
		final String phase = runnerHook.getPhase();
		assert phase != null && !phase.isEmpty();
		if (phaseToLoggerMap.containsKey(phase)) {
			return phaseToLoggerMap.get(phase);
		}

		final LSOLogger logger = new LSOLogger(reportingInterval);
		injector.injectMembers(logger);

		phaseToLoggerMap.put(phase, logger);
		return logger;

	}
}
