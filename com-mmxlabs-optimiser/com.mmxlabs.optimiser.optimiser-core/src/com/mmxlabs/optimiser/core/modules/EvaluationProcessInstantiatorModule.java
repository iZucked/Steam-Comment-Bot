/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.modules;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationProcessInstantiator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class EvaluationProcessInstantiatorModule extends AbstractModule {

	public static final String ENABLED_EVALUATION_PROCESS_NAMES = "EnabledEvaluationProcessNames";

	@Override
	protected void configure() {

	}

	@Provides
	private List<IEvaluationProcess> provideEvaluationProcesses(@NonNull final Injector injector, @NonNull final IEvaluationProcessRegistry evaluationProcessRegistry,
			@NonNull @Named(ENABLED_EVALUATION_PROCESS_NAMES) final List<String> enabledProcessNames, @NonNull final IOptimisationData optimisationData) {
		final EvaluationProcessInstantiator evaluationProcessInstantiator = new EvaluationProcessInstantiator();
		final List<IEvaluationProcess> evaluationProcesses = evaluationProcessInstantiator.instantiateEvaluationProcesses(evaluationProcessRegistry, enabledProcessNames, optimisationData);

		final List<IEvaluationProcess> result = new ArrayList<IEvaluationProcess>(evaluationProcesses.size());
		for (final IEvaluationProcess process : evaluationProcesses) {
			if (process != null) {
				result.add(process);
				injector.injectMembers(process);
			}
		}
		return result;
	}

}
