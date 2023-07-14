package com.mmxlabs.models.lng.schedule.emissions;

import java.util.HashSet;
import java.util.Set;

import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUnit;

public class EmissionModelUtils {
	
	public static final double LNG_DENSITY_TON_PER_M3 = 0.450;
	public static final double LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL = 2.750;
	
	public static final int METHANE_CO2_EQUIVALENT = 25;
	public static final double MT_TO_GRAMS = 1_000_000.0;
	public static final double GRAMS_TO_TONS = 0.000_001;
	
	private EmissionModelUtils() {
	}
	
	public static double consumedCarbonEquivalentEmissionLNG(final FuelQuantity fuelQuantity) {
		return consumedQuantityLNG(fuelQuantity) * LNG_EMISSION_RATE_TON_CO2_PER_TON_FUEL;
	}
	
	@SuppressWarnings("null")
	public static Long consumedQuantityLNG(final FuelQuantity fuelQuantity) {

		final Set<FuelUnit> uniqueUnits = new HashSet<>();
		for (final FuelAmount fuelAmount : fuelQuantity.getAmounts()) {
			uniqueUnits.add(fuelAmount.getUnit());
		}

		final boolean unitsAreTheSame = uniqueUnits.size() == 1;
		double quantity = 0.0;

		if (unitsAreTheSame) {
			final FuelUnit unitItSelf = uniqueUnits.stream().findAny().orElseThrow();
			quantity = fuelQuantity.getAmounts().stream().map(FuelAmount::getQuantity).reduce(Double::sum).orElse(0.0);

			// Convert whatever to MT
			quantity = switch (unitItSelf) {
			case MMBTU -> throw new IllegalStateException("Bad");
			case M3 -> quantity * LNG_DENSITY_TON_PER_M3;
			case MT -> quantity;
			};
		} else {
			for (final FuelAmount amount : fuelQuantity.getAmounts()) {
				if (amount.getUnit() == FuelUnit.M3) {
					quantity = amount.getQuantity();
				}
			}
			quantity *= LNG_DENSITY_TON_PER_M3;
		}
		return Math.round(quantity);
	}
}
