/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;
import com.mmxlabs.scenario.service.model.util.IScenarioFragmentOpenHandler;

public class ValueMatrixScenarioAdapterFactory implements IAdapterFactory {

	@Override
	public <T> T getAdapter(Object adaptableObject, Class<T> adapterType) {
		if (adaptableObject instanceof SwapValueMatrixModel) {
			return adapterType.cast(openValueMatrixScenario());
		}
		return adapterType.cast(null);
	}

	@Override
	public Class<?>[] getAdapterList() {
		return new Class[] { IScenarioFragmentOpenHandler.class };
	}

	private IScenarioFragmentOpenHandler openValueMatrixScenario() {
		return (fragment, scenarioInstance) -> {
			if (fragment.getFragment() instanceof @NonNull final SwapValueMatrixModel model) {
				ValueMatrixScenario.open(scenarioInstance, model);
			}
		};
	}
}
