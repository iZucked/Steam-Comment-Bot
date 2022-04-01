/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.ui;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public interface IProgressProvider {

	enum RunType {
		Local, Cloud
	}
	
	public interface IProgressChanged {
		void changed(Object element);
	}

	@Nullable Pair<Double, RunType> getProgress(ScenarioInstance scenarioInstance, ScenarioFragment fragment);

	void removeChangedListener(IProgressChanged listener);

	void addChangedListener(IProgressChanged listener);
}
