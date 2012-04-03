/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessFactory;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessInstantiator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Class used to obtain {@link IEvaluationProcess}s from the {@link IEvaluationProcessRegistry}
 * 
 * @author Simon Goodall
 * 
 */
public final class EvaluationProcessInstantiator implements IEvaluationProcessInstantiator {

	@Override
	public List<IEvaluationProcess> instantiateEvaluationProcesses(final IEvaluationProcessRegistry registry, final IOptimisationData optimisationData) {

		final Collection<IEvaluationProcessFactory> factories = registry.getEvaluationProcessFactories();
		final List<IEvaluationProcess> checkers = new ArrayList<IEvaluationProcess>(factories.size());
		for (final IEvaluationProcessFactory factory : factories) {
			final IEvaluationProcess checker = factory.instantiate();

			checkers.add(checker);
		}
		return checkers;
	}

	@Override
	public List<IEvaluationProcess> instantiateEvaluationProcesses(final IEvaluationProcessRegistry registry, final List<String> evaluationProcessNames, final IOptimisationData optimisationData) {

		final List<IEvaluationProcess> evaluationProcesses = new ArrayList<IEvaluationProcess>(evaluationProcessNames.size());

		// Mapping between constraint checker name and instance
		final Map<String, IEvaluationProcess> evaluationProcessMap = new HashMap<String, IEvaluationProcess>();

		// Get Collection of relevant factories.
		final Collection<IEvaluationProcessFactory> factories = registry.getEvaluationProcessFactories(evaluationProcessNames);

		// Process factories instantiating constraint checkers and add to the
		// map.
		for (final IEvaluationProcessFactory factory : factories) {

			final String name = factory.getName();
			final IEvaluationProcess checker = factory.instantiate();
			evaluationProcessMap.put(name, checker);
		}

		// Generate the ordered list of constraint checkers.
		for (final String name : evaluationProcessNames) {

			final IEvaluationProcess evaluationProcess;
			if (evaluationProcessMap.containsKey(name)) {
				evaluationProcess = evaluationProcessMap.get(name);
			} else {
				evaluationProcess = null;
			}
			evaluationProcesses.add(evaluationProcess);
		}

		return evaluationProcesses;
	}
}
