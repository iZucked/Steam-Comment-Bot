package com.mmxlabs.shiplingo.platform.reports.utils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.common.Equality;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.Contract;
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
			if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
				return true;
			} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
				return true;
			} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
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

				if (!caAllocation.getPort().getName().equals(refAllocation.getPort().getName())) {
					return true;
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

			if ((ca.getSequence().getVessel() == null) != (ref.getSequence().getVessel() == null)) {
				return true;
			} else if ((ca.getSequence().getVesselClass() == null) != (ref.getSequence().getVesselClass() == null)) {
				return true;
			} else if (ca.getSequence().getVessel() != null && (!ca.getSequence().getVessel().getName().equals(ref.getSequence().getVessel().getName()))) {
				return true;
			} else if (ca.getSequence().getVesselClass() != null && (!ca.getSequence().getVesselClass().getName().equals(ref.getSequence().getVesselClass().getName()))) {
				return true;
			}

			if (!ca.getPort().getName().equals(ref.getPort().getName())) {
				return true;
			}

			final Contract caContract = ca.getSlotAllocation().getContract();
			final Contract refContract = ref.getSlotAllocation().getContract();
			final String caName = caContract == null ? null : caContract.getName();
			final String refName = refContract == null ? null : refContract.getName();

			if (!Equality.isEqual(caName, refName)) {
				return true;
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
