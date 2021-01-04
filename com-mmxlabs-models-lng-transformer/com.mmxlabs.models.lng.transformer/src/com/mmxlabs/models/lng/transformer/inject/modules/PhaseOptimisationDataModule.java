/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.transformer.IncompleteScenarioException;
import com.mmxlabs.optimiser.common.scenario.PhaseOptimisationData;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;

public class PhaseOptimisationDataModule extends AbstractModule {

	@Provides
	@Singleton
	public IPhaseOptimisationData provideOptimisationData(Injector injector, @Named(OptimiserConstants.SEQUENCE_TYPE_INPUT) ISequences initialSequences) {
		final PhaseOptimisationData phaseOptimisationData = injector.getInstance(PhaseOptimisationData.class);
		List<@NonNull ISequence> sequences = initialSequences.getResources().stream().map(initialSequences::getSequence).collect(Collectors.toList());
		List<ISequenceElement> l = new LinkedList<>();
		for (ISequence sequence : sequences) {
			for (ISequenceElement e : sequence) {
				l.add(e);
			}
		}
		l.addAll(initialSequences.getUnusedElements());
		Collections.sort(l, (a, b) -> Integer.compare(a.getIndex(), b.getIndex()));
		phaseOptimisationData.setSequenceElements(l);
		phaseOptimisationData.setResources(initialSequences.getResources());
		return phaseOptimisationData;
	}

	@Override
	protected void configure() {

	}

}
