/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.optimiser.core.impl;

import java.util.Collections;
import java.util.List;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessFunctionRegistry;
import com.mmxlabs.optimiser.core.impl.OptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

@RunWith(JMock.class)
public class OptimisationContextTest {

	Mockery context = new JUnit4Mockery();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testOptimisationContext() {

		final IOptimisationData optimisationData = context
				.mock(IOptimisationData.class);
		final ISequences initialSequences = context.mock(ISequences.class);

		final List<IFitnessComponent> fitnessComponents = Collections
				.emptyList();
		final IFitnessFunctionRegistry fitnessFunctionRegistry = context
				.mock(IFitnessFunctionRegistry.class);
		final List<IConstraintChecker> constraintCheckers = Collections
				.emptyList();
		final IConstraintCheckerRegistry constraintCheckerRegistry = context
				.mock(IConstraintCheckerRegistry.class);

		final OptimisationContext optContext = new OptimisationContext(
				optimisationData, initialSequences, fitnessComponents,
				fitnessFunctionRegistry, constraintCheckers,
				constraintCheckerRegistry);

		Assert.assertSame(optimisationData, optContext.getOptimisationData());
		Assert.assertSame(initialSequences, optContext.getInitialSequences());
		Assert.assertSame(fitnessComponents, optContext.getFitnessComponents());
		Assert.assertSame(fitnessFunctionRegistry, optContext
				.getFitnessFunctionRegistry());
		Assert.assertSame(constraintCheckers, optContext
				.getConstraintCheckers());
		Assert.assertSame(constraintCheckerRegistry, optContext
				.getConstraintCheckerRegistry());
	}
}
