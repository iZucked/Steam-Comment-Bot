/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IPairwiseConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.providers.IPortExclusionProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;

/**
 * A constraint checker which determines whether every port in the sequence is legal for the given
 * vessel. Whether this is the best possible solution is debatable, as it involves a few more lookups
 * than the minimum possible number; we could instead store a map from vessels to excluded <em>elements</em>
 * instead of <em>ports</em>, saving a lookup at the expense of builder complexity.
 * @author hinton
 *
 * @param 
 */
public class PortExclusionConstraintChecker implements IPairwiseConstraintChecker {

	private IPortExclusionProvider portExclusionProvider;
	private IVesselProvider vesselProvider;
	private IPortProvider portProvider;

	private final String name;
	private final String exclusionProviderKey;
	private final String vesselProviderKey;
	private final String portProviderKey;
	
	public PortExclusionConstraintChecker(final String name,
			final String exclusionProviderKey, final String vesselProviderKey,
			final String portProviderKey) {
		super();
		this.name = name;
		this.exclusionProviderKey = exclusionProviderKey;
		this.vesselProviderKey = vesselProviderKey;
		this.portProviderKey = portProviderKey;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean checkSequence(final ISequence sequence, final IResource resource) {
		return checkSequence(sequence, resource, null);
	}
	
	public boolean checkSequence(final ISequence sequence, final IResource resource, final List<String> messages) {
		if (portExclusionProvider.hasNoExclusions()) return true;
		
		final Set<IPort> excludedPorts = 
			portExclusionProvider.getExcludedPorts(
				vesselProvider.getVessel(resource).getVesselClass()
			);
		
		if (excludedPorts.isEmpty()) return true;
		boolean valid = true;
		for (int j = 0; j<sequence.size(); j++) {
			if (excludedPorts.contains(portProvider.getPortForElement(sequence.get(j)))) {
				if (messages == null) {
					return false; //fail fast.
				} else {
					messages.add("Vessel " + vesselProvider.getVessel(resource).getName() + " is excluded from port " + 
							portProvider.getPortForElement(sequence.get(j)).getName());
					valid = false;
				}
			}
		}
		return valid || messages.isEmpty();
	}
	
	/*
	 * This is a fail-fast version of the method below
	 */
	@Override
	public boolean checkConstraints(final ISequences sequences) {
		return checkConstraints(sequences, null);
	}

	@Override
	public boolean checkConstraints(final ISequences sequences,
			final List<String> messages) {
		if (portExclusionProvider.hasNoExclusions()) return true;
		final List<IResource> resources = sequences.getResources();
	
		boolean valid = true;
		
		for (int i = 0; i<sequences.size(); i++) {
			if (!checkSequence(sequences.getSequence(i), resources.get(i), messages)) {
				if (messages == null)
					return false;
				else
					valid = false;
			}
		}
		
		return valid;
	}

	@Override
	public void setOptimisationData(final IOptimisationData data) {
		setPortExclusionProvider(data.getDataComponentProvider(exclusionProviderKey, IPortExclusionProvider.class));
		setVesselProvider(data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class));
		setPortProvider(data.getDataComponentProvider(portProviderKey, IPortProvider.class));
	}

	public IPortExclusionProvider getPortExclusionProvider() {
		return portExclusionProvider;
	}

	public void setPortExclusionProvider(
			final IPortExclusionProvider portExclusionProvider) {
		this.portExclusionProvider = portExclusionProvider;
	}

	public IVesselProvider getVesselProvider() {
		return vesselProvider;
	}

	public void setVesselProvider(final IVesselProvider vesselProvider) {
		this.vesselProvider = vesselProvider;
	}

	public IPortProvider getPortProvider() {
		return portProvider;
	}

	public void setPortProvider(final IPortProvider portProvider) {
		this.portProvider = portProvider;
	}

	@Override
	public boolean checkPairwiseConstraint(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		if (portExclusionProvider.hasNoExclusions()) return true;
		
		final IVessel vessel = vesselProvider.getVessel(resource);
		final Set<IPort> exclusions = portExclusionProvider.getExcludedPorts(vessel.getVesselClass());
		
		if (exclusions.isEmpty()) return true;
		
		return !(exclusions.contains(portProvider.getPortForElement(first)) ||
			exclusions.contains(portProvider.getPortForElement(second)));
	}

	@Override
	public String explain(final ISequenceElement first, final ISequenceElement second, final IResource resource) {
		// TODO Auto-generated method stub
		return null;
	}
}
