/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;

/**
 * Helper class used by PeriodTransformer to determine which elements are included and which are excluded.
 * 
 * @author Simon Goodall
 * 
 */
public class InclusionChecker {
	/**
	 * Enum defining where an object lies. The value {@link #In} is inside the optimisation range and can be optimised. The value {@link #Out} is outside range and should be excluded. The value
	 * {@link #Boundary} is within the boundary and cutoff portion and should be included but frozen.
	 * 
	 * 
	 */
	@NonNullByDefault
	public static enum InclusionType {
		In, Out, Boundary
	}

	/**
	 * Enum defining the position of an object in relation the Period bounds. For objects with an InclusionType of {@link InclusionType#Out} or {@link InclusionType#Boundary} a position of
	 * {@link #Before} or {@link #After} should be available. For a {@link InclusionType#In} only {@link #Unknown} is defined.
	 * 
	 */
	@NonNullByDefault
	public enum Position {
		Before, After, Unknown, Both
	}

	/**
	 * Defines the period range. boundaries are the optimiser range, cutoff is the additional flex of frozen stuff. In general, lowerCutoff <= lowerBoundary < upperBoundary <= upperCutoff. Note these
	 * parameters could be null if unbounded. However the nulls should be in pairs. E.g. both lowerCutoff and lowerBoundary should be null or not-null. Same for the two upper fields.
	 * 
	 * 
	 */
	public static class PeriodRecord {
		@Nullable
		public ZonedDateTime lowerCutoff;
		@Nullable
		public ZonedDateTime lowerBoundary;

		@Nullable
		public ZonedDateTime upperBoundary;
		@Nullable
		public ZonedDateTime upperCutoff;

		@Nullable
		LocalDate promptStart = null;
		@Nullable
		LocalDate promptEnd = null;
	}

