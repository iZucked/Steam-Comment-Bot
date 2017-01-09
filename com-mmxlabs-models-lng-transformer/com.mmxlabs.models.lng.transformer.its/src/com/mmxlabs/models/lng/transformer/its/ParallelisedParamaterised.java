/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.runners.Parameterized;
import org.junit.runners.model.RunnerScheduler;

/**
 * 
 * A subclass of the {@link Parameterized} runner to execute tests in parallel. This will parallelise the list of parameters (not the list of @Test's). Annotate a test class with
 * 
 * <pre>
 * &#64;RunWith(value = ParallelisedParamaterised.class)
 * </pre>
 * 
 * @see http://stackoverflow.com/questions/10141648/concurrent-junit-tests-with-parameters
 *
 */
public class ParallelisedParamaterised extends Parameterized {
	public ParallelisedParamaterised(final Class<?> klass) throws Throwable {
		super(klass);
		setScheduler(new ThreadPoolScheduler());
	}

	private static class ThreadPoolScheduler implements RunnerScheduler {

		private final ExecutorService executor;

		public ThreadPoolScheduler() {
			// TODO: Link to a System property
			final int threads = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
			executor = Executors.newFixedThreadPool(threads);
		}

		@Override
		public void schedule(final Runnable childStatement) {
			executor.submit(childStatement);
		}

		@Override
		public void finished() {
			try {
				executor.shutdown();
				// This needs to be long enough to cover *all* test execution, otherwise it will abort early
				executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

			} catch (final InterruptedException e) {
				e.printStackTrace(System.err);
				throw new RuntimeException(e);
			}
		}
	}

}
