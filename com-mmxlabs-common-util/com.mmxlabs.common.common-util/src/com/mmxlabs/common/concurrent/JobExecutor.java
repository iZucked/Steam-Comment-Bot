/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class JobExecutor implements AutoCloseable {
	private ExecutorService delegate;
	private final List<Future<?>> futures = new LinkedList<>();
	private int threads;

	private boolean inScope;

	public JobExecutor(Thread parentThread, int threads, Supplier<AutoCloseable> action) {
		this.delegate = NamedExecutorService.createFixedPool(parentThread, threads, action);
		inScope = true;
	}

	@Override
	public void close() {
		if (delegate != null) {
			// Wait for task list to complete.
			delegate.shutdownNow();
			try {
				delegate.awaitTermination(1, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public synchronized void waitUntilComplete(long timeout, TimeUnit unit) {
		if (!inScope) {
			throw new IllegalStateException("Not called begin");
		}
		// Wait for task list to complete.
		delegate.shutdown();

		try {
			delegate.awaitTermination(timeout, unit);

			inScope = false;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	public synchronized void waitUntilComplete() {
		waitUntilComplete(1, TimeUnit.DAYS);
	}

	public <T> Future<T> submit(final Callable<T> task) {
		return storeAndReturn(delegate.submit(task));
	}

	public Future<?> submit(final Runnable task) {
		return storeAndReturn(delegate.submit(task));
	}

	private synchronized <T> Future<T> storeAndReturn(final Future<T> f) {

		if (!inScope) {
			throw new IllegalStateException("Not called begin");
		}

		futures.add(f);
		return f;
	}

	public synchronized void removeCompleted() {

		if (!inScope) {
			throw new IllegalStateException("Not called begin");
		}

		final Iterator<Future<?>> iterator = futures.iterator();
		while (iterator.hasNext()) {
			final Future<?> future = iterator.next();
			if (future.isDone()) {
				iterator.remove();
			}
		}
	}

	public int getNumThreads() {
		return threads;
	}

}
