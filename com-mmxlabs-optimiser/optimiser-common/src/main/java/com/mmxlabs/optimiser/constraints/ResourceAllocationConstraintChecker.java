package com.mmxlabs.optimiser.constraints;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.IConstraintChecker;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.optimiser.ISequence;
import com.mmxlabs.optimiser.ISequences;
import com.mmxlabs.optimiser.dcproviders.IResourceAllocationConstraintDataComponentProvider;

/**
 * A {@link IConstraintChecker} implementation which enforces allocation of
 * sequence elements to permitted {@link IResource}s. This implements an early
 * break-out, meaning only the first violation of this constraint is logged.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class ResourceAllocationConstraintChecker<T> implements
		IConstraintChecker<T> {

	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	@Override
	public boolean checkConstraints(final ISequences<T> sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences<T> sequences,
			final List<String> messages) {

		final List<IResource> resources = sequences.getResources();
		for (final IResource resource : resources) {
			final ISequence<T> sequence = sequences.getSequence(resource);
			final boolean ok = checkSequence(resource, sequence, messages);
			if (!ok) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Loop through sequence checking the resource is a permitted resource for
	 * the sequence element.
	 * 
	 * @param resource
	 * @param sequence
	 * @param messages
	 * @return
	 */
	private boolean checkSequence(final IResource resource,
			final ISequence<T> sequence, final List<String> messages) {

		for (final T t : sequence) {
			final Collection<IResource> allowedResources = resourceAllocationConstraintDataComponentProvider
					.getAllowedResources(t);
			if (allowedResources != null) {
				if (!allowedResources.contains(resource)) {

					if (messages != null) {
						final String msg = String
								.format(
										"Element (%s) is not permitted to be allocated to resource (%s)",
										t, resource);
						messages.add(msg);
					}

					return false;
				}
			}
		}

		return true;
	}

	public void setResourceAllocationConstraintDataComponentProvider(
			IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider) {
		this.resourceAllocationConstraintDataComponentProvider = resourceAllocationConstraintDataComponentProvider;
	}

	public IResourceAllocationConstraintDataComponentProvider getResourceAllocationConstraintDataComponentProvider() {
		return resourceAllocationConstraintDataComponentProvider;
	}
}
