/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.lso.logging.ILoggingProvider;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;
import com.mmxlabs.optimiser.lso.logging.LSOLogger.LoggingParameters;
import com.mmxlabs.scheduler.optimiser.actionplan.ActionSetLogger;
import com.mmxlabs.scheduler.optimiser.actionplan.BagOptimiser;

/**
 * A {@link Module} providing the data from {@link ILoggingDataStore} to the {@link Injector} framework.
 * 
 * @author Simon Goodall
 * 
 */
public class LoggingModule extends AbstractModule {

	private final Map<String, LSOLogger> phaseToLoggerMap;
	private final AbstractRunnerHook runnerHook;
	private final ActionSetLogger actionSetLogger;
	private final LSOLogger.LoggingParameters loggingParameters;

	public LoggingModule(final Map<String, LSOLogger> phaseToLoggerMap, final ActionSetLogger actionSetLogger, final AbstractRunnerHook runnerHook, final LoggingParameters loggingParameters) {
		this.phaseToLoggerMap = phaseToLoggerMap;
		this.runnerHook = runnerHook;
		
		this.loggingParameters = loggingParameters;
		
		this.actionSetLogger = actionSetLogger;
	}

	@Override
	protected void configure() {

	}

	@Provides
	private ILoggingDataStore providerILoggingDataStore(@NonNull final LSOLogger logger) {
		return logger;
	}

	@Provides
	@Named("PHASE_TO_LOGGER_MAP")
	private Map<String, LSOLogger> providePhaseToLoggerMap() {
		return phaseToLoggerMap;
	}

	@Provides
	@Named("RUNNER_HOOK")
	private AbstractRunnerHook provideRunnerHook() {
		return runnerHook;
	}

	/*
	@Provides
	@Named("REPORTING_INTERVAL")
	private int provideReportingInterval() {
		return reportingInterval;
	}
	*/

	
	@Provides
	private LSOLogger.LoggingParameters provideLoggingParameters() {
		return loggingParameters;
	}
	// @Provides
	// private LSOLogger providerLSOLogger(@NonNull final Injector injector) {
	// final String phase = runnerHook.getPhase();
	// assert phase != null && !phase.isEmpty();
	// if (phaseToLoggerMap.containsKey(phase)) {
	// return phaseToLoggerMap.get(phase);
	// }
	//
	// final LSOLogger logger = new LSOLogger(reportingInterval);
	// injector.injectMembers(logger);
	//
	// phaseToLoggerMap.put(phase, logger);
	// return logger;
	// }

	@Provides
	private LSOLogger providerLSOLogger(@NonNull final Injector injector) {
		return null;
	}

	@Provides
	private ILoggingProvider provideLoggingProvider(@NonNull final Injector injector) {
		final LoggingProvider logger = new LoggingProvider();
		injector.injectMembers(logger);
		return logger;
	}

	@Provides
	@Named(BagOptimiser.ACTION_PLAN__LOGGER)
	private ActionSetLogger providerActionSetLogger(@NonNull final Injector injector) {
		return actionSetLogger;
	}
}
