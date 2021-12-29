/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
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
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.providers.IElementPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.INominatedVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProvider;
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
	private IPortExclusionProvider portExclusionProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IElementPortProvider portProvider;

	@Inject
	private INominatedVesselProvider nominatedVesselProvider;

	private final @NonNull String name;

	public PortExclusionConstraintChecker(@NonNull final String name) {
		super();
		this.name = name;
	}

	@Override
	@NonNull
	public String getName() {
		return name;
	}

	public boolean checkSequence(@NonNull final ISequence sequence, @NonNull final IResource resource, final List<String> messages) {
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
				if (messages != null)
					messages.add(this.name + ": Vessel " + vesselProvider.getVesselAvailability(resource).getVessel().getName() + " is excluded from port "
						+ (portForElement == null ? "(unknown port)" : portForElement.getName()));
				return false;
			}
		}
		return valid;
	}

	@Override
	public boolean checkConstraints(@NonNull final ISequences sequences, @Nullable final Collection<@NonNull IResource> changedResources, final List<String> messages) {
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
				return false;
			}
		}

		return valid;
	}
 
	@Override
	public boolean checkPairwiseConstraint(@NonNull final ISequenceElement first, @NonNull final ISequenceElement second, @NonNull final IResource resource, final List<String> messages) {
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
		final IPort firstPort = portProvider.getPortForElement(first);
		final IPort secondPort = portProvider.getPortForElement(second);
		final boolean result = !(exclusions.contains(firstPort) || exclusions.contains(secondPort));
		if (!result && messages != null) {
			messages.add(String.format("%s: Vessel %s is exluded from ports %s or %s.", this.name, vessel.getName(), firstPort.getName(), secondPort.getName()));
		}
		return result;
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
		assert exclusions != null;
		return exclusions;
	}

}
