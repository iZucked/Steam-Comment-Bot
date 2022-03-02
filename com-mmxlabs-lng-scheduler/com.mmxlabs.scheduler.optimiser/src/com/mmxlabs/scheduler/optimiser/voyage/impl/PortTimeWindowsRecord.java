/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.cache.AbstractWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * The default {@link IPortTimeWindowsRecord}
 * 
 */
public final class PortTimeWindowsRecord extends AbstractWriteLockable implements IPortTimeWindowsRecord {

	private static class SlotWindowRecord {
		private int duration;
		private int[] extraIdleTimes = new int[ExplicitIdleTime.values().length];
		private int index;
		private AvailableRouteChoices nextVoyageRoute = AvailableRouteChoices.OPTIMAL;
		private @Nullable IRouteOptionBooking routeOptionBooking = null;
		private int additionalPanamaIdleTimeInHours = 0;
		private boolean isConstrainedPanamaJourney = false;
		private @Nullable ITimeWindow feasibleWindow = null;

		@Override
		public boolean equals(final @Nullable Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj instanceof SlotWindowRecord) {
				final SlotWindowRecord other = (SlotWindowRecord) obj;
				return duration == other.duration //
						&& index == other.index //
						&& nextVoyageRoute == other.nextVoyageRoute //
						&& routeOptionBooking == other.routeOptionBooking //
						&& additionalPanamaIdleTimeInHours == other.additionalPanamaIdleTimeInHours //
						&& isConstrainedPanamaJourney == other.isConstrainedPanamaJourney //
						&& Arrays.equals(extraIdleTimes, other.extraIdleTimes) //
						&& Objects.equals(feasibleWindow, other.feasibleWindow) //
				;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(feasibleWindow, duration, nextVoyageRoute);
		}

		@Override
		public @NonNull String toString() {
			return String.format("Idx %d, Win: %s Dur: %d", index, feasibleWindow, duration);
		}
	}

	private ImmutableMap<IPortSlot, SlotWindowRecord> slotRecords = ImmutableMap.of();
	private ImmutableList<IPortSlot> slots = ImmutableList.of();
	private @Nullable ITimeWindow firstSlotFeasibleTimeWindow = null;
	private @Nullable IPortSlot firstPortSlot = null;
	private @Nullable IPortSlot returnSlot;

	public PortTimeWindowsRecord() {

	}

	public PortTimeWindowsRecord(final IPortTimeWindowsRecord other) {
		for (final IPortSlot slot : other.getSlots()) {
			this.setSlot(slot, other.getSlotFeasibleTimeWindow(slot), other.getSlotDuration(slot), other.getIndex(slot));
			this.setSlotNextVoyageOptions(slot, other.getSlotNextVoyageOptions(slot));
			this.setSlotAdditionalPanamaDetails(slot, other.getSlotIsNextVoyageConstrainedPanama(slot), other.getSlotAdditionalPanamaIdleHours(slot));
			this.setRouteOptionBooking(slot, other.getRouteOptionBooking(slot));
			for (var type : ExplicitIdleTime.values()) {
				this.setSlotExtraIdleTime(slot, type, other.getSlotExtraIdleTime(slot, type));
			}
			this.slotRecords.get(slot).index = other.getIndex(slot);
		}
		final IPortSlot otherReturnSlot = other.getReturnSlot();
		if (otherReturnSlot != null) {
			this.setReturnSlot(otherReturnSlot, other.getSlotFeasibleTimeWindow(otherReturnSlot), 0, other.getIndex(otherReturnSlot));
		}

		assert other.equals(this);
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
	public ImmutableList<IPortSlot> getSlots() {
		return slots;
	}

	private SlotWindowRecord getOrCreateSlotRecord(final IPortSlot slot) {
		assert slot != null;
		SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			checkWritable();
			allocation = new SlotWindowRecord();
			slots = ImmutableList.<IPortSlot>builder().addAll(slots).add(slot).build();
			slotRecords = ImmutableMap.<IPortSlot, SlotWindowRecord>builder().putAll(slotRecords).put(slot, allocation).build();
		}
		return allocation;
	}

