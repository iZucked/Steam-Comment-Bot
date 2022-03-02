/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class NamedExecutorService {
	private NamedExecutorService() {

	}

	public static ThreadPoolExecutor createFixedPool(Thread parentThread, final int nThreads, Supplier<AutoCloseable> action) {
		final ThreadGroup threadGroup = parentThread.getThreadGroup();
		final String prefix = threadGroup == null ? "Opt" : threadGroup.getName();

		return createFixedPool(parentThread, prefix, nThreads, action);
	}

	public static ThreadPoolExecutor createFixedPool(Thread parentThread, final String name, final int nThreads, Supplier<AutoCloseable> action) {
		// Replace the normal fixed executor pool with thread groups and custom naming
		final ThreadGroup threadGroup = parentThread.getThreadGroup();
		final String prefix = name + "-pool-";

		return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
			private final AtomicInteger threadNumber = new AtomicInteger(1);

			@Override
			public Thread newThread(final Runnable r) {
				final Thread t = new Thread(threadGroup, r, prefix + threadNumber.getAndIncrement(), 0) {
					@Override
					public void run() {
						if (action != null) {
							try (AutoCloseable c = action.get()) {
								super.run();
							} catch (Exception e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						} else {
							super.run();
						}
					}
				};
				if (t.isDaemon()) {
					t.setDaemon(false);
				}
				if (t.getPriority() != Thread.NORM_PRIORITY) {
					t.setPriority(Thread.NORM_PRIORITY);
				}
				return t;
			}
		}, (r, e) -> {
			throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
		});
	}
}
