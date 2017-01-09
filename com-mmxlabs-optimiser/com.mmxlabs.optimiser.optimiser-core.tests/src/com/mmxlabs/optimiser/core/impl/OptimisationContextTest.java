/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class OptimisationContextTest {

	@SuppressWarnings("null")
	@Test
	public void testOptimisationContext() {

		final IOptimisationData optimisationData = Mockito.mock(IOptimisationData.class);
		final ISequences initialSequences = Mockito.mock(ISequences.class);

		final List<@NonNull String> fitnessComponents = Collections.emptyList();
		final IFitnessFunctionRegistry fitnessFunctionRegistry = Mockito.mock(IFitnessFunctionRegistry.class);
		final List<@NonNull String> constraintCheckers = Collections.emptyList();
		final IConstraintCheckerRegistry constraintCheckerRegistry = Mockito.mock(IConstraintCheckerRegistry.class);
		final List<@NonNull String> evaluationProcesses = Collections.emptyList();
		final IEvaluationProcessRegistry evaluationProcessRegistry = Mockito.mock(IEvaluationProcessRegistry.class);
		final List<@NonNull String> evaluatedStateConstraintCheckers = Collections.emptyList();
		final IEvaluatedStateConstraintCheckerRegistry evaluatedStateConstraintCheckerRegistry = Mockito.mock(IEvaluatedStateConstraintCheckerRegistry.class);

		final OptimisationContext optContext = new OptimisationContext(optimisationData, initialSequences, fitnessComponents, fitnessFunctionRegistry, constraintCheckers, constraintCheckerRegistry,
				evaluationProcesses, evaluationProcessRegistry, evaluatedStateConstraintCheckers, evaluatedStateConstraintCheckerRegistry);

//		Assert.assertSame(optimisationData, optContext.getOptimisationData());
		Assert.assertSame(initialSequences, optContext.getInputSequences());
		Assert.assertSame(fitnessComponents, optContext.getFitnessComponents());
		Assert.assertSame(fitnessFunctionRegistry, optContext.getFitnessFunctionRegistry());
		Assert.assertSame(constraintCheckers, optContext.getConstraintCheckers());
		Assert.assertSame(constraintCheckerRegistry, optContext.getConstraintCheckerRegistry());
		Assert.assertSame(evaluationProcesses, optContext.getEvaluationProcesses());
		Assert.assertSame(evaluationProcessRegistry, optContext.getEvaluationProcessRegistry());
		Assert.assertSame(evaluatedStateConstraintCheckers, optContext.getEvaluatedStateConstraintCheckers());
		Assert.assertSame(evaluatedStateConstraintCheckerRegistry, optContext.getEvaluatedStateConstraintCheckerRegistry());
	}
}
