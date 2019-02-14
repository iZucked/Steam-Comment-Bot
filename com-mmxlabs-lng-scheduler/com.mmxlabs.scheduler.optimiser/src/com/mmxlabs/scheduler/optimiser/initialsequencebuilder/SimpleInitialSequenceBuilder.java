/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.providers.IAlternativeElementProvider;

public class SimpleInitialSequenceBuilder implements IInitialSequenceBuilder {

	private static final Logger log = LoggerFactory.getLogger(SimpleInitialSequenceBuilder.class);

	@Inject
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private IAlternativeElementProvider alternativeElementProvider;

	@Override
	public ISequences createInitialSequences(final IPhaseOptimisationData data, final ISequences suggestion, final Map<ISequenceElement, IResource> resourceSuggestion,
			final Map<ISequenceElement, ISequenceElement> pairingHints) {
		if (suggestion == null) {
			throw new IllegalArgumentException("Suggested sequence is required");
		}

		// Get the set of all optional elements...
		final Collection<@NonNull ISequenceElement> optionalElements = new LinkedHashSet<>(phaseOptimisationData.getOptionalElements());

		// ...remove elements specified in the paringHints as these will be used and should not be considered optional for the builder
		optionalElements.removeAll(pairingHints.keySet());
		optionalElements.removeAll(pairingHints.values());

		// Add all elements to the unsequenced set.
		final Set<ISequenceElement> unsequencedElements = new LinkedHashSet<>();
		unsequencedElements.addAll(data.getSequenceElements());
		unsequencedElements.removeAll(alternativeElementProvider.getAllAlternativeElements());

		// Remove elements in the initial suggestion from the unsequenced set
		for (final ISequence seq : suggestion.getSequences().values()) {
			for (final ISequenceElement element : seq) {
				if (!unsequencedElements.contains(element)) {
					log.warn("Element {} is already sequenced", element);
				}
				unsequencedElements.remove(element);
			}
		}

		// Determine the set of elements remaining that will not be sequenced.
		final List<@NonNull ISequenceElement> unusedElements = new ArrayList<>();
		unusedElements.addAll(optionalElements);
		unusedElements.retainAll(unsequencedElements);

		// Remove the optional elements from further consideration by the builder
		unsequencedElements.removeAll(optionalElements);

		final IModifiableSequences result = new ModifiableSequences(suggestion);

		result.getModifiableUnusedElements().clear();
		result.getModifiableUnusedElements().addAll(unusedElements);

		return result;
	}

}
