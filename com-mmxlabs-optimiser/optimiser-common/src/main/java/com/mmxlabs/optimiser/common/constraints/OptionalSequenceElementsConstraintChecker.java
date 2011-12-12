/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.List;

import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * An {@link IConstraintChecker} which makes sure that no required elements have been put in the unused elements bag
 * 
 * Does not check that every required element is present, but does check that the numbers add up.
 * 
 * @author hinton
 * 
 */
public class OptionalSequenceElementsConstraintChecker implements IConstraintChecker {
	private final String key, name;
	private IOptionalElementsProvider optionalElementsProvider;

	public OptionalSequenceElementsConstraintChecker(final String key, final String name) {
		super();
		this.key = key;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {
		for (final ISequenceElement element : sequences.getUnusedElements()) {
			if (optionalElementsProvider.isElementRequired(element)) {
				if (messages != null)
					messages.add("Element " + element + " is required, but is in the unused set");
				return false;
			}
		}

		int elementCount = sequences.getUnusedElements().size();
		for (final IResource resource : sequences.getResources()) {
			elementCount += sequences.getSequence(resource).size();
		}

		final int desiredCount = optionalElementsProvider.getOptionalElements().size() + optionalElementsProvider.getRequiredElements().size();
		if (elementCount != desiredCount) {
			if (messages != null)
				messages.add("There are " + elementCount + " elements in the solution, but there should be " + desiredCount);
			return false;
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {
		optionalElementsProvider = optimisationData.getDataComponentProvider(key, IOptionalElementsProvider.class);
	}

}
