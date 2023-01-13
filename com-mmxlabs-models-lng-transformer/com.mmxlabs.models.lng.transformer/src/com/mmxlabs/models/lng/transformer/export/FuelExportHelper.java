/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IBaseFuel;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelKey;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.LNGFuelKeys;

public class FuelExportHelper {

	private FuelExportHelper() {

	}

	public static final Map<Fuel, List<Function<IVessel, FuelKey>>> portFuelComponentNames = new EnumMap<>(Fuel.class);
	public static final Map<Fuel, List<Function<IVessel, FuelKey>>> travelFuelComponentNames = new EnumMap<>(Fuel.class);
	public static final Map<Fuel, List<Function<IVessel, FuelKey>>> idleFuelComponentNames = new EnumMap<>(Fuel.class);

	public static final Map<Fuel, FuelUnit[]> displayFuelUnits = new EnumMap<>(Fuel.class);

	public static final Map<FuelUnit, com.mmxlabs.models.lng.schedule.FuelUnit> modelUnits = new EnumMap<>(FuelUnit.class);

	static {
		portFuelComponentNames.put(Fuel.BASE_FUEL, Lists.newArrayList(IVessel::getInPortBaseFuelInMT));
		portFuelComponentNames.put(Fuel.NBO, Lists.newArrayList(vc -> LNGFuelKeys.NBO_In_m3, vc -> LNGFuelKeys.NBO_In_mmBtu));

		travelFuelComponentNames.put(Fuel.BASE_FUEL, Lists.newArrayList(IVessel::getTravelBaseFuelInMT, IVessel::getSupplementalTravelBaseFuelInMT));
		travelFuelComponentNames.put(Fuel.PILOT_LIGHT, Lists.newArrayList(IVessel::getPilotLightFuelInMT));

		travelFuelComponentNames.put(Fuel.NBO, Lists.newArrayList(vc -> LNGFuelKeys.NBO_In_m3, vc -> LNGFuelKeys.NBO_In_mmBtu));
		travelFuelComponentNames.put(Fuel.FBO, Lists.newArrayList(vc -> LNGFuelKeys.FBO_In_m3, vc -> LNGFuelKeys.FBO_In_mmBtu));

		idleFuelComponentNames.put(Fuel.BASE_FUEL, Lists.newArrayList(IVessel::getIdleBaseFuelInMT));
		idleFuelComponentNames.put(Fuel.PILOT_LIGHT, Lists.newArrayList(IVessel::getIdlePilotLightFuelInMT));
		idleFuelComponentNames.put(Fuel.NBO, Lists.newArrayList(vc -> LNGFuelKeys.IdleNBO_In_m3, vc -> LNGFuelKeys.IdleNBO_In_mmBtu));

		displayFuelUnits.put(Fuel.PILOT_LIGHT, new FuelUnit[] { FuelUnit.MT });
		displayFuelUnits.put(Fuel.BASE_FUEL, new FuelUnit[] { FuelUnit.MT });
		displayFuelUnits.put(Fuel.NBO, new FuelUnit[] { FuelUnit.M3, FuelUnit.MMBTu });
		displayFuelUnits.put(Fuel.FBO, new FuelUnit[] { FuelUnit.M3, FuelUnit.MMBTu });

		modelUnits.put(FuelUnit.M3, com.mmxlabs.models.lng.schedule.FuelUnit.M3);
		modelUnits.put(FuelUnit.MT, com.mmxlabs.models.lng.schedule.FuelUnit.MT);
		modelUnits.put(FuelUnit.MMBTu, com.mmxlabs.models.lng.schedule.FuelUnit.MMBTU);
	}

	public static <T> List<FuelQuantity> exportFuelData(final T details, final IVessel vessel, final Map<Fuel, List<Function<IVessel, FuelKey>>> fuelMap,
			final ToLongBiFunction<T, FuelKey> consumptionProvider, final ToIntBiFunction<T, FuelComponent> priceProvider, ModelEntityMap modelEntityMap) {

		// TODO: If we allow mixed HFO/MDO on a leg, then this needs to change...
		final List<FuelQuantity> result = new LinkedList<>();
		for (final Map.Entry<Fuel, List<Function<IVessel, FuelKey>>> entry : fuelMap.entrySet()) {
			long totalCost = 0;
			boolean matters = false;

			final FuelQuantity quantity = ScheduleFactory.eINSTANCE.createFuelQuantity();
			quantity.setFuel(entry.getKey());

			for (final FuelUnit unit : FuelUnit.values()) {
				long totalConsumption = 0;
				long totalUnitPrice = 0;
				int count = 0;

				IBaseFuel baseFuel = null;
				for (final Function<IVessel, FuelKey> f : entry.getValue()) {
					FuelKey fuelKey = f.apply(vessel);
					if (baseFuel != null) {
						assert baseFuel == fuelKey.getBaseFuel();
					}
					baseFuel = fuelKey.getBaseFuel();

					if (fuelKey.getFuelUnit() == unit) {
						final long consumption = consumptionProvider.applyAsLong(details, fuelKey);

						totalConsumption += consumption;
						if (unit == fuelKey.getFuelComponent().getPricingFuelUnit()) {
							final int unitPrice = priceProvider.applyAsInt(details, fuelKey.getFuelComponent());
							if (unitPrice != 0) {
								totalUnitPrice += unitPrice;
								count++;

								final long cost = Calculator.costFromConsumption(unitPrice, consumption);
								totalCost += cost;
							}
						}
					}
				}
				if (totalConsumption > 0) {
					final FuelAmount amount = ScheduleFactory.eINSTANCE.createFuelAmount();
					amount.setQuantity(OptimiserUnitConvertor.convertToExternalFloatVolume(totalConsumption));
					amount.setUnit(modelUnits.get(unit));
					// Average price
					if (count != 0) {
						amount.setUnitPrice(OptimiserUnitConvertor.convertToExternalPrice((int) (totalUnitPrice / count)));
					}
					quantity.getAmounts().add(amount);
					matters = true;
				}
				if (matters) {

					if (baseFuel != null) {
						quantity.setBaseFuel(modelEntityMap.getModelObject(baseFuel, BaseFuel.class));
					}
					quantity.setCost(OptimiserUnitConvertor.convertToExternalFloatFixedCost(totalCost));
					result.add(quantity);
				}
			}
		}
		return result;
	}
}
