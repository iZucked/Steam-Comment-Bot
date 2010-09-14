package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;

/**
 * A sequence manipulator for removing the dummy start location from spot charter vessels.
 * Needs to be told which resources should have their start locations removed.
 * 
 * @author hinton
 *
 * @param <T>
 */
public class StartLocationRemovingSequenceManipulator<T> implements
		ISequencesManipulator<T> {
	
	private final Set<IResource> resourcesToManipulate = 
		new HashSet<IResource>();
	
	@Override
	public void manipulate(IModifiableSequences<T> sequences) {
		// Loop through each sequence in turn and manipulate
		for (final Map.Entry<IResource, IModifiableSequence<T>> entry : sequences
				.getModifiableSequences().entrySet()) {
			manipulate(entry.getKey(), entry.getValue());
		}
	}

	private void manipulate(IResource resource, IModifiableSequence<T> sequence) {
		if (getShouldRemoveStartLocation(resource)) {
			sequence.remove(0);
		}
	}

	@Override
	public void dispose() {
		resourcesToManipulate.clear();
	}

	/**
	 * Set whether the given resource should have its start element removed (typically
	 * this will be if it is a spot charter vessel).
	 * 
	 * @param resource
	 * @param shouldRemoveStartLocation if true, start location will be removed, otherwise not.
	 */
	public void setShouldRemoveStartLocation(final IResource resource,
			final boolean shouldRemoveStartLocation) {
		if (shouldRemoveStartLocation)
			resourcesToManipulate.add(resource);
		else
			resourcesToManipulate.remove(resource);
	}
	
	public boolean getShouldRemoveStartLocation(final IResource resource) {
		return resourcesToManipulate.contains(resource);
	}
}
