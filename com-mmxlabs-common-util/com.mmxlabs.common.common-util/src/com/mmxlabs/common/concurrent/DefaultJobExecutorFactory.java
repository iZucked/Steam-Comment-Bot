/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.common.concurrent;

import java.util.function.Supplier;

public class DefaultJobExecutorFactory implements JobExecutorFactory {
	private int numThreads;
	private Supplier<AutoCloseable> action;

	private Thread currentThead;

	public DefaultJobExecutorFactory(int numThreads) {
		this(numThreads, null);
	}

	public DefaultJobExecutorFactory(int numThreads, Supplier<AutoCloseable> action) {
		this(Thread.currentThread(), numThreads, action);
	}

	public DefaultJobExecutorFactory(Thread currentThead, int numThreads, Supplier<AutoCloseable> action) {
		this.numThreads = numThreads;
		this.action = action;
		this.currentThead = currentThead;
	}

	@Override
	public JobExecutorFactory withDefaultBegin(Supplier<AutoCloseable> action) {
		return new DefaultJobExecutorFactory(currentThead, getNumThreads(), action);
	}

	@Override
	public JobExecutor begin(Supplier<AutoCloseable> action) {
		return new JobExecutor(currentThead, getNumThreads(), action);
	}

	@Override
	public JobExecutor begin() {
		return new JobExecutor(currentThead, getNumThreads(), action);
	}

	public int getNumThreads() {
		return numThreads;
	}
}
