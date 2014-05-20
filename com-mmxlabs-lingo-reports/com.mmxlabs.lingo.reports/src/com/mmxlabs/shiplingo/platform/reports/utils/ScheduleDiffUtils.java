/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * @since 3.0
 * @noinstantiate This class is not intended to be instantiated by clients.
 */
public class ScheduleDiffUtils {
	public static boolean isElementDifferent(final EObject pinnedObject, final EObject otherObject) {

		// oops - dynamic eobjects coming here...

		if (pinnedObject == null || otherObject == null) {
			return true;
		}

		if (pinnedObject instanceof GeneratedCharterOut || otherObject instanceof GeneratedCharterOut) {
			// Specific to each scenario/schedule so always mark as different.
			return true;
		}

		if (pinnedObject instanceof CargoAllocation && otherObject instanceof CargoAllocation) {
			CargoAllocation ref = null;
			CargoAllocation ca = null;
			ref = (CargoAllocation) pinnedObject;

			ca = (CargoAllocation) otherObject;

			// Check vessel
			if ((ca.getSequence().getVesselAvailability() == null) != (ref.getSequence().getVesselAvailability() == null)) {
				return true;
			} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
				return true;
			} else if (ca.getSequence().getVesselAvailability() != null
					&& (!ca.getSequence().getVesselAvailability().getVessel().getName().equals(ref.getSequence().getVesselAvailability().getVessel().getName()))) {
				return true;
			} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVesselClass().getName().equals(ref.getSequence().getVesselClass().getName()))) {
				return true;
			}

			EList<SlotAllocation> caSlotAllocations = ca.getSlotAllocations();
			EList<SlotAllocation> refSlotAllocations = ref.getSlotAllocations();

			if (caSlotAllocations.size() != refSlotAllocations.size()) {
				return true;
			}

			for (int i = 0; i < caSlotAllocations.size(); ++i) {
				SlotAllocation caAllocation = caSlotAllocations.get(i);
				SlotAllocation refAllocation = refSlotAllocations.get(i);

				{
					final Slot caSlot = caAllocation.getSlot();
					final Slot refSlot = refAllocation.getSlot();
					final String caName = caSlot == null ? null : caSlot.getName();
					final String refName = refSlot == null ? null : refSlot.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}

				{
					final Port caPort = caAllocation.getPort();
					final Port refPort = refAllocation.getPort();
					final String caName = caPort == null ? null : caPort.getName();
					final String refName = refPort == null ? null : refPort.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}

				{
					final Contract caContract = caAllocation.getContract();
					final Contract refContract = refAllocation.getContract();
					final String caName = caContract == null ? null : caContract.getName();
					final String refName = refContract == null ? null : refContract.getName();

					if (!Equality.isEqual(caName, refName)) {
						return true;
					}
				}
			}
		} else if (pinnedObject instanceof SlotVisit && otherObject instanceof SlotVisit) {
			SlotVisit ref = null;
			SlotVisit ca = null;
			ref = (SlotVisit) pinnedObject;

			ca = (SlotVisit) otherObject;

			if ((ca.getSequence().getVesselAvailability() == null) != (ref.getSequence().getVesselAvailability() == null)) {
				return true;
			} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
				return true;
			} else if (ca.getSequence().getVesselAvailability() != null
					&& (!ca.getSequence().getVesselAvailability().getVessel().getName().equals(ref.getSequence().getVesselAvailability().getVessel().getName()))) {
				return true;
			} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVesselClass().getName().equals(ref.getSequence().getVesselClass().getName()))) {
				return true;
			}

			{
				final Slot caSlot = ca.getSlotAllocation().getSlot();
				final Slot refSlot = ref.getSlotAllocation().getSlot();
				final String caName = caSlot == null ? null : caSlot.getName();
				final String refName = refSlot == null ? null : refSlot.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}

			{
				final Port caPort = ca.getSlotAllocation().getPort();
				final Port refPort = ref.getSlotAllocation().getPort();
				final String caName = caPort == null ? null : caPort.getName();
				final String refName = refPort == null ? null : refPort.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}

			{
				final Contract caContract = ca.getSlotAllocation().getContract();
				final Contract refContract = ref.getSlotAllocation().getContract();
				final String caName = caContract == null ? null : caContract.getName();
				final String refName = refContract == null ? null : refContract.getName();

				if (!Equality.isEqual(caName, refName)) {
					return true;
				}
			}
			return isElementDifferent(ref.getSlotAllocation().getCargoAllocation(), ca.getSlotAllocation().getCargoAllocation());
		} else if (pinnedObject instanceof Event && otherObject instanceof Event) {
			final Event vev = (Event) otherObject;
			final Event ref = (Event) pinnedObject;

			final int refTime = getEventDuration(ref);
			final int vevTime = getEventDuration(vev);

			// 3 Days
			if (Math.abs(refTime - vevTime) > 3 * 24) {
				return true;
			}

		}

		return false;
	}

	public static int getEventDuration(final Event event) {

		int duration = event.getDuration();
		Event next = event.getNextEvent();
		while (next != null && !isSegmentStart(next)) {
			duration += next.getDuration();
			next = next.getNextEvent();
		}
		return duration;

	}

	public static boolean isSegmentStart(final Event event) {
		if (event instanceof VesselEventVisit) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof SlotVisit) {
			if (((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
				return true;
			}
		}
		return false;
	}

}
