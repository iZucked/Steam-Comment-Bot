/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.function.Supplier;

import org.eclipse.jdt.annotation.Nullable;

public interface JobExecutorFactory {

	interface Hook {
		void beginAction();

		void doneAction();

	}

	int getNumThreads();

	default JobExecutor begin() {
		return begin(null);
	}

	JobExecutorFactory withDefaultBegin(Supplier<AutoCloseable> action);

	JobExecutor begin(@Nullable Supplier<AutoCloseable> action);

}
