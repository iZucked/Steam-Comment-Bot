/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleCleanableExecutorService implements CleanableExecutorService {
	private final ExecutorService delegate;
	private final List<Future<?>> futures = new LinkedList<>();
	private int threads;

	public SimpleCleanableExecutorService(final ExecutorService delegate, int threads) {
		this.delegate = delegate;
		this.threads = threads;
	}

	@Override
	public void execute(final Runnable command) {
		delegate.execute(command);
	}

	@Override
	public void shutdown() {
		delegate.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return delegate.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return delegate.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return delegate.isTerminated();
	}

	@Override
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		return delegate.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(final Callable<T> task) {
		return storeAndReturn(delegate.submit(task));
	}

	@Override
	public <T> Future<T> submit(final Runnable task, final T result) {
		return storeAndReturn(delegate.submit(task, result));
	}

	@Override
	public Future<?> submit(final Runnable task) {
		return storeAndReturn(delegate.submit(task));
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return delegate.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException {
		return delegate.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return delegate.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return delegate.invokeAny(tasks, timeout, unit);
	}

	private <T> Future<T> storeAndReturn(final Future<T> f) {
		futures.add(f);
		return f;
	}

	@Override
	public void checkIfDone() {
		for (final Future<?> future : futures) {
			if (!future.isDone()) {
				System.out.println("not done!");
			}
		}
	}

	@Override
	public void clean() {
		final Iterator<Future<?>> iterator = futures.iterator();
		while (iterator.hasNext()) {
			final Future<?> future = iterator.next();
			try {
				future.get();
			} catch (final Exception e) {
				// We don't care about exceptions here, we are just trying to ensure all jobs have finished
			}
			iterator.remove();
		}
	}

	@Override
	public void removeCompleted() {
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
