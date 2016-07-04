/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * The default {@link IPortTimeWindowsRecord}
 * 
 */
public class PortTimeWindowsRecord implements IPortTimeWindowsRecord {

	private static class SlotWindowRecord {
		public ITimeWindow feasibleWindow = null;
		public int duration;
		public int index;

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof SlotWindowRecord) {
				final SlotWindowRecord other = (SlotWindowRecord) obj;
				return feasibleWindow == other.feasibleWindow && duration == other.duration;
			}
			return false;
		}

		@Override
		public int hashCode() {

			return Objects.hash(feasibleWindow, duration);
		}
	}

	// Most voyages are load, discharge, next. DES/FOB cargoes have a start, load, discharge end sequence. 4 elements is a good starting point, although LDD etc style cargoes could start to push this
	// up.
	private static final int INITIAL_CAPACITY = 4;
	private final Map<IPortSlot, SlotWindowRecord> slotRecords = new HashMap<IPortSlot, SlotWindowRecord>(INITIAL_CAPACITY);
	private final List<IPortSlot> slots = new ArrayList<IPortSlot>(INITIAL_CAPACITY);
	private ITimeWindow firstSlotFeasibleTimeWindow = null;
	private IPortSlot firstPortSlot = null;
	private IPortSlot returnSlot;
	private IResource resource;
	
	public PortTimeWindowsRecord() {

	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final String slotFormat = "%s@%d";
		boolean firstLoop = true;
		for (final IPortSlot slot : slots) {
			final SlotWindowRecord slotAllocation = slotRecords.get(slot);
			if (!firstLoop) {
				builder.append(" to ");
			} else {
				firstLoop = false;
			}

			builder.append(String.format(slotFormat, slot.getId(), slotAllocation.feasibleWindow.getInclusiveStart()));
		}

		return builder.toString();
	}

	@Override
	public List<IPortSlot> getSlots() {
		return slots;
	}

	private SlotWindowRecord getOrCreateSlotRecord(final IPortSlot slot) {
		assert slot != null;
		SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			allocation = new SlotWindowRecord();
			slotRecords.put(slot, allocation);
			slots.add(slot);
		}
		return allocation;
	}
	
	@Override
	public void setSlotFeasibleTimeWindow(final IPortSlot slot, final ITimeWindow timeWindow) {
		getOrCreateSlotRecord(slot).feasibleWindow = timeWindow;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotFeasibleTimeWindow = timeWindow;
			firstPortSlot = slot;
		}
	}

	@Override
	public void setSlot(final IPortSlot slot, final ITimeWindow timeWindow, final int duration, final int index) {
		final SlotWindowRecord allocation = getOrCreateSlotRecord(slot);
		allocation.feasibleWindow = timeWindow;
		allocation.duration = duration;
		allocation.index = index;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotFeasibleTimeWindow = timeWindow;
			firstPortSlot = slot;
		}
	}

	public void setReturnSlotFeasibleTimeWindow(final IPortSlot slot, final ITimeWindow timeWindow) {
		setSlotFeasibleTimeWindow(slot, timeWindow);
		// Return slot should not be in list
		slots.remove(slot);
		this.returnSlot = slot;
	}

	public void setReturnSlot(final IPortSlot slot, final ITimeWindow timeWindow, final int duration, final int index) {
		setSlot(slot, timeWindow, duration, index);
		// Return slot should not be in list
		slots.remove(slot);
		this.returnSlot = slot;
	}

	@Override
	public int getSlotDuration(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.duration;
		}
		return 0;
	}

	@Override
	public void setSlotDuration(final IPortSlot slot, final int duration) {
		getOrCreateSlotRecord(slot).duration = duration;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof PortTimeWindowsRecord) {
			final PortTimeWindowsRecord other = (PortTimeWindowsRecord) obj;
			return slotRecords.equals(other.slotRecords);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(slots, slotRecords);
	}

	@Override
	public ITimeWindow getSlotFeasibleTimeWindow(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.feasibleWindow;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public ITimeWindow getFirstSlotFeasibleTimeWindow() {
		return firstSlotFeasibleTimeWindow;
	}

	@Override
	public IPortSlot getFirstSlot() {
		return firstPortSlot;
	}

	@Override
	public IPortSlot getReturnSlot() {
		return returnSlot;
	}
	
	//TODO: remove this
	@Override
	public int getIndex(IPortSlot slot) {
		SlotWindowRecord allocation = slotRecords.get(slot);
		return allocation != null ? allocation.index : -1;
	}

	@Override
	public IResource getResource() {
		return resource;
	}
	
	public IResource setResource(IResource resource) {
		return this.resource = resource;
	}

}
