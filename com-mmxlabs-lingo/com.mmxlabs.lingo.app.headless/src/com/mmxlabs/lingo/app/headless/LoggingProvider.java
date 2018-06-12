/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.mmxlabs.models.lng.transformer.ui.AbstractRunnerHook;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.fitness.IFitnessEvaluator;
import com.mmxlabs.optimiser.lso.logging.ILoggingProvider;
import com.mmxlabs.optimiser.lso.logging.LSOLogger;

public class LoggingProvider implements ILoggingProvider {

	@Inject
	@Named("PHASE_TO_LOGGER_MAP")
	Map<String, LSOLogger> phaseToLoggerMap;

	@Inject
	@Named("RUNNER_HOOK")
	AbstractRunnerHook runnerHook;

	@Inject
	@Named("REPORTING_INTERVAL")
	int reportingInterval;

	@Inject
	@NonNull
	Injector injector;

	@Override
	public LSOLogger providerLSOLogger(IFitnessEvaluator fitnessEvaluator, IOptimisationContext context) {
		final String stageAndID = runnerHook.getStageAndJobID();
		assert stageAndID != null && !stageAndID.isEmpty();
		if (phaseToLoggerMap.containsKey(stageAndID)) {
			return phaseToLoggerMap.get(stageAndID);
		}
		final LSOLogger logger = new LSOLogger(reportingInterval, fitnessEvaluator, context);
		injector.injectMembers(logger);

		phaseToLoggerMap.put(stageAndID, logger);
		return logger;
	}

}