	@NonNull
	public NonNullPair<InclusionType, Position> getObjectInclusionType(@NonNull final EObject object, @NonNull final Map<EObject, PortVisit> scheduledEventMap,
			@NonNull final PeriodRecord periodRecord) {

		if (object instanceof Cargo) {

			// TODO: Decompose into slots. If slot is in boundary, fix to current vessel (though allowed vessels list), but unlock and allow rewiring.
			// FOB sales stay fixed. DES purchases, only if divertible

			final Cargo cargo = (Cargo) object;
			final NonNullPair<Slot, Slot> slots = getFirstAndLastSlots(cargo);
			final Slot firstSlot = slots.getFirst();
			final Slot lastSlot = slots.getSecond();
			if (periodRecord.upperCutoff != null) {
				final PortVisit portVisit = scheduledEventMap.get(firstSlot);
				if (getScheduledStart(firstSlot, portVisit).isAfter(periodRecord.upperCutoff)) {
					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
			if (periodRecord.lowerCutoff != null) {
				final PortVisit portVisit = scheduledEventMap.get(lastSlot);
				if (getScheduledEnd(lastSlot, portVisit).isBefore(periodRecord.lowerCutoff)) {
					if (portVisit != null) {
						Event evt = portVisit.getNextEvent();
						while (evt != null && !(evt instanceof PortVisit)) {
							evt = evt.getNextEvent();
						}

						if (evt instanceof PortVisit) {
							final PortVisit portVisit2 = (PortVisit) evt;
							if (periodRecord.lowerBoundary != null) {
								if (!evt.getStart().isBefore(periodRecord.lowerBoundary)) {
									return new NonNullPair<>(InclusionType.Boundary, Position.Before);
								}
							} else {
								if (!evt.getStart().isBefore(periodRecord.lowerCutoff)) {
									return new NonNullPair<>(InclusionType.Boundary, Position.Before);
								}
							}
						}
					}
					return new NonNullPair<>(InclusionType.Out, Position.Before);
				}
			}
			{
				InclusionType type = InclusionType.In;
				Position pos = Position.Unknown;
				if (periodRecord.upperBoundary != null) {
					if (lastSlot.getWindowEndWithSlotOrPortTime().isAfter(periodRecord.upperBoundary)) {
						type = InclusionType.Boundary;
						pos = Position.After;
					}
				}
				if (periodRecord.lowerBoundary != null) {
					if (firstSlot.getWindowStartWithSlotOrPortTime().isBefore(periodRecord.lowerBoundary)) {
						type = InclusionType.Boundary;
						if (pos != Position.Unknown) {
							pos = Position.Both;
						} else {
							pos = Position.Before;
						}
					}

				}
				return new NonNullPair<>(type, pos);
			}
		} else if (object instanceof Slot) {
			final Slot slot = (Slot) object;

			// This should just be open positions and thus are only IN or OUT

			if (periodRecord.upperBoundary != null) {
				if (slot.getWindowStartWithSlotOrPortTime().isAfter(periodRecord.upperBoundary)) {
					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
			if (periodRecord.lowerBoundary != null) {
				if (slot.getWindowEndWithSlotOrPortTime().isBefore(periodRecord.lowerBoundary)) {
					return new NonNullPair<>(InclusionType.Out, Position.Before);
				}
			}
		} else if (object instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) object;
			final PortVisit portVisit = scheduledEventMap.get(event);

			if (periodRecord.upperCutoff != null) {
				if (getScheduledStart(event, scheduledEventMap.get(event)).isAfter(periodRecord.upperCutoff)) {
					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
			if (periodRecord.lowerCutoff != null) {
				final ZonedDateTime cal = getScheduledStart(event, scheduledEventMap.get(event)).plusDays(event.getDurationInDays());
				if (cal.isBefore(periodRecord.lowerCutoff)) {
					if (portVisit != null) {
						Event evt = portVisit.getNextEvent();
						while (evt != null && !(evt instanceof PortVisit)) {
							evt = evt.getNextEvent();
						}

						if (evt instanceof PortVisit) {
							if (periodRecord.lowerBoundary != null) {
								if (!evt.getStart().isBefore(periodRecord.lowerBoundary)) {
									return new NonNullPair<>(InclusionType.Boundary, Position.Before);
								}
							} else {
								if (!evt.getStart().isBefore(periodRecord.lowerCutoff)) {
									return new NonNullPair<>(InclusionType.Boundary, Position.Before);
								}
							}
						}
					}
					return new NonNullPair<>(InclusionType.Out, Position.Before);
				}
			}
			{
				InclusionType type = InclusionType.In;
				Position pos = Position.Unknown;
				if (periodRecord.upperBoundary != null) {
					if (event.getStartByAsDateTime().plusDays(event.getDurationInDays()).isAfter(periodRecord.upperBoundary)) {
						type = InclusionType.Boundary;
						pos = Position.After;
					}
				}
				if (periodRecord.lowerBoundary != null) {
					if (event.getStartAfterAsDateTime().isBefore(periodRecord.lowerBoundary)) {
						type = InclusionType.Boundary;
						if (pos != Position.Unknown) {
							pos = Position.Both;
						} else {
							pos = Position.Before;
						}
					}

				}
				return new NonNullPair<>(type, pos);
			}
		} else if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			if (periodRecord.lowerCutoff != null) {
				if (vesselAvailability.isSetEndBy() && vesselAvailability.getEndByAsDateTime().isBefore(periodRecord.lowerCutoff)) {
					return new NonNullPair<>(InclusionType.Out, Position.Before);
				}
			}
			if (periodRecord.upperCutoff != null) {
				if (vesselAvailability.isSetStartAfter() && vesselAvailability.getStartAfterAsDateTime().isAfter(periodRecord.upperCutoff)) {
					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
		}

		return new NonNullPair<>(InclusionType.In, Position.Unknown);
	}

	@NonNull
	public InclusionType getObjectInVesselAvailabilityRange(@NonNull final PortVisit portVisit, @NonNull final VesselAvailability vesselAvailability) {

		if (vesselAvailability.isSetEndBy()) {
			if (portVisit.getStart().isAfter(vesselAvailability.getEndByAsDateTime())) {
				return InclusionType.Out;
			}
		}
		if (vesselAvailability.isSetStartAfter())

		{
			if (portVisit.getEnd().isBefore(vesselAvailability.getStartAfterAsDateTime())) {
				return InclusionType.Out;
			}
		}

		return InclusionType.In;

	}

	@NonNull
	public NonNullPair<Slot, Slot> getFirstAndLastSlots(@NonNull final Cargo cargo) {
		final List<Slot> sortedSlots = cargo.getSortedSlots();

		final Slot firstSlot = sortedSlots.get(0);
		assert firstSlot != null;

		final Slot lastSlot = sortedSlots.get(sortedSlots.size() - 1);
		assert lastSlot != null;

		return new NonNullPair<Slot, Slot>(firstSlot, lastSlot);
	}

	@NonNull
	public ZonedDateTime getScheduledStart(@NonNull final Slot slot, @Nullable final PortVisit portVisit) {
		final ZonedDateTime visitTime = portVisit == null ? null : portVisit.getStart();
		if (visitTime == null) {
			return slot.getWindowStartWithSlotOrPortTime();
		} else {
			return visitTime;
		}
	}

	@NonNull
	public ZonedDateTime getScheduledEnd(@NonNull final Slot slot, @Nullable final PortVisit portVisit) {
		final ZonedDateTime visitTime = portVisit == null ? null : portVisit.getEnd();
		if (visitTime == null) {
			return slot.getWindowEndWithSlotOrPortTime().plusHours(slot.getDuration());
		} else {
			return visitTime;
		}
	}

	@NonNull
	public ZonedDateTime getScheduledStart(@NonNull final VesselEvent vesselEvent, @Nullable final PortVisit portVisit) {
		final ZonedDateTime visitTime = portVisit == null ? null : portVisit.getStart();
		if (visitTime == null) {
			return vesselEvent.getStartAfterAsDateTime();
		} else {
			return visitTime;
		}
	}
}
