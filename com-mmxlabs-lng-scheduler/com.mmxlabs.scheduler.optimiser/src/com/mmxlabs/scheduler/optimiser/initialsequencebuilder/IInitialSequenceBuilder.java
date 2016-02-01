/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.initialsequencebuilder;

import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * Implementations should be able to create some sensible initial sequences from a given {@link IOptimisationData}. In future, the interface should be extended to include "advice" from the scenario,
 * so that the schedule can be partially specified already.
 * 
 * @author hinton
 * 
 */
public interface IInitialSequenceBuilder {
	/**
	 * Create some initial sequences
	 * 
	 * @param data
	 *            the optimisation data
	 * @param sequenceSuggestion
	 *            a suggested (possibly incomplete) start state. May be null
	 * @param resourceSuggestion
	 *            a suggested set of resources to put elements on; this is only considered for elements not in sequenceSuggestion.
	 * @param pairingHints
	 *            a pairing of {@link ISequenceElement} to use for the initial sequences but do not need enforced by a constraint checker.
	 * @return
	 */
	public ISequences createInitialSequences(IOptimisationData data, ISequences sequenceSuggestion, Map<ISequenceElement, IResource> resourceSuggestion,
			Map<ISequenceElement, ISequenceElement> pairingHints);
}
