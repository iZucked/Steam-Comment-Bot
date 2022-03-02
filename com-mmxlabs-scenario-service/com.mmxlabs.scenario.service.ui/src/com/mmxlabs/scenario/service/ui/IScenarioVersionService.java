/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IScenarioVersionService {

	@FunctionalInterface
	public interface IChangedListener {
		void changed();
	}

	boolean differentToBaseCase(ScenarioInstance instance);

	void addChangedListener(IChangedListener l);

	void removeChangedListener(IChangedListener l);
}
