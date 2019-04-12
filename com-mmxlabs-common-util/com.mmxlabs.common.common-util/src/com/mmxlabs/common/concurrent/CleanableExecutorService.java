/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.concurrent.ExecutorService;

public interface CleanableExecutorService extends ExecutorService {
	int getNumThreads();

	void checkIfDone();

	public void clean();

	void removeCompleted();
}
