/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;

/**
 * A {@link Module} implementation intended for use in a single optimisation/evaluation process. Data structures are singletons. A new {@link Module} instance should be created for each process.
 * 
 */
public class SequencesManipulatorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(StartLocationRemovingSequenceManipulator.class);
		bind(EndLocationSequenceManipulator.class);
		bind(ShortCargoSequenceManipulator.class);
	}

	/**
	 * Re-export the {@link ChainedSequencesManipulator} as an {@link ISequencesManipulator}
	 * 
	 * @param manipulator
	 * @return
	 */
	@Provides
//	@Singleton
	private ISequencesManipulator provideSequencesManipulator(final ChainedSequencesManipulator manipulator) {
		return manipulator;

	}

	@Provides
//	@Singleton
	private ChainedSequencesManipulator provideChainedSequencesManipulator(final Injector injector, final StartLocationRemovingSequenceManipulator startLocationRemovingSequenceManipulator,
			final EndLocationSequenceManipulator endLocationSequenceManipulator, ShortCargoSequenceManipulator shortCargoSequenceManipulator) {
		/**
		 * A chained manipulator, because we need several manipulators
		 */
		final ChainedSequencesManipulator chainedManipulator = new ChainedSequencesManipulator();
		injector.injectMembers(chainedManipulator);
		chainedManipulator.addDelegate(startLocationRemovingSequenceManipulator);
		chainedManipulator.addDelegate(endLocationSequenceManipulator);
		chainedManipulator.addDelegate(shortCargoSequenceManipulator);

		return chainedManipulator;

	}
}
