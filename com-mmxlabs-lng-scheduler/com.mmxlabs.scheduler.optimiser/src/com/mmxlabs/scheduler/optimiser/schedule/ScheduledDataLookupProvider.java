/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * A class used to help with looking up data from the different internal data structures by port slot.
 * 
 * NOT THREAD SAFE
 * 
 * TODO: This should be rolled into the core API more directly - i.e. not useful until volume allocation has run, evaluators can cause data to be invalid etc... TODO: Build cache into the
 * {@link ScheduledSequences} API
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduledDataLookupProvider {
	@Inject
	private IVesselProvider vesselProvider;

	private ScheduledSequences currentScheduledSequences;

	private final Map<IPortSlot, ScheduledSequence> slotToSequenceCache = new HashMap<>();

	private boolean dirty = false;

	private void buildCache() {

		assert currentScheduledSequences != null;

		slotToSequenceCache.clear();

		for (final ScheduledSequence scheduledSequence : currentScheduledSequences) {
			for (final IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
				slotToSequenceCache.put(portSlot, scheduledSequence);
			}
		}

		dirty = false;
	}

	public void setInputs(final ScheduledSequences scheduledSequences) {

		this.currentScheduledSequences = scheduledSequences;

		this.dirty = true;
	}

	@Nullable
	public ScheduledSequence getScheduledSequence(final IPortSlot portSlot) {
		if (dirty) {
			buildCache();
		}
		return slotToSequenceCache.get(portSlot);
	}

	public VoyagePlan getVoyagePlan(final IPortSlot portSlot) {

		final ScheduledSequence scheduledSequence = getScheduledSequence(portSlot);
		if (scheduledSequence != null) {
			return scheduledSequence.getVoyagePlan(portSlot);
		}

		return null;
	}

	public IVesselAvailability getVesselAvailability(final IPortSlot portSlot) {

		final ScheduledSequence scheduledSequence = getScheduledSequence(portSlot);
		if (scheduledSequence != null) {
			return vesselProvider.getVesselAvailability(scheduledSequence.getResource());
		}

		return null;
	}

	public Integer getVesselStartTime(final IPortSlot portSlot) {
		final ScheduledSequence scheduledSequence = getScheduledSequence(portSlot);
		if (scheduledSequence != null) {
			return scheduledSequence.getStartTime();
		}
		return null;
	}

	public IAllocationAnnotation getAllocationAnnotation(final IPortSlot portSlot) {

		final ScheduledSequence scheduledSequence = getScheduledSequence(portSlot);
		if (scheduledSequence != null) {
			return scheduledSequence.getAllocationAnnotation(portSlot);
		}
		return null;
	}

	/**
	 * Clears any stored data
	 */
	public void reset() {
		dirty = false;

		slotToSequenceCache.clear();
	}
}
