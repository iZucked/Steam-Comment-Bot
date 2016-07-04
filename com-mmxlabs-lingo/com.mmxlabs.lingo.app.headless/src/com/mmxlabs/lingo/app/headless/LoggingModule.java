/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.models.lng.transformer.ui.ActionSetLogger;
import com.mmxlabs.optimiser.common.logging.ILoggingDataStore;
import com.mmxlabs.optimiser.lso.logging.ILoggingProvider;
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
	private final ActionSetLogger actionSetLogger;
	
	private final static String ACTION_PLAN_LOGGER = "ACTION_PLAN_LOGGER";
	
	public LoggingModule(final Map<String, LSOLogger> phaseToLoggerMap, final ActionSetLogger actionSetLogger, final AbstractRunnerHook runnerHook, final int reportingInterval) {
		this.phaseToLoggerMap = phaseToLoggerMap;
		this.runnerHook = runnerHook;
		this.reportingInterval = reportingInterval;
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
	
	@Provides
	@Named("REPORTING_INTERVAL")
	private int provideReportingInterval() {
		return reportingInterval;
	}
	
//	@Provides
//	private LSOLogger providerLSOLogger(@NonNull final Injector injector) {
//		final String phase = runnerHook.getPhase();
//		assert phase != null && !phase.isEmpty();
//		if (phaseToLoggerMap.containsKey(phase)) {
//			return phaseToLoggerMap.get(phase);
//		}
//
//		final LSOLogger logger = new LSOLogger(reportingInterval);
//		injector.injectMembers(logger);
//
//		phaseToLoggerMap.put(phase, logger);
//		return logger;
//	}
	
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
	@Named(ACTION_PLAN_LOGGER)
	private ActionSetLogger providerActionSetLogger(@NonNull final Injector injector) {
		return actionSetLogger;
	}
}
