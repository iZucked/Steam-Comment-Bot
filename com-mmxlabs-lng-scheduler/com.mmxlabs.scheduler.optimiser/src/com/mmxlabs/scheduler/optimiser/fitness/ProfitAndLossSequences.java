/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.paperdeals.BasicPaperDealAllocationEntry;
import com.mmxlabs.common.paperdeals.BasicPaperDealData;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;

/**
 * @author hinton, modified by FM
 * 
 */
public class ProfitAndLossSequences extends ArrayList<@NonNull VolumeAllocatedSequence> {

	private final Map<IPortSlot, Long> unusedSlotGroupValue = new HashMap<>();
	private final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealRecords = new HashMap<>();

	private final Map<@NonNull IResource, @NonNull VolumeAllocatedSequence> resourceToScheduledSequenceMap = new HashMap<>();

	public ProfitAndLossSequences() {

	}

	// Unit Test Constructor
	public ProfitAndLossSequences(ArrayList<VolumeAllocatedSequence> list) {
		super.addAll(list);
	}

	// @Override
	@Deprecated
	public boolean add(@NonNull final VolumeAllocatedSequence scheduledSequence) {
		// Call the other #add(IVesselAvailability,VolumeAllocatedSequence) method
		throw new UnsupportedOperationException("");
	}

	public boolean add(@NonNull IVesselAvailability vesselAvailability, @NonNull final VolumeAllocatedSequence scheduledSequence) {
		resourceToScheduledSequenceMap.put(scheduledSequence.getResource(), scheduledSequence);
		return super.add(scheduledSequence);
	}

	@NonNull
	public VolumeAllocatedSequence getScheduledSequenceForResource(final @NonNull IResource resource) {
		return resourceToScheduledSequenceMap.get(resource);
	}

	public void setUnusedSlotGroupValue(@NonNull final IPortSlot portSlot, final long groupValue) {
		unusedSlotGroupValue.put(portSlot, groupValue);
	}

	public long getUnusedSlotGroupValue(@NonNull final IPortSlot portSlot) {
		if (unusedSlotGroupValue.containsKey(portSlot)) {
			return unusedSlotGroupValue.get(portSlot);
		}
		return 0L;
	}

	public void setPaperDealRecords(Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealRecords) {
		this.paperDealRecords.clear();
		this.paperDealRecords.putAll(paperDealRecords);
	}

	public List<BasicPaperDealAllocationEntry> getPaperDealAllocationEntries(final BasicPaperDealData paperDeal) {
		return paperDealRecords.get(paperDeal);
	}

	public Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> getPaperDealRecords() {
		return paperDealRecords;
	}
}
