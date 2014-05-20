/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class ScheduledSequence {
	private final int startTime;
	private final List<VoyagePlan> voyagePlans;
	private final IResource resource;
	private final int[] arrivalTimes;

	// Lookup data
	private final Map<IPortSlot, Integer> portSlotToTimeMap = new HashMap<>();
	private final Map<IPortSlot, VoyagePlan> portSlotToVoyagePlanMap = new HashMap<>();
	private final Map<VoyagePlan, List<IPortSlot>> voyagePlanToPortSlots = new HashMap<>();
	private final Map<VoyagePlan, List<Integer>> voyagePlanToArrivalTimes = new HashMap<>();
	private final List<IPortSlot> sequencePortSlots;
	private VoyagePlan lastPlan;
	private Map<IPortSlot, IHeelLevelAnnotation> heelLevels = new HashMap<>();
	private Map<VoyagePlan, IAllocationAnnotation> allocations = new HashMap<>();

	/**
	 */
	public ScheduledSequence(final IResource resource, final int startTime, final List<VoyagePlan> voyagePlans, final int[] arrivalTimes) {
		super();
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
		this.arrivalTimes = arrivalTimes;
		this.sequencePortSlots = new ArrayList<>(arrivalTimes.length);

		// Build the lookup data!
		buildLookup();
	}

	public IResource getResource() {
		return resource;
	}

	public int getStartTime() {
		return startTime;
	}

	public List<VoyagePlan> getVoyagePlans() {
		return voyagePlans;
	}

	/**
	 * @return
	 */
	public int[] getArrivalTimes() {
		return arrivalTimes;
	}

	public boolean isLastVoyagePlan(final VoyagePlan plan) {
		return lastPlan != null && lastPlan == plan;
	}

	public int getArrivalTime(final IPortSlot portSlot) {
		final Integer time = portSlotToTimeMap.get(portSlot);
		if (time == null) {
			throw new IllegalArgumentException("Port slot is not part of this sequence");
		}
		return time.intValue();
	}

	public VoyagePlan getVoyagePlan(final IPortSlot portSlot) {
		return portSlotToVoyagePlanMap.get(portSlot);
	}

	public List<Integer> getArrivalTimes(final VoyagePlan voyagePlan) {
		return voyagePlanToArrivalTimes.get(voyagePlan);
	}

	public List<IPortSlot> getPortSlots(final VoyagePlan voyagePlan) {
		return voyagePlanToPortSlots.get(voyagePlan);
	}

	/**
	 * Builds the lookup data. TODO: More efficient if this is done as the sequence data is built up!
	 */
	private void buildLookup() {
		final VoyagePlanIterator vpi = new VoyagePlanIterator();
		vpi.setVoyagePlans(resource, voyagePlans, arrivalTimes);

		// Lists to store the voyageplan data
		final List<Integer> times = new LinkedList<>();
		final List<IPortSlot> portSlots = new LinkedList<>();
		VoyagePlan previousPlan = null;
		while (vpi.hasNextObject()) {
			previousPlan = vpi.getCurrentPlan();
			final boolean startOfPlan = vpi.nextObjectIsStartOfPlan();

			final Object e = vpi.nextObject();
			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot portSlot = details.getOptions().getPortSlot();
				final int currentTime = vpi.getCurrentTime();

				// Set mapping between slot and time / current plan
				portSlotToTimeMap.put(portSlot, currentTime);
				portSlotToVoyagePlanMap.put(portSlot, vpi.getCurrentPlan());

				// Add time to current times array
				times.add(currentTime);
				portSlots.add(portSlot);
				sequencePortSlots.add(portSlot);

				// Start of vessel plan? then add times and slots to last voyage plans mapping and reset data structures for the current voyage plan
				if (startOfPlan && previousPlan != null) {
					// Add to previous data
					voyagePlanToArrivalTimes.put(previousPlan, new ArrayList<>(times));
					voyagePlanToPortSlots.put(previousPlan, new ArrayList<>(portSlots));

					// Reset for next pass
					times.clear();
					portSlots.clear();

					// Re-add times to array as first elements of the next plan
					times.add(currentTime);
					portSlots.add(portSlot);
				}
			}
			lastPlan = vpi.getCurrentPlan();
		}

		// End of loop, add in last bits of data
		final VoyagePlan currentPlan = vpi.getCurrentPlan();
		if (currentPlan != null) {
			voyagePlanToArrivalTimes.put(currentPlan, new ArrayList<>(times));
			voyagePlanToPortSlots.put(currentPlan, new ArrayList<>(portSlots));
			lastPlan = currentPlan;
		}
	}

	public Map<VoyagePlan, IAllocationAnnotation> getAllocations() {
		return allocations;
	}

	public Map<IPortSlot, IHeelLevelAnnotation> getHeelLevels() {
		return heelLevels;
	}
}