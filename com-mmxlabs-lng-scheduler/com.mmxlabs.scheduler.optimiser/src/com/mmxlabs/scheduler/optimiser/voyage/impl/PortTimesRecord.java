/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * A small class storing the port arrival time and visit duration for a {@link VoyagePlan}. This class may or may not include the times for the last element in the plan depending on how it is created.
 * For scheduling purposes the end element should be included. Often for pricing purposes the end element can be ignored.
 * 
 * Note, the order slots are added is important. They should be added in scheduled order. The calls to {@link #getFirstSlotTime()} and {@link #getFirstSlot()} are expected to be bound the first slot
 * added to the instance.
 * 
 * @author Simon Goodall
 * 
 */
public class PortTimesRecord implements IPortTimesRecord {

	private static class SlotVoyageRecord {
		public int startTime;
		public int duration;

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof SlotVoyageRecord) {
				final SlotVoyageRecord other = (SlotVoyageRecord) obj;
				return startTime == other.startTime && duration == other.duration;
			}
			return false;
		}

		@Override
		public int hashCode() {

			return Objects.hash(startTime, duration);
		}
	}

	// Most voyages are load, discharge, next. DES/FOB cargoes have a start, load, discharge end sequence. 4 elements is a good starting point, although LDD etc style cargoes could start to push this
	// up.
	private static final int INITIAL_CAPACITY = 4;
	private final Map<IPortSlot, SlotVoyageRecord> slotRecords = new HashMap<IPortSlot, SlotVoyageRecord>(INITIAL_CAPACITY);
	private final List<IPortSlot> slots = new ArrayList<IPortSlot>(INITIAL_CAPACITY);
	private int firstSlotTime = Integer.MAX_VALUE;
	private IPortSlot firstPortSlot = null;
	private IPortSlot returnSlot;

	public PortTimesRecord() {

	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public PortTimesRecord(final IPortTimesRecord other) {
		IPortSlot otherReturnSlot = other.getReturnSlot();
		for (final IPortSlot slot : other.getSlots()) {
			if (otherReturnSlot == slot) {
				this.setReturnSlotTime(slot, other.getSlotTime(slot));
			} else {
				this.setSlotTime(slot, other.getSlotTime(slot));
				this.setSlotDuration(slot, other.getSlotDuration(slot));
			}
		}
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		final String slotFormat = "%s@%d";
		boolean firstLoop = true;
		for (final IPortSlot slot : slots) {
			final SlotVoyageRecord slotAllocation = slotRecords.get(slot);
			if (!firstLoop) {
				builder.append(" to ");
			} else {
				firstLoop = false;
			}

			builder.append(String.format(slotFormat, slot.getId(), slotAllocation.startTime));
		}

		return builder.toString();
	}

	@Override
	public List<IPortSlot> getSlots() {
		return slots;
	}

	private SlotVoyageRecord getOrCreateSlotRecord(final IPortSlot slot) {
		SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			allocation = new SlotVoyageRecord();
			slotRecords.put(slot, allocation);
			slots.add(slot);
		}
		return allocation;
	}

	@Override
	public int getSlotTime(final IPortSlot slot) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.startTime;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	public void setReturnSlotTime(IPortSlot slot, final int time) {
		setSlotTime(slot, time);
		this.returnSlot = slot;
	}

	public void setSlotTime(final IPortSlot slot, final int time) {
		getOrCreateSlotRecord(slot).startTime = time;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotTime = time;
			firstPortSlot = slot;
		}
	}

	@Override
	public int getSlotDuration(final IPortSlot slot) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.duration;
		}
		return 0;
	}

	public void setSlotDuration(final IPortSlot slot, final int duration) {
		getOrCreateSlotRecord(slot).duration = duration;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof PortTimesRecord) {
			final PortTimesRecord other = (PortTimesRecord) obj;
			return slotRecords.equals(other.slotRecords);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(slots, slotRecords);
	}

	@Override
	public int getFirstSlotTime() {
		// return getSlotTime(getFirstSlot());
		return firstSlotTime;
	}

	@Override
	public IPortSlot getFirstSlot() {
		return firstPortSlot;
	}

	@Override
	public IPortSlot getReturnSlot() {
		return returnSlot;
	}
}
