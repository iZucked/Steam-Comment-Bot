package com.mmxlabs.scheduler.optimiser.constraints.impl;

import java.util.List;
import java.util.Set;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPort;
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
 * @param <T>
 */
public class PortExclusionConstraintChecker<T> implements IConstraintChecker<T> {

	private IPortExclusionProvider portExclusionProvider;
	private IVesselProvider vesselProvider;
	private IPortProvider portProvider;

	private final String name;
	private final String exclusionProviderKey;
	private final String vesselProviderKey;
	private final String portProviderKey;
	
	
	
	public PortExclusionConstraintChecker(String name,
			String exclusionProviderKey, String vesselProviderKey,
			String portProviderKey) {
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

	/*
	 * This is a fail-fast version of the method below
	 */
	@Override
	public boolean checkConstraints(ISequences<T> sequences) {
		final List<IResource> resources = sequences.getResources();
		
		for (int i = 0; i<sequences.size(); i++) {
			final Set<IPort> excludedPorts = 
				portExclusionProvider.getExcludedPorts(
					vesselProvider.getVessel(resources.get(i)).getVesselClass()
				);
			final ISequence<T> sequence = sequences.getSequence(i);
			for (int j = 0; j<sequence.size(); j++) {
				if (excludedPorts.contains(portProvider.getPortForElement(sequence.get(j)))) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public boolean checkConstraints(ISequences<T> sequences,
			List<String> messages) {
		if (messages == null)
			return checkConstraints(sequences);
		final List<IResource> resources = sequences.getResources();
		boolean valid = true;
		for (int i = 0; i<sequences.size(); i++) {
			final Set<IPort> excludedPorts = 
				portExclusionProvider.getExcludedPorts(
					vesselProvider.getVessel(resources.get(i)).getVesselClass()
				);
			final ISequence<T> sequence = sequences.getSequence(i);
			for (int j = 0; j<sequence.size(); j++) {
				if (excludedPorts.contains(portProvider.getPortForElement(sequence.get(j)))) {
					messages.add("Vessel " + resources.get(i).getName() + " cannot visit port " +
							portProvider.getPortForElement(sequence.get(j)).getName());
					valid = false;
				}
				
			}
		}
		return valid;
	}

	@Override
	public void setOptimisationData(IOptimisationData<T> data) {
		this.portExclusionProvider = data.getDataComponentProvider(exclusionProviderKey, IPortExclusionProvider.class);
		this.vesselProvider = data.getDataComponentProvider(vesselProviderKey, IVesselProvider.class);
		this.portProvider = data.getDataComponentProvider(portProviderKey, IPortProvider.class);
	}

}
