package com.mmxlabs.scheduler.optimiser.schedule;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * 
 * A class used to help with looking up data from the different internal data structures by port slot.
 * 
 * NOT THREAD SAFE
 * 
 * TODO: This should be rolled into the core API more directly - i.e. not useful until volume allocation has run, evaluators can cause data to be invalid etc...
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduledDataLookupProvider {

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IPortSlotProvider portSlotProvider;

	private ScheduledSequences currentScheduledSequences;
	private ISequences currentSequences;

	private final Map<IPortSlot, VoyagePlan> slotToVoyagePlanCache = new HashMap<>();
	private final Map<IPortSlot, IVessel> slotToVesselCache = new HashMap<>();
	private final Map<IPortSlot, IAllocationAnnotation> slotToAllocationAnnotationCache = new HashMap<>();
	private final Map<IPortSlot, Integer> slotToVesselStartTimeCache = new HashMap<>();
	private final Map<IPortSlot, Integer> slotToArrivalTimeCache = new HashMap<>();

	private boolean dirty = false;

	/**
	 * Analyses the {@link ScheduledSequences} object for volume allocations (note this must be run after the volume allocator has completed) and records the data required for USS calculations. This
	 * 
	 * @return
	 */
	private void buildCache() {

		assert currentSequences != null;
		assert currentScheduledSequences != null;

		slotToVesselCache.clear();
		slotToAllocationAnnotationCache.clear();
		slotToVesselStartTimeCache.clear();
		slotToVesselCache.clear();

		for (final Map.Entry<VoyagePlan, IAllocationAnnotation> e : currentScheduledSequences.getAllocations().entrySet()) {
			final VoyagePlan vp = e.getKey();
			final IAllocationAnnotation aa = e.getValue();
			for (final IPortSlot slot : aa.getSlots()) {
				slotToVoyagePlanCache.put(slot, vp);
				slotToAllocationAnnotationCache.put(slot, aa);
			}
		}

		for (final ScheduledSequence scheduledSequence : currentScheduledSequences) {

			final IResource resource = scheduledSequence.getResource();
			final IVessel vessel = vesselProvider.getVessel(resource);
			final ISequence sequence = currentSequences.getSequence(resource);

			int[] arrivalTimes = scheduledSequence.getArrivalTimes();
			final int vesselStartTime = scheduledSequence.getStartTime();
			int idx = 0;
			for (final ISequenceElement element : sequence) {
				final IPortSlot slot = portSlotProvider.getPortSlot(element);
				if (slot != null) {
					slotToVesselCache.put(slot, vessel);
					slotToVesselStartTimeCache.put(slot, vesselStartTime);
					slotToArrivalTimeCache.put(slot, arrivalTimes[idx]);
				}
				++idx;
			}
		}

		dirty = false;
	}

	public void setInputs(final ISequences sequences, final ScheduledSequences scheduledSequences) {

		this.currentSequences = sequences;
		this.currentScheduledSequences = scheduledSequences;

		this.dirty = true;
	}

	public VoyagePlan getVoyagePlan(final IPortSlot portSlot) {
		if (dirty) {
			buildCache();
		}
		return slotToVoyagePlanCache.get(portSlot);
	}

	public IVessel getVessel(final IPortSlot portSlot) {

		if (dirty) {
			buildCache();
		}
		return slotToVesselCache.get(portSlot);
	}

	public Integer getVesselStartTime(final IPortSlot portSlot) {
		if (dirty) {
			buildCache();
		}
		return slotToVesselStartTimeCache.get(portSlot);
	}

	public IAllocationAnnotation getAllocationAnnotation(final IPortSlot portSlot) {

		if (dirty) {
			buildCache();
		}
		return slotToAllocationAnnotationCache.get(portSlot);
	}

	/**
	 * Clears any stored data
	 */
	public void reset() {
		dirty = false;

		slotToVesselCache.clear();
		slotToAllocationAnnotationCache.clear();
		slotToVesselStartTimeCache.clear();
		slotToVesselCache.clear();

	}

}
