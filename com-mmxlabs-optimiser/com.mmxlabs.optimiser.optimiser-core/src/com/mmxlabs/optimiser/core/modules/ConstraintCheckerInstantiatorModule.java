/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.impl.ConstraintCheckerInstantiator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

public class ConstraintCheckerInstantiatorModule extends AbstractModule {

	public static final String ENABLED_CONSTRAINT_NAMES = "EnabledConstraintNames";

	@Override
	protected void configure() {

	}

	@Provides
	private List<IConstraintChecker> provideConstraintCheckers(@NonNull final Injector injector, @NonNull final IConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull @Named(ENABLED_CONSTRAINT_NAMES) final List<String> enabledConstraintNames, @NonNull final IOptimisationData optimisationData) {
		final ConstraintCheckerInstantiator constraintCheckerInstantiator = new ConstraintCheckerInstantiator();
		final List<IConstraintChecker> constraintCheckers = constraintCheckerInstantiator.instantiateConstraintCheckers(constraintCheckerRegistry, enabledConstraintNames, optimisationData);

		final List<IConstraintChecker> result = new ArrayList<IConstraintChecker>(constraintCheckers.size());
		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker != null) {
				result.add(checker);
				injector.injectMembers(checker);
			}
		}
		return result;
	}

	@Provides
	private List<IPairwiseConstraintChecker> providePairwiseConstraintCheckers(final List<IConstraintChecker> contraintCheckers) {
		final ArrayList<IPairwiseConstraintChecker> pairwiseCheckers = new ArrayList<IPairwiseConstraintChecker>();
		for (final IConstraintChecker checker : contraintCheckers) {
			if (checker instanceof IPairwiseConstraintChecker) {
				pairwiseCheckers.add((IPairwiseConstraintChecker) checker);
			}
		}

		return pairwiseCheckers;
	}
}
