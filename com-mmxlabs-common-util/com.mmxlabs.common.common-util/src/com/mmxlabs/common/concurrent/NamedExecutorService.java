/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class NamedExecutorService {
	private NamedExecutorService() {

	}

	public static ThreadPoolExecutor createFixedPool(final int nThreads) {
		final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		final String prefix = threadGroup == null ? "Opt" : threadGroup.getName();

		return createFixedPool(prefix, nThreads);
	}

	public static ThreadPoolExecutor createFixedPool(final String name, final int nThreads) {
		// Replace the normal fixed executor pool with thread groups and custom naming
		final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		final String prefix = name + "-pool-";

		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
			private final AtomicInteger threadNumber = new AtomicInteger(1);

			@Override
			public Thread newThread(final Runnable r) {
				final Thread t = new Thread(threadGroup, r, prefix + threadNumber.getAndIncrement(), 0);
				if (t.isDaemon()) {
					t.setDaemon(false);
				}
				if (t.getPriority() != Thread.NORM_PRIORITY) {
					t.setPriority(Thread.NORM_PRIORITY);
				}
				return t;
			}
		}, new RejectedExecutionHandler() {

			@Override
			public void rejectedExecution(final Runnable r, final ThreadPoolExecutor e) {
				throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
			}
		});
	}
}
