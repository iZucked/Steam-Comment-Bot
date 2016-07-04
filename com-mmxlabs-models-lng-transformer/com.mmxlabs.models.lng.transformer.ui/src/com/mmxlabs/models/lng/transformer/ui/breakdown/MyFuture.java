/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.breakdown;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyFuture implements Future<Collection<JobState>>

{

	private final ChangeSetFinderJob job;

	public MyFuture(final ChangeSetFinderJob job) {
		this.job = job;

	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Collection<JobState> get() throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		return job.call();
	}

	@Override
	public Collection<JobState> get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}