	@Override
	public void setSlotFeasibleTimeWindow(final IPortSlot slot, final ITimeWindow timeWindow) {

		checkWritable();
		if (getOrCreateSlotRecord(slot).feasibleWindow != null) {
			// assert false;
		}
		getOrCreateSlotRecord(slot).feasibleWindow = timeWindow;
		// Set or update the first port slot and time
		if (firstPortSlot == null || slot == firstPortSlot) {
			firstSlotFeasibleTimeWindow = timeWindow;
			firstPortSlot = slot;
		}
	}

	@Override
	public void setSlot(final IPortSlot slot, final @Nullable ITimeWindow timeWindow, final int duration, final int index) {
		checkWritable();
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

		checkWritable();
		setSlotFeasibleTimeWindow(slot, timeWindow);
		// Return slot should not be in list
		if (slots.contains(slot)) {
			slots = ImmutableList.<IPortSlot>builder().addAll(slots.stream().filter(s -> s != slot).iterator()).build();
		}
		this.returnSlot = slot;
	}

	@Override
	public void setReturnSlot(final IPortSlot slot, final @Nullable ITimeWindow timeWindow, final int duration, final int index) {

		checkWritable();
		setSlot(slot, timeWindow, duration, index);
		// Return slot should not be in list
		if (slots.contains(slot)) {
			slots = ImmutableList.<IPortSlot>builder().addAll(slots.stream().filter(s -> s != slot).iterator()).build();
		}
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
		checkWritable();
		getOrCreateSlotRecord(slot).duration = duration;
	}

	@Override
	public int getSlotExtraIdleTime(final IPortSlot slot, ExplicitIdleTime type) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.extraIdleTimes[type.ordinal()];
		}
		return 0;
	}

	@Override
	public int getSlotTotalExtraIdleTime(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			int sum = 0;
			for (var type : ExplicitIdleTime.values()) {
				sum += allocation.extraIdleTimes[type.ordinal()];
			}
			return sum;
		}
		return 0;
	}

	@Override
	public void setSlotExtraIdleTime(final IPortSlot slot, ExplicitIdleTime type, final int extraIdleTime) {
		checkWritable();
		getOrCreateSlotRecord(slot).extraIdleTimes[type.ordinal()] = extraIdleTime;
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
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
	public @Nullable ITimeWindow getSlotFeasibleTimeWindow(final IPortSlot slot) {
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
	public @Nullable IPortSlot getReturnSlot() {
		return returnSlot;
	}

	@Override
	public int getIndex(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		return allocation != null ? allocation.index : -1;
	}

	@Override
	public @Nullable IRouteOptionBooking getRouteOptionBooking(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.routeOptionBooking;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public void setRouteOptionBooking(final IPortSlot slot, final @Nullable IRouteOptionBooking routeOptionBooking) {
		checkWritable();
		getOrCreateSlotRecord(slot).routeOptionBooking = routeOptionBooking;
	}

	@Override
	public void setSlotNextVoyageOptions(final IPortSlot slot, final AvailableRouteChoices nextVoyageRoute) {
		checkWritable();
		getOrCreateSlotRecord(slot).nextVoyageRoute = nextVoyageRoute;
	}

	@Override
	public void setSlotAdditionalPanamaDetails(final IPortSlot slot, final boolean isConstrainedPanamaJourney, final int additionalPanamaIdleTimeInHours) {
		checkWritable();
		assert additionalPanamaIdleTimeInHours >= 0;
		getOrCreateSlotRecord(slot).isConstrainedPanamaJourney = isConstrainedPanamaJourney;
		getOrCreateSlotRecord(slot).additionalPanamaIdleTimeInHours = additionalPanamaIdleTimeInHours;
	}

	@Override
	public AvailableRouteChoices getSlotNextVoyageOptions(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.nextVoyageRoute;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public boolean getSlotIsNextVoyageConstrainedPanama(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.isConstrainedPanamaJourney;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}

	@Override
	public int getSlotAdditionalPanamaIdleHours(final IPortSlot slot) {
		final SlotWindowRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.additionalPanamaIdleTimeInHours;
		}
		throw new IllegalArgumentException("Unknown port slot");
	}
}
