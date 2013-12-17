package com.mmxlabs.models.lng.transformer.its.tests.calculation;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.fleet.FuelConsumption;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * Start of a utils class to navigate a data model to fins particular elements
 * 
 * TODO: Start replacing existing code/classes to use this class
 * 
 * TODO: WIP
 * 
 * @noinstantiate
 * @author Simon Goodall
 * 
 */
public final class ScheduleTools {

	public static @Nullable
	CargoAllocation findCargoAllocation(@NonNull String cargoID, @NonNull Schedule schedule) {
		for (CargoAllocation ca : schedule.getCargoAllocations()) {
			if (cargoID.equals(ca.getName())) {
				return ca;
			}
		}

		return null;
	}

	public static int getFuelQuantity(@NonNull Fuel fuel, @NonNull FuelUnit unit, @NonNull FuelUsage fuelUsage) {
		for (FuelQuantity fq : fuelUsage.getFuels()) {
			if (fq.getFuel().equals(fuel)) {
				for (FuelAmount fa : fq.getAmounts()) {
					if (fa.getUnit().equals(unit)) {
						return fa.getQuantity();
					}
				}
			}

		}
		return 0;
	}

	public static double getFuelConsumption(@NonNull List<FuelConsumption> consumptions, double speed) {

		double lowerBound = 0.0;
		double upperBound = Double.MAX_VALUE;
		FuelConsumption lowerConsumption = null;
		FuelConsumption upperConsumption = null;

		for (FuelConsumption fc : consumptions) {
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
			double diff = upperBound - lowerBound;
			double relative = (speed - lowerBound) / diff;

			double consumption = (upperConsumption.getConsumption() - lowerConsumption.getConsumption()) * relative + lowerConsumption.getConsumption();
			return Math.round(consumption);
		}

		return 0;
	}
}
