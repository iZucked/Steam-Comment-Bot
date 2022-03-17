/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.period;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer.InclusionRecord;
import com.mmxlabs.models.lng.transformer.period.PeriodTransformer.SlotLockMode;

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
		public @Nullable ZonedDateTime lowerCutoff;
		public @Nullable ZonedDateTime lowerBoundary;

		public @Nullable ZonedDateTime upperBoundary;
		public @Nullable ZonedDateTime upperCutoff;

		public @Nullable LocalDate promptStart = null;
		public @Nullable LocalDate promptEnd = null;

		public @Nullable LocalDateTime schedulingEndDate = null;
	}

	@NonNull
	public NonNullPair<InclusionType, Position> getObjectInclusionType(@NonNull final InclusionRecord inclusionRecord, @NonNull final PeriodRecord periodRecord) {
		final EObject object = inclusionRecord.object;

		if (object instanceof Cargo) {

			// TODO: Decompose into slots. If slot is in boundary, fix to current vessel (though allowed vessels list), but unlock and allow rewiring.
			// FOB sales stay fixed. DES purchases, only if divertible

			final Cargo cargo = (Cargo) object;
			final NonNullPair<Slot<?>, Slot<?>> slots = getFirstAndLastSlots(cargo);
			final Slot<?> firstSlot = slots.getFirst();

			final Slot<?> lastSlot = slots.getSecond();
			// Is the cargo after the upper cut off? If so OUT
			if (periodRecord.upperCutoff != null) {
				final PortVisit portVisit = inclusionRecord.event.get(firstSlot);
				if (getScheduledStart(firstSlot, portVisit).isAfter(periodRecord.upperCutoff)) {

					for (final Slot<?> s : cargo.getSlots()) {
						inclusionRecord.slotLockMode.put(s, SlotLockMode.VESSEL_AND_DATES);
					}

					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
			// Is the cargo before the lower cutoff - but with the final ballast coming into the period? BOUNDARY
			if (periodRecord.lowerCutoff != null) {
				final PortVisit portVisit = inclusionRecord.event.get(lastSlot);
				if (getScheduledEnd(lastSlot, portVisit).isBefore(periodRecord.lowerCutoff)) {

					// Slots need to be fully locked down
					for (final Slot<?> s : cargo.getSlots()) {
						inclusionRecord.slotLockMode.put(s, SlotLockMode.VESSEL_AND_DATES);
					}

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
				// Assume cargo is IN unless following checks update the state
				InclusionType type = InclusionType.In;
				Position pos = Position.Unknown;
				// If the last slot is after the boundary, then assume IN -> OUT => BOUNDARY
				if (periodRecord.upperBoundary != null) {
					if (lastSlot.getSchedulingTimeWindow().getEnd().isAfter(periodRecord.upperBoundary)) {
						type = InclusionType.Boundary;
						pos = Position.After;

						// Lock the slots to the current vessel, but keep the window flex as they are in the future
						boolean first = true;
						for (final Slot<?> s : cargo.getSortedSlots()) {
							if (first) {
								first = false;
								continue;
							}
							if (s.getSchedulingTimeWindow().getEnd().isAfter(periodRecord.upperBoundary)) {
								inclusionRecord.slotLockMode.put(s, SlotLockMode.VESSEL_ONLY);
							}
						}
					}
				}
				if (periodRecord.lowerBoundary != null) {
					// If the first slot is before the boundary then assume OUT -> IN => BOUNDARY
					if (firstSlot.getSchedulingTimeWindow().getStart().isBefore(periodRecord.lowerBoundary)) {
						type = InclusionType.Boundary;
						if (pos != Position.Unknown) {
							pos = Position.Both;
						} else {
							pos = Position.Before;
						}

						// Historical slots, lock down vessel and window
						for (final Slot<?> s : cargo.getSortedSlots()) {
							if (s.getSchedulingTimeWindow().getEnd().isBefore(periodRecord.lowerBoundary)) {
								inclusionRecord.slotLockMode.put(s, SlotLockMode.VESSEL_AND_DATES);
							}
						}

					}

				}
				return new NonNullPair<>(type, pos);
			}
		}

		else if (object instanceof Slot<?>) {
			final Slot<?> slot = (Slot<?>) object;

			// This should just be open positions and thus are only IN or OUT

			if (periodRecord.upperBoundary != null) {
				if (slot.getSchedulingTimeWindow().getStart().isAfter(periodRecord.upperBoundary)) {
					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
			if (periodRecord.lowerBoundary != null) {
				if (slot.getSchedulingTimeWindow().getEnd().isBefore(periodRecord.lowerBoundary)) {
					return new NonNullPair<>(InclusionType.Out, Position.Before);
				}
			}
		} else if (object instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) object;
			final PortVisit portVisit = inclusionRecord.event.get(event);

			if (periodRecord.upperCutoff != null) {
				// Is the start of the event after the upper cut off? if so exclude
				if (getScheduledStart(event, portVisit).isAfter(periodRecord.upperCutoff)) {
					return new NonNullPair<>(InclusionType.Out, Position.After);
				}
			}
			if (periodRecord.lowerCutoff != null) {
				// Look to see if the ballast leg finishes before the lower cutoff. We assume the next port visit after the event is the marker for end of the ballast leg
				final ZonedDateTime cal = getScheduledStart(event, portVisit).plusDays(event.getDurationInDays());
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
		}

		return new NonNullPair<>(InclusionType.In, Position.Unknown);
	}


	@NonNull
	public NonNullPair<InclusionType, Position> getVesselAvailabilityInclusionType(@NonNull final VesselAvailability vesselAvailability, @NonNull final PeriodRecord periodRecord) {

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

		return new NonNullPair<>(InclusionType.In, Position.Unknown);
	}

	public Position getSlotPosition(final Slot<?> slot, final PeriodRecord periodRecord) {

		// This should just be open positions and thus are only IN or OUT

		if (periodRecord.upperBoundary != null) {
			if (slot.getSchedulingTimeWindow().getStart().isAfter(periodRecord.upperBoundary)) {
				return Position.After;
			}
		}
		if (periodRecord.lowerBoundary != null) {
			if (slot.getSchedulingTimeWindow().getEnd().isBefore(periodRecord.lowerBoundary)) {
				return Position.Before;
			}
		}
		return Position.Unknown;

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
	public NonNullPair<Slot<?>, Slot<?>> getFirstAndLastSlots(@NonNull final Cargo cargo) {
		final List<Slot<?>> sortedSlots = cargo.getSortedSlots();

		final Slot<?> firstSlot = sortedSlots.get(0);
		assert firstSlot != null;

		final Slot<?> lastSlot = sortedSlots.get(sortedSlots.size() - 1);
		assert lastSlot != null;

		return new NonNullPair<>(firstSlot, lastSlot);
	}

	@NonNull
	public ZonedDateTime getScheduledStart(@NonNull final Slot<?> slot, @Nullable final PortVisit portVisit) {
		final ZonedDateTime visitTime = portVisit == null ? null : portVisit.getStart();
		if (visitTime == null) {
			return slot.getSchedulingTimeWindow().getStart();
		} else {
			return visitTime;
		}
	}

	@NonNull
	public ZonedDateTime getScheduledEnd(@NonNull final Slot<?> slot, @Nullable final PortVisit portVisit) {
		final ZonedDateTime visitTime = portVisit == null ? null : portVisit.getEnd();
		if (visitTime == null) {
			return slot.getSchedulingTimeWindow().getEnd().plusHours(slot.getSchedulingTimeWindow().getDuration());
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
