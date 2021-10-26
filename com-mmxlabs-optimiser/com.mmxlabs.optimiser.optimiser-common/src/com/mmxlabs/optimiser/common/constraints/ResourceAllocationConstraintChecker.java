/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.optimiser.core.constraints.IResourceElementConstraintChecker;

/**
 * A {@link IConstraintChecker} implementation which enforces allocation of sequence elements to permitted {@link IResource}s. This implements an early break-out, meaning only the first violation of
 * this constraint is logged.
 * 
 * @author Simon Goodall
 * 
 */
public final class ResourceAllocationConstraintChecker implements IPairwiseConstraintChecker, IResourceElementConstraintChecker {
	@NonNull
	private final String name;

	@Inject
	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	public ResourceAllocationConstraintChecker(@NonNull final String name) {
		this.name = name;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {

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
	private boolean checkSequence(@NonNull final IResource resource, @NonNull final ISequence sequence, final List<String> messages) {

		for (final ISequenceElement t : sequence) {
			assert t != null;
			if (!checkElement(t, resource, messages)) {
				return false;
			}
		}

		return true;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
		final boolean result = checkElement(first, resource, messages) && checkElement(second, resource, messages);
		if (messages != null && !result)
			messages.add(explain(first, second, resource));
		return result;
	}

	@Override
	public final boolean checkElement(@NonNull final ISequenceElement element, @NonNull final IResource resource, final List<String> messages) {

		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(element);
		final boolean result = ((resources == null) || resources.contains(resource));
		if (!result && messages != null)
			messages.add(String.format("%s: Element (%s) is not permitted to be allocated to resource (%s)", this.name, element, resource));
		return result;
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(first);
		String resourcesName = resources != null ? resources.toString() : "no allowed resources for this sequence element";
		return String.format("%s: for sequnce element [%s] resource [%s] first in [%s] second in [%s]", this.name, first.getName(), resource.getName(), resourcesName, 
				resourceAllocationConstraintDataComponentProvider.getAllowedResources(second));
	}
}
