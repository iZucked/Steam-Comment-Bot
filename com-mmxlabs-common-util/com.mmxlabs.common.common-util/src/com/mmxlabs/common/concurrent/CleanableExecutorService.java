/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.concurrent.ExecutorService;

public interface CleanableExecutorService extends ExecutorService {
	public void checkIfDone();
	public void clean();
	void removeCompleted();
}
