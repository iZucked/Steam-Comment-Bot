/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.jdt.annotation.NonNull;

@FunctionalInterface
public interface IPostChangeHook {
	void changed(@NonNull ScenarioModelRecord modelRecord);

}
