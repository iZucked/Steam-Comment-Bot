/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.diff.utils;

import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.google.common.collect.Sets;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
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
			return calculateLegCost(cargoAllocation, allocation);

		}
		return null;
	}

	public static Integer calculateLegCost(final CargoAllocation cargoAllocation, final SlotAllocation allocation) {
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
		return null;
	}

	public static Integer calculateLegCost(final EventGrouping grouping) {
		if (grouping != null) {

			// boolean collecting = false;
			int total = 0;
			for (final Event event : grouping.getEvents()) {
				if (event instanceof SlotVisit) {
					SlotVisit slotVisit = (SlotVisit) event;
					// total += slotVisit.getFuelCost();
					total += slotVisit.getCharterCost();
					total += slotVisit.getPortCost();
				} else if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					// total += journey.getFuelCost();
					total += journey.getCharterCost();
					total += journey.getToll();
				} else if (event instanceof Idle) {
					final Idle idle = (Idle) event;
					// total += idle.getFuelCost();
					total += idle.getCharterCost();
				}
				if (event instanceof FuelUsage) {
					final FuelUsage fuelUsage = (FuelUsage) event;
					total += getFuelCost(fuelUsage, Fuel.BASE_FUEL, Fuel.PILOT_LIGHT);
				}

			}

			return total;
		}
		return null;
	}

	protected static int getFuelCost(final FuelUsage fuelUser, final Fuel... fuels) {
		final Set<Fuel> fuelsOfInterest = Sets.newHashSet(fuels);
		int sum = 0;
		if (fuelUser != null) {
			final EList<FuelQuantity> fuelQuantities = fuelUser.getFuels();
			for (final FuelQuantity fq : fuelQuantities) {
				if (fuelsOfInterest.contains(fq.getFuel())) {
					sum += fq.getCost();
				}
			}
		}
		return sum;
	}

}
