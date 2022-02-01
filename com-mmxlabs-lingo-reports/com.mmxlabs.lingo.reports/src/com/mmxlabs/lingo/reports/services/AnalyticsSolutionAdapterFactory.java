/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.ui.utils.AnalyticsSolution;
import com.mmxlabs.scenario.service.model.util.IScenarioFragmentOpenHandler;

public class AnalyticsSolutionAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {
		if (adaptableObject instanceof AbstractSolutionSet) {
			return adapterType.cast(openSolution());
		}
		return adapterType.cast(null);
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IScenarioFragmentOpenHandler.class };
	}

	private IScenarioFragmentOpenHandler openSolution() {
		return (fragment, scenarioInstance) -> {
			final Object object = fragment.getFragment();
			if (object instanceof AbstractSolutionSet solutionSet) {
				new AnalyticsSolution(scenarioInstance, solutionSet, solutionSet.getName()).openAndSwitchScreen();
			}
		};
	}
}
