/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.optimiser.common.constraints;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
	@NonNull
	private final String name;

	@Inject
	@NonNull
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	public ResourceAllocationConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {

		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}
		for (final IResource resource : loopResources) {
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
	private boolean checkSequence(@NonNull final IResource resource, @NonNull final ISequence sequence, @Nullable final List<String> messages) {

		for (final ISequenceElement t : sequence) {
			assert t != null;
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
	public void setOptimisationData(@NonNull final IOptimisationData optimisationData) {
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {

		return checkElement(first, resource) && checkElement(second, resource);
	}

	private final boolean checkElement(@NonNull final ISequenceElement element, @NonNull final IResource resource) {

		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(element);
		boolean ret = ((resources == null) || resources.contains(resource));

		return ret;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(first);
		return "Resource: " + resource.getName() + ", first in " + resources + ", second in " + resourceAllocationConstraintDataComponentProvider.getAllowedResources(second);
	}
}
