/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.optimiser.lso.impl;

import java.util.Random;
import java.util.concurrent.Callable;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;

import com.google.inject.Injector;
import com.mmxlabs.optimiser.common.components.ILookupManager;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.lso.IMoveGenerator;

/**
 * A class that could be passed into a CleanableExecutorService to attempt to
 * apply a move and evaluate an ISequences state.
 * 
 * @author AC
 *
 */
@NonNullByDefault
public final class LSOJob implements Callable<LSOJobState> {
	private final Random random;
	private final Injector injector;
	private final ILookupManager stateManager;
	private final ISequences rawSequences;
	private final IMoveGenerator moveGenerator;
	private final boolean failedInitialConstraintCheckers;
	private final long seed;

	public LSOJob(final Injector injector, final ISequences rawSequences, final ILookupManager stateManager, final IMoveGenerator moveGenerator, final long seed,
			final boolean failedInitialConstraintCheckers) {
		this(injector, rawSequences, stateManager, moveGenerator, new Random(seed), seed, failedInitialConstraintCheckers);
	}

	public LSOJob(final Injector injector, final ISequences rawSequences, final ILookupManager stateManager, final IMoveGenerator moveGenerator, final Random random, long seed,
			final boolean failedInitialConstraintCheckers) {
		this.injector = injector;
		this.rawSequences = rawSequences;
		this.stateManager = stateManager;
		this.moveGenerator = moveGenerator;
		this.random = random;
		this.seed = seed;
		this.failedInitialConstraintCheckers = failedInitialConstraintCheckers;
	}

	@Override
	public LSOJobState call() {
		try {
			final LSOMover mover = injector.getInstance(LSOMover.class);
			return mover.search(new ModifiableSequences(rawSequences), stateManager, random, moveGenerator, seed, failedInitialConstraintCheckers);
		} catch (final Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}