package com.mmxlabs.scheduler.optimiser.providers;

import org.eclipse.jdt.annotation.NonNull;

public interface ILazyExpressionManagerContainer {

	@NonNull
	ILazyExpressionManager getExpressionManager();
}
