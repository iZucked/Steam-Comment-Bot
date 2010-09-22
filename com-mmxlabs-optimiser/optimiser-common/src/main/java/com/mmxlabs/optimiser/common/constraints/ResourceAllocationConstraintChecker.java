package com.mmxlabs.optimiser.common.constraints;

import java.util.Collection;
import java.util.List;

import com.mmxlabs.optimiser.common.dcproviders.IResourceAllocationConstraintDataComponentProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;

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
		IPairwiseConstraintChecker<T> {

	private final String name;

	private final String dataProviderKey;

	private IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider;

	public ResourceAllocationConstraintChecker(final String name,
			final String dataProviderKey) {
		this.name = name;
		this.dataProviderKey = dataProviderKey;
	}

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
			if (!checkElement(t, resource)) {
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

		return true;
	}

	public void setProvider(
			final IResourceAllocationConstraintDataComponentProvider resourceAllocationConstraintDataComponentProvider) {
		this.resourceAllocationConstraintDataComponentProvider = resourceAllocationConstraintDataComponentProvider;
	}

	public IResourceAllocationConstraintDataComponentProvider getProvider() {
		return resourceAllocationConstraintDataComponentProvider;
	}

	@Override
	public void setOptimisationData(final IOptimisationData<T> optimisationData) {
		final IResourceAllocationConstraintDataComponentProvider provider = optimisationData
				.getDataComponentProvider(
						dataProviderKey,
						IResourceAllocationConstraintDataComponentProvider.class);
		setProvider(provider);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean checkPairwiseConstraint(T first, T second, IResource resource) {
		return checkElement(first, resource) && checkElement(second, resource);
	}
	
	private final boolean checkElement(T element, IResource resource) {
		final Collection<IResource> resources = resourceAllocationConstraintDataComponentProvider.getAllowedResources(element);
		return (resources == null || resources.contains(resource));
	}

	@Override
	public String explain(T first, T second, IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}
}
