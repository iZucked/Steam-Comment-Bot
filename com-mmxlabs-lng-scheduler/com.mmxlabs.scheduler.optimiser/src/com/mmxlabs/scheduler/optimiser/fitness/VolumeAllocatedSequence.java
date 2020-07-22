/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord.LatenessRecord;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class VolumeAllocatedSequence {
	private final int startTime;
	private final @NonNull IResource resource;
	private final @NonNull ISequence sequence;
	private final List<VoyagePlanRecord> voyagePlans;

	// Cached Lookup data
	private final Map<IPortSlot, VoyagePlanRecord> portSlotToVoyagePlanMap = new HashMap<>();
	private LatenessRecord record;

	public static @NonNull VolumeAllocatedSequence empty(final @NonNull IResource resource, final @NonNull ISequence sequence) {
		return new VolumeAllocatedSequence(resource, sequence, 0, Collections.emptyList());
	}

	/**
	 */
	public VolumeAllocatedSequence(final @NonNull IResource resource, final @NonNull ISequence sequence, final int startTime, final List<VoyagePlanRecord> voyagePlans) {

		this.sequence = sequence;
		this.startTime = startTime;
		this.voyagePlans = voyagePlans;
		this.resource = resource;
		for (VoyagePlanRecord vpr : voyagePlans) {
			for (IPortSlot slot : vpr.getPortTimesRecord().getSlots()) {
				portSlotToVoyagePlanMap.put(slot, vpr);
			}
			if (vpr.getPortTimesRecord().getReturnSlot() != null) {
				portSlotToVoyagePlanMap.put(vpr.getPortTimesRecord().getReturnSlot(), vpr);
			}
		}
	}

	protected @NonNull ISequence getSequence() {
		return sequence;
	}

	public @NonNull IResource getResource() {
		return resource;
	}

	public int getStartTime() {
		return startTime;
	}

	public List<VoyagePlanRecord> getVoyagePlanRecords() {
		return voyagePlans;
	}

	public VoyagePlanRecord getVoyagePlanRecord(@NonNull IPortSlot portSlot) {
		return portSlotToVoyagePlanMap.get(portSlot);
	}

	public IPortTimesRecord getPortTimesRecord(final @NonNull IPortSlot portSlot) {
		return portSlotToVoyagePlanMap.get(portSlot).getPortTimesRecord();
	}

	public final boolean isEqual(@NonNull final VolumeAllocatedSequence other) {

		return this.startTime == other.startTime //
				&& Objects.deepEquals(this.resource, other.resource) //
				&& Objects.deepEquals(this.portSlotToVoyagePlanMap, other.portSlotToVoyagePlanMap) //
				&& Objects.deepEquals(this.sequence, other.sequence) //
				&& Objects.deepEquals(this.voyagePlans, other.voyagePlans);
	}

	public void addMaxDurationLateness(long weightedLateness, Interval interval, int latenessInHours) {
		record = new LatenessRecord();
		record.weightedLateness = weightedLateness;
		record.latenessWithFlex = latenessInHours;
		record.latenessWithoutFlex = latenessInHours;
		record.interval = interval;

	}

	public @Nullable LatenessRecord getMaxDurationLatenessRecord() {
		return record;
	}

}