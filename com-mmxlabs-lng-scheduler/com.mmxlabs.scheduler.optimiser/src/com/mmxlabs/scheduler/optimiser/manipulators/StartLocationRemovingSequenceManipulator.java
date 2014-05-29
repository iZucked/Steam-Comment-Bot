/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.manipulators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A sequence manipulator for removing the dummy start location from spot charter vessels. Needs to be told which resources should have their start locations removed.
 * 
 * @author hinton
 * 
 * @param
 */
public class StartLocationRemovingSequenceManipulator implements ISequencesManipulator {

	@Inject
	private IVesselProvider vesselProvider;

	private final Set<IResource> resourcesToManipulate = new HashSet<IResource>();

	@Override
	public void manipulate(final IModifiableSequences sequences) {
		// Loop through each sequence in turn and manipulate
		for (final Map.Entry<IResource, IModifiableSequence> entry : sequences.getModifiableSequences().entrySet()) {
			manipulate(entry.getKey(), entry.getValue());
		}
	}

	private void manipulate(final IResource resource, final IModifiableSequence sequence) {
		if (getShouldRemoveStartLocation(resource)) {
			sequence.remove(0);
		}
	}

	@Override
	public void dispose() {
		resourcesToManipulate.clear();
	}

	/**
	 * Set whether the given resource should have its start element removed (typically this will be if it is a spot charter vessel).
	 * 
	 * @param resource
	 * @param shouldRemoveStartLocation
	 *            if true, start location will be removed, otherwise not.
	 */
	public void setShouldRemoveStartLocation(final IResource resource, final boolean shouldRemoveStartLocation) {
		if (shouldRemoveStartLocation) {
			resourcesToManipulate.add(resource);
		} else {
			resourcesToManipulate.remove(resource);
		}
	}

	public boolean getShouldRemoveStartLocation(final IResource resource) {
		return resourcesToManipulate.contains(resource);
	}

	/**
	 */
	@Override
	public void init(final IOptimisationData data) {

		for (final IResource resource : data.getResources()) {
			final VesselInstanceType vesselInstanceType = vesselProvider.getVessel(resource).getVesselInstanceType();
			if (vesselInstanceType == VesselInstanceType.CARGO_SHORTS) {
				setShouldRemoveStartLocation(resource, true);
			} else if (vesselInstanceType.equals(VesselInstanceType.SPOT_CHARTER)) {
				setShouldRemoveStartLocation(resource, true);
			}
		}
	}
}
