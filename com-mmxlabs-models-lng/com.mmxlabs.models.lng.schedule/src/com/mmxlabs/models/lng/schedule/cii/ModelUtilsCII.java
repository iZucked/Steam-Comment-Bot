package com.mmxlabs.models.lng.schedule.cii;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.emissions.EmissionModelUtils;

public class ModelUtilsCII {
	
	private ModelUtilsCII() {
	}

	public static void processAccumulatableEventModelForCII(final CIIAccumulatableEventModel model, final Vessel vessel, final Event event) {
		model.setCIIVessel(vessel);
		model.setCIIEvent(event);
		model.setCIIStartDate(event.getStart().toLocalDate());
		model.setCIIEndDate(event.getEnd().toLocalDate());
		if (event instanceof FuelUsage fuelUsageEvent) {
			processFuelUsageEvent(model, fuelUsageEvent);
		}
	}

	private static void processFuelUsageEvent(final CIIAccumulatableEventModel model, FuelUsage fuelUsageEvent) {
		for (final FuelQuantity fuelQuantity : fuelUsageEvent.getFuels()) {
			final Fuel fuel = fuelQuantity.getFuel();
			final BaseFuel baseFuel = fuelQuantity.getBaseFuel();
			final FuelAmount fuelAmount = fuelQuantity.getAmounts().get(0);
			switch (fuel) {
			case BASE_FUEL, PILOT_LIGHT:
				if (fuelQuantity.getAmounts().get(0).getUnit() != FuelUnit.MT) {
					throw new IllegalStateException();
				}
				model.addToTotalEmissionForCII(Math.round(fuelAmount.getQuantity() * baseFuel.getEmissionRate()));
				break;
			case FBO, NBO:
				model.addToTotalEmissionForCII(EmissionModelUtils.consumedCarbonEquivalentEmissionLNG(fuelQuantity));
				break;
			default:
			}
		}
	}
}
