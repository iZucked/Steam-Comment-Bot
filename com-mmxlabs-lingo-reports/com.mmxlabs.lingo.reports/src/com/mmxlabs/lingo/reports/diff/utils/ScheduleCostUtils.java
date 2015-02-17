package com.mmxlabs.lingo.reports.diff.utils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class ScheduleCostUtils {

	public static Integer calculateLegCost(final Object object, final EStructuralFeature cargoAllocationRef, final EStructuralFeature allocationRef) {
		if (object instanceof EObject) {
			final EObject eObject = (EObject) object;
			final CargoAllocation cargoAllocation = (CargoAllocation) eObject.eGet(cargoAllocationRef);
			final SlotAllocation allocation = (SlotAllocation) eObject.eGet(allocationRef);
			if (allocation != null && cargoAllocation != null) {

				boolean collecting = false;
				int total = 0;
				for (final Event event : cargoAllocation.getEvents()) {
					if (event instanceof SlotVisit) {
						final SlotVisit slotVisit = (SlotVisit) event;
						if (allocation.getSlotVisit() == event) {
							collecting = true;
						} else {
							if (collecting) {
								// Finished!
								break;
							}
						}
						if (collecting) {
							total += slotVisit.getFuelCost();
							total += slotVisit.getCharterCost();
							total += slotVisit.getPortCost();
						}

					} else if (event instanceof Journey) {
						final Journey journey = (Journey) event;
						if (collecting) {
							total += journey.getFuelCost();
							total += journey.getCharterCost();
							total += journey.getToll();
						}
					} else if (event instanceof Idle) {
						final Idle idle = (Idle) event;
						if (collecting) {
							total += idle.getFuelCost();
							total += idle.getCharterCost();
						}
					}
				}

				return total;
			}

		}
		return null;
	}

}
