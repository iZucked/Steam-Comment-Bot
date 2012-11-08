package com.mmxlabs.scheduler.optimiser.manipulators;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.impl.ChainedSequencesManipulator;

/**
 * @since 2.0
 */
public class SequencesManipulatorModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(StartLocationRemovingSequenceManipulator.class);
		bind(EndLocationSequenceManipulator.class);
	}

	/**
	 * Re-export the {@link ChainedSequencesManipulator} as an {@link ISequencesManipulator}
	 * 
	 * @param manipulator
	 * @return
	 */
	@Provides
	@Singleton
	private ISequencesManipulator provideSequencesManipulator(final ChainedSequencesManipulator manipulator) {
		return manipulator;

	}

	@Provides
	@Singleton
	private ChainedSequencesManipulator provideChainedSequencesManipulator(final Injector injector, final StartLocationRemovingSequenceManipulator startLocationRemovingSequenceManipulator,
			final EndLocationSequenceManipulator endLocationSequenceManipulator) {
		/**
		 * A chained manipulator, because we need several manipulators
		 */
		final ChainedSequencesManipulator chainedManipulator = new ChainedSequencesManipulator();
		injector.injectMembers(chainedManipulator);
		chainedManipulator.addDelegate(startLocationRemovingSequenceManipulator);
		chainedManipulator.addDelegate(endLocationSequenceManipulator);

		return chainedManipulator;

	}
}
