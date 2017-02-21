/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionableset;

import java.util.concurrent.Callable;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IProgressReporter;
import com.mmxlabs.optimiser.core.ISequences;

public final class ActionableSetJob implements Callable<ActionableSetJobState> {
	private final long seed;
	private @NonNull final Injector injector;
	private @NonNull final ActionableSetJobState baseState;
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
			try {
				final ISequences rawSequences = baseState.getRawSequences();
				final ILookupManager lookupManager = injector.getInstance(ILookupManager.class);
				lookupManager.createLookup(rawSequences);
				return mover.search(baseState, lookupManager, seed);
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (final Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			progressReporter.report(1);
		}
	}
}