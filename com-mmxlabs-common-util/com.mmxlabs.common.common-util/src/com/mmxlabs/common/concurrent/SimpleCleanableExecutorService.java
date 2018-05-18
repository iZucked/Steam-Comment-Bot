package com.mmxlabs.common.concurrent;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleCleanableExecutorService implements CleanableExecutorService {
	ExecutorService delegate;
	List<Future> futures = new LinkedList<>();

	public SimpleCleanableExecutorService(ExecutorService delegate) {
		this.delegate = delegate;
	}

	@Override
	public void execute(Runnable command) {
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
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return delegate.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return storeAndReturn(delegate.submit(task));
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return storeAndReturn(delegate.submit(task, result));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return storeAndReturn(delegate.submit(task));
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return delegate.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return delegate.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return delegate.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return delegate.invokeAny(tasks, timeout, unit);
	}

	private <T> Future<T> storeAndReturn(Future<T> f) {
		futures.add(f);
		return f;
	}

	@Override
	public void checkIfDone() {
		for (Future future : futures) {
			if (!future.isDone()) {
				System.out.println("not done!");
			}
		}
	}

	@Override
	public void clean() {
		Iterator<Future> iterator = futures.iterator();
		while (iterator.hasNext()) {
			Future future = iterator.next();
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}  catch (CancellationException e) {
				e.printStackTrace();
			}
			iterator.remove();
		}
	}
}
