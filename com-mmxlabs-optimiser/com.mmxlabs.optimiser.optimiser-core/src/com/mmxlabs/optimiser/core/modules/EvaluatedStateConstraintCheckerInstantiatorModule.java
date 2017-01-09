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
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintCheckerRegistry;
import com.mmxlabs.optimiser.core.constraints.impl.EvaluatedStateConstraintCheckerInstantiator;

public class EvaluatedStateConstraintCheckerInstantiatorModule extends AbstractModule {

	public static final String ENABLED_EVALUATED_STATE_CONSTRAINT_NAMES = "EnabledEvaluatedStateConstraintNames";

	@Override
	protected void configure() {

	}

	@Provides
	private List<@NonNull IEvaluatedStateConstraintChecker> provideConstraintCheckers(@NonNull final Injector injector,
			@NonNull final IEvaluatedStateConstraintCheckerRegistry constraintCheckerRegistry,
			@NonNull @Named(ENABLED_EVALUATED_STATE_CONSTRAINT_NAMES) final List<@NonNull String> enabledConstraintNames) {
		final EvaluatedStateConstraintCheckerInstantiator instantiator = new EvaluatedStateConstraintCheckerInstantiator();
		final List<IEvaluatedStateConstraintChecker> constraintCheckers = instantiator.instantiateConstraintCheckers(constraintCheckerRegistry, enabledConstraintNames);

		final List<@NonNull IEvaluatedStateConstraintChecker> result = new ArrayList<>(constraintCheckers.size());
		for (final IEvaluatedStateConstraintChecker checker : constraintCheckers) {
			if (checker != null) {
				result.add(checker);
				injector.injectMembers(checker);
			}
		}
		return result;
	}
}
