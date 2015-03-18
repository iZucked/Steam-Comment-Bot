/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

/**
 * A {@link IConstraintChecker} implementation which enforces allocation of sequence elements to permitted {@link IResource}s. This implements an early break-out, meaning only the first violation of
 * this constraint is logged.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintChecker implements IPairwiseConstraintChecker {

	private final String name;

	@Inject
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	public ResourceAllocationConstraintChecker(final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences, final List<String> messages) {

		final List<IResource> resources = sequences.getResources();
		for (final IResource resource : resources) {
			final ISequence sequence = sequences.getSequence(resource);
			final boolean ok = checkSequence(resource, sequence, messages);
			if (!ok) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Loop through sequence checking the resource is a permitted resource for the sequence element.
	 * 
	 * @param resource
	 * @param sequence
	 * @param messages
	 * @return
	 */
	private boolean checkSequence(final IResource resource, final ISequence sequence, final List<String> messages) {

		for (final ISequenceElement t : sequence) {
			if (!checkElement(t, resource)) {
				if (messages != null) {
					final String msg = String.format("Element (%s) is not permitted to be allocated to resource (%s)", t, resource);
					messages.add(msg);
				}
				return false;
			}
		}

		return true;
	}

	@Override
	public void setOptimisationData(final IOptimisationData optimisationData) {
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {

		return checkElement(first, resource) && checkElement(second, resource);
	}

	private final boolean checkElement(final ISequenceElement element, final IResource resource) {

		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(element);
		return ((resources == null) || resources.contains(resource));
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(first);
		return "Resource: " + resource.getName() + ", first in " + resources + ", second in " + resourceAllocationConstraintDataComponentProvider.getAllowedResources(second);
	}
}
