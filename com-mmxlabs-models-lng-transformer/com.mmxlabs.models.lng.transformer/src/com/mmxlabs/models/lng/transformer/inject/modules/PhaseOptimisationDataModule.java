/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.inject.modules;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mmxlabs.optimiser.common.scenario.PhaseOptimisationData;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

public class PhaseOptimisationDataModule extends AbstractModule {

	@Provides
	@Singleton
	public IPhaseOptimisationData provideOptimisationData(final Injector injector, @Named(OptimiserConstants.SEQUENCE_TYPE_INPUT) final ISequences initialSequences,
			final IOptimisationData optimisationData) {

		final PhaseOptimisationData phaseOptimisationData = injector.getInstance(PhaseOptimisationData.class);

		final List<@NonNull ISequence> sequences = initialSequences.getResources() //
				.stream() //
				.map(initialSequences::getSequence) //
				.collect(Collectors.toList());

		final List<ISequenceElement> l = new LinkedList<>();
		for (final ISequence sequence : sequences) {
			for (final ISequenceElement e : sequence) {
				l.add(e);
			}
		}
		l.addAll(initialSequences.getUnusedElements());

		Collections.sort(l, (a, b) -> Integer.compare(a.getIndex(), b.getIndex()));
		phaseOptimisationData.setSequenceElements(l);
		phaseOptimisationData.setResources(initialSequences.getResources());
		phaseOptimisationData.setConsideredAsOptionalElements(optimisationData.getConsideredAsOptionalElements());

		return phaseOptimisationData;
	}

	@Override
	protected void configure() {
		// Impl all in provider methods
	}

}
