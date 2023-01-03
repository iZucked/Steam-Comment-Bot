/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.annotation.NonNull;

public interface IScenarioValidationListener {

	void validationChanged(@NonNull ScenarioModelRecord modelRecord, @NonNull IStatus status);
}
