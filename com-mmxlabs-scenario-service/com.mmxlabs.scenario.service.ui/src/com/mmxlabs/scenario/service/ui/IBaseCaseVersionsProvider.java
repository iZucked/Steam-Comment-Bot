/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import org.eclipse.jdt.annotation.Nullable;

public interface IBaseCaseVersionsProvider {

	@FunctionalInterface
	interface IBaseCaseChanged {
		void changed();
	}

	@Nullable
	String getVersion(String typeId);

	@Nullable
	ScenarioInstance getBaseCase();

	void addChangedListener(IBaseCaseChanged listener);

	void removeChangedListener(IBaseCaseChanged listener);
}
