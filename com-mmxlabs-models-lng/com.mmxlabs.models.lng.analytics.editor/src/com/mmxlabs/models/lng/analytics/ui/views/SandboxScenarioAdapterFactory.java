/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views;

import org.eclipse.core.runtime.IAdapterFactory;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.scenario.service.model.util.IScenarioFragmentOpenHandler;

public class SandboxScenarioAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(final Object adaptableObject, final Class<T> adapterType) {
		if (adaptableObject instanceof OptionAnalysisModel) {
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
			if (object instanceof OptionAnalysisModel) {
				final OptionAnalysisModel model = (OptionAnalysisModel) object;
				SandboxScenario.open(scenarioInstance, model);
			}
		};
	}
}
