/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.ActionableSetPlan;
import com.mmxlabs.models.lng.analytics.OptimisationResult;
import com.mmxlabs.models.lng.analytics.SlotInsertionOptions;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.util.IScenarioFragmentOpenHandler;

public class AnalyticsSolutionAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {
		if (adaptableObject instanceof SlotInsertionOptions) {
			return adapterType.cast(openSlotInsertionOptions());
		} else if (adaptableObject instanceof AbstractSolutionSet) {
			return adapterType.cast(openMultipleSolution());
		} else if (adaptableObject instanceof OptimisationResult) {
			return adapterType.cast(openOptimisation());
		}
		return adapterType.cast(null);
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IScenarioFragmentOpenHandler.class };
	}

	private IScenarioFragmentOpenHandler openSlotInsertionOptions() {
		return new IScenarioFragmentOpenHandler() {

			@Override
			public void open(@NonNull final ScenarioFragment fragment, @NonNull final ScenarioInstance scenarioInstance) {
				final Object object = fragment.getFragment();
				if (object instanceof SlotInsertionOptions) {
					final SlotInsertionOptions slotInsertionOptions = (SlotInsertionOptions) object;
					final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, slotInsertionOptions, slotInsertionOptions.getName());
					data.open();
				}
			}
		};
	}

	private IScenarioFragmentOpenHandler openActionableSetPlan() {
		return new IScenarioFragmentOpenHandler() {

			@Override
			public void open(@NonNull final ScenarioFragment fragment, @NonNull final ScenarioInstance scenarioInstance) {
				final Object object = fragment.getFragment();
				if (object instanceof ActionableSetPlan) {
					final ActionableSetPlan actionableSetPlan = (ActionableSetPlan) object;
					final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, actionableSetPlan, actionableSetPlan.getName());
					data.open();
				}
			}
		};
	}

	private IScenarioFragmentOpenHandler openOptimisation() {
		return new IScenarioFragmentOpenHandler() {

			@Override
			public void open(@NonNull final ScenarioFragment fragment, @NonNull final ScenarioInstance scenarioInstance) {
				final Object object = fragment.getFragment();
				if (object instanceof OptimisationResult) {
					final OptimisationResult actionableSetPlan = (OptimisationResult) object;
					final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, actionableSetPlan, actionableSetPlan.getName());
					data.open();

				}
			}
		};
	}

	private IScenarioFragmentOpenHandler openMultipleSolution() {

		return new IScenarioFragmentOpenHandler() {

			@Override
			public void open(@NonNull final ScenarioFragment fragment, @NonNull final ScenarioInstance scenarioInstance) {
				final Object object = fragment.getFragment();
				if (object instanceof AbstractSolutionSet) {
					final AbstractSolutionSet solutionSet = (AbstractSolutionSet) object;
					final AnalyticsSolution data = new AnalyticsSolution(scenarioInstance, solutionSet, solutionSet.getName());
					data.open();
				}
			}
		};
	}
}
