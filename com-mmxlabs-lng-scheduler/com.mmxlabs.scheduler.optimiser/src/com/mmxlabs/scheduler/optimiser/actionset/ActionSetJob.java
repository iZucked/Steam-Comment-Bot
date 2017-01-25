/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.actionset;

import java.util.concurrent.Callable;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.IProgressReporter;
import com.mmxlabs.optimiser.core.ISequences;

/**
 * A class that could be passed into an ExecutorService to attempt to apply a move and evaluate an ISequences state.
 * 
 * @author AC
 *
 */
public final class ActionSetJob implements Callable<ActionSetJobState> {
	private final long seed;
	private @NonNull final Injector injector;
	// private @NonNull ILookupManager lookupManager;
	private @NonNull final ActionSetJobState baseState;
	// private @NonNull IMoveGenerator moveGenerator;
	// private boolean failedInitialConstraintCheckers;
	private IProgressReporter progressReporter;

	public ActionSetJob(@NonNull final Injector injector, @NonNull final ActionSetJobState baseState, final long seed, IProgressReporter progressReporter) {
		this.injector = injector;
		this.baseState = baseState;
		// this.lookupManager = new;
		this.seed = seed;
		this.progressReporter = progressReporter;
	}

	@Override
	public ActionSetJobState call() {
		try {
			final ActionSetMover mover = injector.getInstance(ActionSetMover.class);
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