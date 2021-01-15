/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.concurrent.Callable;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.core.IProgressReporter;

public final class ActionableSetJob implements Callable<ActionableSetJobState> {
	private final long seed;
	private final @NonNull Injector injector;
	private final @NonNull ActionableSetJobState baseState;
	private final IProgressReporter progressReporter;

	public ActionableSetJob(@NonNull final Injector injector, @NonNull final ActionableSetJobState baseState, final long seed, final IProgressReporter progressReporter) {
		this.injector = injector;
		this.baseState = baseState;
		this.seed = seed;
		this.progressReporter = progressReporter;
	}

	@Override
	public ActionableSetJobState call() {
		try {
			final ActionableSetMover mover = injector.getInstance(ActionableSetMover.class);
			return mover.search(baseState, seed);
		} catch (final Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			progressReporter.report(1);
		}
	}
}