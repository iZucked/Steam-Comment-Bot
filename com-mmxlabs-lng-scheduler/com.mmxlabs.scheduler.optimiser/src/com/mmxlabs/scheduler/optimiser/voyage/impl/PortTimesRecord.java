/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.voyage.impl;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmxlabs.scheduler.optimiser.cache.AbstractWriteLockable;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IRouteOptionBooking;
import com.mmxlabs.scheduler.optimiser.voyage.ExplicitIdleTime;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * A small class storing the port arrival time and visit duration for a
 * {@link VoyagePlan}. This class may or may not include the times for the last
 * element in the plan depending on how it is created. For scheduling purposes
 * the end element should be included. Often for pricing purposes the end
 * element can be ignored.
 * 
 * Note, the order slots are added is important. They should be added in
 * scheduled order. The calls to {@link #getFirstSlotTime()} and
 * {@link #getFirstSlot()} are expected to be bound the first slot added to the
 * instance.
 * 
 * @author Simon Goodall
 * 
 */
@NonNullByDefault
public final class PortTimesRecord extends AbstractWriteLockable implements IPortTimesRecord {

	private static final String UNKNOWN_PORT_SLOT = "Unknown port slot";

	private static final class SlotVoyageRecord {
		private int startTime;
		private int duration;
		private int[] extraIdleTimes = new int[ExplicitIdleTime.values().length];
		private AvailableRouteChoices nextVoyageRoute = AvailableRouteChoices.OPTIMAL;
		private int additionalPanamaIdleTime;
		private int maxAdditionalPanamaIdleTime;
		private @Nullable IRouteOptionBooking routeOptionBooking;

		@Override
		public boolean equals(final @Nullable Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof SlotVoyageRecord) {
				final SlotVoyageRecord other = (SlotVoyageRecord) obj;
				return startTime == other.startTime //
						&& duration == other.duration //
						&& Arrays.equals(extraIdleTimes, other.extraIdleTimes) //
						&& nextVoyageRoute == other.nextVoyageRoute //
						&& additionalPanamaIdleTime == other.additionalPanamaIdleTime //
						&& maxAdditionalPanamaIdleTime == other.maxAdditionalPanamaIdleTime //
						&& Objects.equals(routeOptionBooking, other.routeOptionBooking);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(startTime, duration, nextVoyageRoute);
		}
	}

	private ImmutableMap<IPortSlot, SlotVoyageRecord> slotRecords = ImmutableMap.of();
	private ImmutableList<IPortSlot> slots = ImmutableList.of();
	private int firstSlotTime = Integer.MAX_VALUE;
	private @Nullable IPortSlot firstPortSlot = null;
	private @Nullable IPortSlot returnSlot;

	public PortTimesRecord() {

	}

	/**
	 * Copy constructor.
	 * 
	 * @param other
	 */
	public PortTimesRecord(final IPortTimesRecord other) {
		for (final IPortSlot slot : other.getSlots()) {
			this.setSlotTime(slot, other.getSlotTime(slot));
			this.setSlotDuration(slot, other.getSlotDuration(slot));
			this.setSlotAdditionalPanamaIdleHours(slot, other.getSlotAdditionaPanamaIdleHours(slot));
			this.setSlotMaxAvailablePanamaIdleHours(slot, other.getSlotMaxAdditionaPanamaIdleHours(slot));
			this.setRouteOptionBooking(slot, other.getRouteOptionBooking(slot));
			this.setSlotNextVoyageOptions(slot, other.getSlotNextVoyageOptions(slot));
			for (var type : ExplicitIdleTime.values()) {
				this.setSlotExtraIdleTime(slot, type, other.getSlotExtraIdleTime(slot, type));
			}
		}
		final IPortSlot otherReturnSlot = other.getReturnSlot();
		if (otherReturnSlot != null) {
			this.setReturnSlotTime(otherReturnSlot, other.getSlotTime(otherReturnSlot));
		}

		assert this.equals(other);
	}
	
	public static IPortTimesRecord from(final IPortTimesRecord other) {
		PortTimesRecord result = new PortTimesRecord();
		for (final IPortSlot slot : other.getSlots()) {
			result.setSlotTime(slot, other.getSlotTime(slot));
			result.setSlotDuration(slot, other.getSlotDuration(slot));
			result.setSlotAdditionalPanamaIdleHours(slot, other.getSlotAdditionaPanamaIdleHours(slot));
			result.setSlotMaxAvailablePanamaIdleHours(slot, other.getSlotMaxAdditionaPanamaIdleHours(slot));
			result.setRouteOptionBooking(slot, other.getRouteOptionBooking(slot));
			result.setSlotNextVoyageOptions(slot, other.getSlotNextVoyageOptions(slot));
			for (var type : ExplicitIdleTime.values()) {
				result.setSlotExtraIdleTime(slot, type, other.getSlotExtraIdleTime(slot, type));
			}
		}
		final IPortSlot otherReturnSlot = other.getReturnSlot();
		if (otherReturnSlot != null) {
			result.setReturnSlotTime(otherReturnSlot, other.getSlotTime(otherReturnSlot));
		}
		return result;
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

		if (returnSlot != null) {
			final SlotVoyageRecord slotAllocation = slotRecords.get(returnSlot);
			builder.append(" return ");
			builder.append(String.format(slotFormat, returnSlot.getId(), slotAllocation.startTime));

		}

		return builder.toString();
	}

	@Override
	public ImmutableList<IPortSlot> getSlots() {
		return slots;
	}

	private SlotVoyageRecord getOrCreateSlotRecord(final IPortSlot slot) {
		assert slot != null;
		SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation == null) {
			checkWritable();
			allocation = new SlotVoyageRecord();
			slots = ImmutableList.<IPortSlot>builder().addAll(slots).add(slot).build();
			slotRecords = ImmutableMap.<IPortSlot, SlotVoyageRecord>builder().putAll(slotRecords).put(slot, allocation).build();
		}
		return allocation;
	}

	@Override
	public int getSlotTime(final IPortSlot slot) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.startTime;
		}
		throw new IllegalArgumentException(UNKNOWN_PORT_SLOT);
	}

	public void setReturnSlotTime(final IPortSlot slot, final int time) {

		checkWritable();
		setSlotTime(slot, time);
		// Return slot should not be in list
		if (slots.contains(slot)) {
			slots = ImmutableList.<IPortSlot>builder().addAll(slots.stream().filter(s -> s != slot).iterator()).build();
		}
		this.returnSlot = slot;
	}

	@Override
	public void setSlotTime(final IPortSlot slot, final int time) {

		checkWritable();

		assert time != Integer.MAX_VALUE; // Check for max value
		assert time < Integer.MAX_VALUE - 100_000; // Check we are not near max value

		assert time >= 0;
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

	@Override
	public int getSlotExtraIdleTime(final IPortSlot slot, ExplicitIdleTime type) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.extraIdleTimes[type.ordinal()];
		}
		return 0;
	}

	@Override
	public int getSlotTotalExtraIdleTime(final IPortSlot slot) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
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
	public void setSlotDuration(final IPortSlot slot, final int duration) {

		checkWritable();

		getOrCreateSlotRecord(slot).duration = duration;
	}

	@Override
	public void setSlotExtraIdleTime(final IPortSlot slot, ExplicitIdleTime type, final int extraIdleTime) {

		checkWritable();

		getOrCreateSlotRecord(slot).extraIdleTimes[type.ordinal()] = extraIdleTime;
	}

	@Override
	public void setSlotNextVoyageOptions(final IPortSlot slot, final AvailableRouteChoices nextVoyageRoute) {

		checkWritable();

		getOrCreateSlotRecord(slot).nextVoyageRoute = nextVoyageRoute;
	}

	@Override
	public AvailableRouteChoices getSlotNextVoyageOptions(final IPortSlot slot) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.nextVoyageRoute;
		}
		throw new IllegalArgumentException(UNKNOWN_PORT_SLOT);
	}

	@Override
	public boolean equals(final @Nullable Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof PortTimesRecord) {
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
		return firstSlotTime;
	}

	@Override
	public IPortSlot getFirstSlot() {
		final IPortSlot pFirstPortSlot = firstPortSlot;
		if (pFirstPortSlot == null) {
			throw new IllegalStateException("#getFirstSlot called before slots have been added");
		}
		return pFirstPortSlot;
	}

	@Override
	public @Nullable IPortSlot getReturnSlot() {
		return returnSlot;
	}

	@Override
	public @Nullable IRouteOptionBooking getRouteOptionBooking(final IPortSlot slot) {
		final SlotVoyageRecord allocation = slotRecords.get(slot);
		if (allocation != null) {
			return allocation.routeOptionBooking;
		}
		throw new IllegalArgumentException(UNKNOWN_PORT_SLOT);
	}

	@Override
	public void setRouteOptionBooking(final IPortSlot slot, @Nullable final IRouteOptionBooking routeOptionBooking) {
		checkWritable();

		getOrCreateSlotRecord(slot).routeOptionBooking = routeOptionBooking;
	}

	public PortTimesRecord copy() {
		return new PortTimesRecord(this);
	}

	@Override
	public void setSlotMaxAvailablePanamaIdleHours(final IPortSlot from, final int maxIdleTimeAvailable) {
		checkWritable();
		assert maxIdleTimeAvailable >= 0;
		getOrCreateSlotRecord(from).maxAdditionalPanamaIdleTime = maxIdleTimeAvailable;
	}

	@Override
	public void setSlotAdditionalPanamaIdleHours(final IPortSlot from, final int additionalPanamaTime) {
		checkWritable();
		assert additionalPanamaTime >= 0;
		getOrCreateSlotRecord(from).additionalPanamaIdleTime = additionalPanamaTime;
	}

	@Override
	public int getSlotAdditionaPanamaIdleHours(@NonNull final IPortSlot slot) {
		return getOrCreateSlotRecord(slot).additionalPanamaIdleTime;
	}

	@Override
	public int getSlotMaxAdditionaPanamaIdleHours(@NonNull final IPortSlot slot) {
		return getOrCreateSlotRecord(slot).maxAdditionalPanamaIdleTime;
	}
}
