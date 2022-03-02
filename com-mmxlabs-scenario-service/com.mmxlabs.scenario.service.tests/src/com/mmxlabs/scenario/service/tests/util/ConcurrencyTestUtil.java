/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.tests.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;

public class ConcurrencyTestUtil {

	/**
	 * https://github.com/junit-team/junit4/wiki/Multithreaded-code-and-concurrency
	 * 
	 * @param message
	 * @param runnables
	 * @param maxTimeoutSeconds
	 * @throws InterruptedException
	 */
	public static void assertConcurrent(final String message, final List<? extends Runnable> runnables, final int maxTimeoutSeconds) throws InterruptedException {
		final int numThreads = runnables.size();
		final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<Throwable>());
		final ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
		try {
			final CountDownLatch allExecutorThreadsReady = new CountDownLatch(numThreads);
			final CountDownLatch afterInitBlocker = new CountDownLatch(1);
			final CountDownLatch allDone = new CountDownLatch(numThreads);
			for (final Runnable submittedTestRunnable : runnables) {
				threadPool.submit(new Runnable() {
					@Override
					public void run() {
						allExecutorThreadsReady.countDown();
						try {
							afterInitBlocker.await();
							submittedTestRunnable.run();
						} catch (final Throwable e) {
							exceptions.add(e);
						} finally {
							allDone.countDown();
						}
					}
				});
			}
			// wait until all threads are ready
			Assertions.assertTrue(allExecutorThreadsReady.await(runnables.size() * 10, TimeUnit.MILLISECONDS),
					"Timeout initializing threads! Perform long lasting initializations before passing runnables to assertConcurrent");
			// start all test runners
			afterInitBlocker.countDown();
			Assertions.assertTrue(allDone.await(maxTimeoutSeconds, TimeUnit.SECONDS), message + " timeout! More than" + maxTimeoutSeconds + "seconds");
		} finally {
			threadPool.shutdownNow();
		}

		// Dump any exceptions to console
		if (exceptions.isEmpty() == false) {
			exceptions.forEach(e -> e.printStackTrace());
		}
		Assertions.assertTrue(exceptions.isEmpty(), message + "failed with exception(s)" + exceptions);

	}
}
