package com.mmxlabs.models.lng.transformer.period;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
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
	public static enum InclusionType {
		In, Out, Boundary
	}

	/**
	 * Defines the period range. boundaries are the optimiser range, cutoff is the additional flex of frozen stuff. In general, lowerCutoff <= lowerBoundary < upperBoundary <= upperCutoff. Note these
	 * parameters could be null if unbounded. However the nulls should be in pairs. E.g. both lowerCutoff and lowerBoundary should be null or not-null. Same for the two upper fields.
	 * 
	 * 
	 */
	public static class PeriodRecord {
		public Date lowerCutoff;
		public Date lowerBoundary;
		public Date upperBoundary;
		public Date upperCutoff;
	}

	public InclusionType getObjectInclusionType(final EObject object, final PeriodRecord periodRecord) {

		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot firstSlot = sortedSlots.get(0);
			final Slot lastSlot = sortedSlots.get(sortedSlots.size() - 1);

			if (periodRecord.upperCutoff != null) {
				if (firstSlot.getWindowStartWithSlotOrPortTime().after(periodRecord.upperCutoff)) {
					return InclusionType.Out;
				}
			}
			if (periodRecord.lowerCutoff != null) {
				if (lastSlot.getWindowEndWithSlotOrPortTime().before(periodRecord.lowerCutoff)) {
					return InclusionType.Out;
				}
			}
			if (periodRecord.upperBoundary != null) {
				if (lastSlot.getWindowEndWithSlotOrPortTime().after(periodRecord.upperBoundary)) {
					return InclusionType.Boundary;
				}
			}
			if (periodRecord.lowerBoundary != null) {
				if (firstSlot.getWindowStartWithSlotOrPortTime().before(periodRecord.lowerBoundary)) {
					return InclusionType.Boundary;
				}

			}
		} else if (object instanceof Slot) {
			final Slot slot = (Slot) object;
			if (periodRecord.upperBoundary != null) {
				if (slot.getWindowStartWithSlotOrPortTime().after(periodRecord.upperBoundary)) {
					return InclusionType.Out;
				}
			}
			if (periodRecord.lowerBoundary != null) {
				if (slot.getWindowEndWithSlotOrPortTime().before(periodRecord.lowerBoundary)) {
					return InclusionType.Out;
				}
			}
		} else if (object instanceof VesselEvent) {
			final VesselEvent event = (VesselEvent) object;
			if (periodRecord.upperCutoff != null) {
				if (event.getStartAfter().after(periodRecord.upperCutoff)) {
					return InclusionType.Out;
				}
			}
			if (periodRecord.lowerCutoff != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(event.getStartBy());
				cal.add(Calendar.DATE, event.getDurationInDays());
				if (cal.getTime().before(periodRecord.lowerCutoff)) {
					return InclusionType.Out;
				}
			}
			// TODO: Check duration
			if (event instanceof CharterOutEvent) {
				// TODO: If in boundary, limit available vessels to assigned vessel
			}
		} else if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			if (periodRecord.lowerCutoff != null) {
				if (vesselAvailability.isSetEndAfter() && vesselAvailability.getEndBy().before(periodRecord.lowerCutoff)) {
					return InclusionType.Out;
				}
			}
			if (periodRecord.upperCutoff != null) {
				if (vesselAvailability.isSetStartAfter() && vesselAvailability.getStartAfter().after(periodRecord.upperCutoff)) {
					return InclusionType.Out;
				}
			}
		}

		return InclusionType.In;
	}

	public InclusionType getObjectInVesselAvailabilityRange(final PortVisit portVisit, final VesselAvailability vesselAvailability) {

		if (vesselAvailability.isSetEndBy()) {
			if (portVisit.getStart().after(vesselAvailability.getEndBy())) {
				return InclusionType.Out;
			}
		}
		if (vesselAvailability.isSetStartAfter()) {
			if (portVisit.getEnd().before(vesselAvailability.getStartAfter())) {
				return InclusionType.Out;
			}
		}

		return InclusionType.In;
	}
}
