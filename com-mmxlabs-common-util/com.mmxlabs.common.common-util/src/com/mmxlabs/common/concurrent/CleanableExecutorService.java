package com.mmxlabs.common.concurrent;

import java.util.concurrent.ExecutorService;

public interface CleanableExecutorService extends ExecutorService {
	public void checkIfDone();
	public void clean();
}
