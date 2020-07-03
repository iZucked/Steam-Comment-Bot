/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcessRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class OptimisationContextTest {

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

		final OptimisationContext optContext = new OptimisationContext(initialSequences, fitnessComponents, fitnessFunctionRegistry, constraintCheckers, constraintCheckerRegistry,
				evaluationProcesses, evaluationProcessRegistry, evaluatedStateConstraintCheckers, evaluatedStateConstraintCheckerRegistry);

//		Assertions.assertSame(optimisationData, optContext.getOptimisationData());
		Assertions.assertSame(initialSequences, optContext.getInputSequences());
		Assertions.assertSame(fitnessComponents, optContext.getFitnessComponents());
		Assertions.assertSame(fitnessFunctionRegistry, optContext.getFitnessFunctionRegistry());
		Assertions.assertSame(constraintCheckers, optContext.getConstraintCheckers());
		Assertions.assertSame(constraintCheckerRegistry, optContext.getConstraintCheckerRegistry());
		Assertions.assertSame(evaluationProcesses, optContext.getEvaluationProcesses());
		Assertions.assertSame(evaluationProcessRegistry, optContext.getEvaluationProcessRegistry());
		Assertions.assertSame(evaluatedStateConstraintCheckers, optContext.getEvaluatedStateConstraintCheckers());
		Assertions.assertSame(evaluatedStateConstraintCheckerRegistry, optContext.getEvaluatedStateConstraintCheckerRegistry());
	}
}
