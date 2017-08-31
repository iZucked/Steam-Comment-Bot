/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.manager;

import org.eclipse.jdt.annotation.NonNull;

public interface IScenarioLockListener {

	void lockStateChanged(@NonNull ModelRecord modelRecord, boolean writeLocked);
}
