/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanIterator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public final class ScheduledSequence {
	private final IResource resource;
	private final ISequence sequence;
	private final int startTime;
	private final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans;

	// Cached Lookup data
	private final Map<IPortSlot, Integer> portSlotToTimeMap = new HashMap<>();
	private final Map<IPortSlot, VoyagePlan> portSlotToVoyagePlanMap = new HashMap<>();
	private final Map<IPortSlot, IPortTimesRecord> portSlotToPortTimesRecordMap = new HashMap<>();
	private final Map<IPortSlot, IHeelLevelAnnotation> portSlotToHeelLevelAnnotationMap = new HashMap<>();
	private final List<IPortSlot> sequencePortSlots;

	/**
	 */
	public ScheduledSequence(final IResource resource, final ISequence sequence, final int startTime, final List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> voyagePlans) {
		super();
		this.sequence = sequence;
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
		this.sequencePortSlots = new ArrayList<>(sequence.size());

		// Build the lookup data!
		buildLookup();
	}

	protected ISequence getSequence() {
		return sequence;
	}

	public IResource getResource() {
		return resource;
	}

	public int getStartTime() {
		return startTime;
	}

	public List<Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord>> getVoyagePlans() {
		return voyagePlans;
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

	public List<IPortSlot> getSequenceSlots() {
		return sequencePortSlots;
	}

	public IPortTimesRecord getPortTimesRecord(final IPortSlot portSlot) {
		return portSlotToPortTimesRecordMap.get(portSlot);

	}

	@Nullable
	public IAllocationAnnotation getAllocationAnnotation(final IPortSlot portSlot) {
		final IPortTimesRecord portTimesRecord = getPortTimesRecord(portSlot);
		if (portTimesRecord instanceof IAllocationAnnotation) {
			return (IAllocationAnnotation) portTimesRecord;
		}
		return null;

	}

	public IHeelLevelAnnotation getHeelLevelAnnotation(final IPortSlot portSlot) {
		return portSlotToHeelLevelAnnotationMap.get(portSlot);
	}

	/**
	 * Builds the lookup data. TODO: More efficient if this is done as the sequence data is built up!
	 */
	private void buildLookup() {
		final VoyagePlanIterator vpi = new VoyagePlanIterator(this);

		// Lists to store the voyageplan data
		while (vpi.hasNextObject()) {
			final Object e = vpi.nextObject();
			if (e instanceof PortDetails) {
				final PortDetails details = (PortDetails) e;
				final IPortSlot portSlot = details.getOptions().getPortSlot();
				final int currentTime = vpi.getCurrentTime();

				// Set mapping between slot and time / current plan
				sequencePortSlots.add(portSlot);
				portSlotToTimeMap.put(portSlot, currentTime);
				portSlotToVoyagePlanMap.put(portSlot, vpi.getCurrentPlan());
				portSlotToPortTimesRecordMap.put(portSlot, vpi.getCurrentPortTimeRecord());
				final Map<IPortSlot, IHeelLevelAnnotation> currentHeelLevelAnnotations = vpi.getCurrentHeelLevelAnnotations();
				if (currentHeelLevelAnnotations != null) {
					portSlotToHeelLevelAnnotationMap.put(portSlot, currentHeelLevelAnnotations.get(portSlot));
				}
			}
		}
	}

}