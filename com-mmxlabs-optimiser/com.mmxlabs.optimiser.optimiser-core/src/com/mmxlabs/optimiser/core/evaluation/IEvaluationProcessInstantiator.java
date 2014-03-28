/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.evaluation;

import java.util.List;

import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public interface IEvaluationProcessInstantiator {

	/**
	 * Return new instances of all {@link IEvaluationProcess}s the {@link IEvaluationProcessRegistry} knows about.
	 * 
	 * @param registry
	 * @return
	 */

	List<IEvaluationProcess> instantiateEvaluationProcesses(IEvaluationProcessRegistry registry, IOptimisationData optimisationData);

	/**
	 * Return new instances of all {@link IEvaluationProcess}s the {@link IEvaluationProcessRegistry} knows about. The returned list will contain {@link IEvaluationProcess}s in the same order as the
	 * input list. Missing {@link IEvaluationProcess}s will be replaced with a null entry in the list.
	 * 
	 * @param registry
	 * @param constraintCheckerNames
	 * @return
	 */
	List<IEvaluationProcess> instantiateEvaluationProcesses(IEvaluationProcessRegistry registry, List<String> evaluationProcessNames, IOptimisationData optimisationData);

}
