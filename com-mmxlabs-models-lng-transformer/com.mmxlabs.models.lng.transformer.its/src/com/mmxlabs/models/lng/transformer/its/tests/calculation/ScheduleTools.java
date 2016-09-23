/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.its.tests.calculation;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * Start of a utils class to navigate a data model to fins particular elements
 * 
 * TODO: Start replacing existing code/classes to use this class
 * 
 * @noinstantiate
 * @author Simon Goodall
 * 
 */
public final class ScheduleTools {

	@Nullable
	public static CargoAllocation findCargoAllocation(@NonNull final String cargoID, @NonNull final Schedule schedule) {
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			if (cargoID.equals(ca.getName())) {
				return ca;
			}
		}

		return null;
	}

	@Nullable
	public static CargoAllocation findCargoAllocationByDischargeID(@NonNull final String dischargeID, @NonNull final Schedule schedule) {
		for (final CargoAllocation ca : schedule.getCargoAllocations()) {
			for (final SlotAllocation sa : ca.getSlotAllocations()) {
				final Slot s = sa.getSlot();
				if (s instanceof DischargeSlot) {
					if (dischargeID.equals(s.getName())) {
						return ca;
					}
				}
			}
		}

		return null;
	}

	public static int getFuelQuantity(@NonNull final Fuel fuel, @NonNull final FuelUnit unit, @NonNull final FuelUsage fuelUsage) {
		for (final FuelQuantity fq : fuelUsage.getFuels()) {
			if (fq.getFuel().equals(fuel)) {
				for (final FuelAmount fa : fq.getAmounts()) {
					if (fa.getUnit().equals(unit)) {
						return fa.getQuantity();
					}
				}
			}

		}
		return 0;
	}

	public static double getFuelConsumption(@NonNull final List<FuelConsumption> consumptions, final double speed) {

		double lowerBound = 0.0;
		double upperBound = Double.MAX_VALUE;
		FuelConsumption lowerConsumption = null;
		FuelConsumption upperConsumption = null;

		for (final FuelConsumption fc : consumptions) {
			// Exact match
			if (Double.compare(fc.getSpeed(), speed) == 0) {
				return fc.getConsumption();
			}
			if (fc.getSpeed() > speed) {
				upperBound = fc.getSpeed();
				upperConsumption = fc;
			} else if (fc.getSpeed() < speed) {
				if (fc.getSpeed() > lowerBound) {
					lowerBound = fc.getSpeed();
					lowerConsumption = fc;
				}
			}
		}

		if (lowerConsumption != null && upperConsumption != null) {
			final double diff = upperBound - lowerBound;
			final double relative = (speed - lowerBound) / diff;

			final double consumption = (upperConsumption.getConsumption() - lowerConsumption.getConsumption()) * relative + lowerConsumption.getConsumption();
			return Math.round(consumption);
		}

		return 0;
	}

	/**
	 * Get allocated slot's price
	 * 
	 * @param scenario
	 * @param slot
	 * @return
	 */
	public static double getPrice(LNGScenarioModel scenario, Slot slot) {
		EList<SlotAllocation> slotAllocations = scenario.getScheduleModel().getSchedule().getSlotAllocations();
		SlotAllocation interestingSlotAllocation = null;
		for (SlotAllocation slotAllocation : slotAllocations) {
			if (slotAllocation.getSlot().equals(slot)) {
				interestingSlotAllocation = slotAllocation;
			}
		}
		assert (interestingSlotAllocation != null);
		return interestingSlotAllocation.getPrice();
	}

}
