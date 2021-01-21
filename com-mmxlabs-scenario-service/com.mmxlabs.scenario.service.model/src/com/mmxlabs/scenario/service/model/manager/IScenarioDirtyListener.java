/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.jdt.annotation.NonNull;

public interface IScenarioDirtyListener {

	void dirtyStatusChanged(@NonNull ModelRecord modelRecord, boolean isDirty);
}
