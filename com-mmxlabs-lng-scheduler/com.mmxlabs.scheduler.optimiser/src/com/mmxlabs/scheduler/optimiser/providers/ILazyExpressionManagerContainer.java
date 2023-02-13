/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

public interface ILazyExpressionManagerContainer {

	@NonNull
	ILazyExpressionManager getExpressionManager();
}
