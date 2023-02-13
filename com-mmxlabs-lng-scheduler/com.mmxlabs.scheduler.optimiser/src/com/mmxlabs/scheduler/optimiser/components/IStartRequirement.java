/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.components;

import org.eclipse.jdt.annotation.Nullable;

public interface IStartRequirement extends IStartEndRequirement {

	@Nullable
	IHeelOptionSupplier getHeelOptions();
}
