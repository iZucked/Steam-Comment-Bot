/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A constraint checker which determines whether every port in the sequence is legal for the given vessel. Whether this is the best possible solution is debatable, as it involves a few more lookups
 * than the minimum possible number; we could instead store a map from vessels to excluded <em>elements</em> instead of <em>ports</em>, saving a lookup at the expense of builder complexity.
 * 
 * @author hinton
 * 
 * @param
 */
public class PortExclusionConstraintChecker implements IPairwiseConstraintChecker {

	@Inject
	@NonNull
	private IPortExclusionProvider portExclusionProvider;

	@Inject
	@NonNull
	private IVesselProvider vesselProvider;

	@Inject
	@NonNull
	private IPortProvider portProvider;

	@Inject
	@NonNull
	private INominatedVesselProvider nominatedVesselProvider;

	@NonNull
	private final String name;

	public PortExclusionConstraintChecker(@NonNull final String name) {
		super();
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource) {
		return checkSequence(sequence, resource, null);
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, @Nullable final List<String> messages) {
		if (portExclusionProvider.hasNoExclusions()) {
			return true;
		}

		IVessel vessel = nominatedVesselProvider.getNominatedVessel(resource);
		if (vessel == null) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			vessel = vesselAvailability.getVessel();
		}
		assert vessel != null;

		final Set<IPort> excludedPorts = getExclusionsForVessel(vessel);
		if (excludedPorts.isEmpty()) {
			return true;
		}
		boolean valid = true;
		for (int j = 0; j < sequence.size(); j++) {
			IPort portForElement = portProvider.getPortForElement(sequence.get(j));
			if (excludedPorts.contains(portForElement)) {
				if (messages == null) {
					return false; // fail fast.
				} else {
					messages.add("Vessel " + vesselProvider.getVesselAvailability(resource).getVessel().getName() + " is excluded from port "
							+ (portForElement == null ? "(unknown port)" : portForElement.getName()));
					valid = false;
				}
			}
		}
		return valid;
	}

	/*
	 * This is a fail-fast version of the method below
	 */
	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources) {
		return checkConstraints(sequences, changedResources, null);
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, @Nullable final List<String> messages) {
		if (portExclusionProvider.hasNoExclusions()) {
			return true;
		}
		final Collection<@NonNull IResource> loopResources;
		if (changedResources == null) {
			loopResources = sequences.getResources();
		} else {
			loopResources = changedResources;
		}
		boolean valid = true;
		for (final IResource resource : loopResources) {
			final ISequence sequence = sequences.getSequence(resource);
			if (!checkSequence(sequence, resource, messages)) {
				if (messages == null) {
					return false;
				} else {
					valid = false;
				}
			}
		}

		return valid;
	}

	@Override
	public void setOptimisationData(@NonNull final IOptimisationData data) {
	}

	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		if (portExclusionProvider.hasNoExclusions()) {
			return true;
		}

		IVessel vessel = nominatedVesselProvider.getNominatedVessel(resource);
		if (vessel == null) {
			final IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resource);
			vessel = vesselAvailability.getVessel();
		}
		assert vessel != null;

		// Get vessel exclusions,
		final Set<IPort> exclusions = getExclusionsForVessel(vessel);
		if (exclusions.isEmpty()) {
			return true;
		}

		return !(exclusions.contains(portProvider.getPortForElement(first)) || exclusions.contains(portProvider.getPortForElement(second)));
	}

	@Override
	public String explain(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource) {
		return null;
	}

	/**
	 * Get the vessel or class port exclusions applicable to this vessel.
	 * 
	 * @param vessel
	 * @return
	 */
	@NonNull
	private Set<IPort> getExclusionsForVessel(@NonNull final IVessel vessel) {
		Set<IPort> exclusions = portExclusionProvider.getExcludedPorts(vessel);

		// If there are non, pick the class exclusions
		if (exclusions.isEmpty()) {
			exclusions = portExclusionProvider.getExcludedPorts(vessel.getVesselClass());
		}
		assert exclusions != null;
		return exclusions;
	}

}
