/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.optimiser.lso.parallellso;

import java.util.Random;
import java.util.concurrent.Callable;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * A class that could be passed into an ExecutorService to attempt to apply a move and evaluate an ISequences state.
 * 
 * @author AC
 *
 */
public final class MultiObjectiveOptimiserJob implements Callable<MultiObjectiveJobState> {
	private final long seed;
	private @NonNull final Injector injector;
	private @NonNull ILookupManager stateManager;
	private @NonNull ISequences rawSequences;
	private @NonNull IMoveGenerator moveGenerator;
	private boolean failedInitialConstraintCheckers;
	
	public MultiObjectiveOptimiserJob(@NonNull final Injector injector, @NonNull final ISequences rawSequences, @NonNull final ILookupManager stateManager, @NonNull IMoveGenerator moveGenerator, final long seed, final boolean failedInitialConstraintCheckers) {
		this.injector = injector;
		this.rawSequences = rawSequences;
		this.stateManager = stateManager;
		this.moveGenerator = moveGenerator;
		this.seed = seed;
		this.failedInitialConstraintCheckers = failedInitialConstraintCheckers;
	}

	@Override
	public MultiObjectiveJobState call() {
		try {
			final SimpleMultiObjectiveLSOMover mover = injector.getInstance(SimpleMultiObjectiveLSOMover.class);
			try {
				return mover.search(new ModifiableSequences(rawSequences), stateManager, new Random(seed), moveGenerator, seed, failedInitialConstraintCheckers);
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (final Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}