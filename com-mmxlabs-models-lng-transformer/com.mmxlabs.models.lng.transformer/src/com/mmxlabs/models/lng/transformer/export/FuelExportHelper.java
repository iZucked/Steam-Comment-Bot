package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntBiFunction;

import com.mmxlabs.common.util.ToLongTriFunction;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

public class FuelExportHelper {

	public static final Map<Fuel, FuelComponent[]> portFuelComponentNames = new HashMap<Fuel, FuelComponent[]>();
	public static final Map<Fuel, FuelComponent[]> travelFuelComponentNames = new HashMap<Fuel, FuelComponent[]>();
	public static final Map<Fuel, FuelComponent[]> idleFuelComponentNames = new HashMap<Fuel, FuelComponent[]>();

	public static final Map<Fuel, FuelUnit[]> displayFuelUnits = new HashMap<Fuel, FuelUnit[]>();

	public static final Map<FuelUnit, com.mmxlabs.models.lng.schedule.FuelUnit> modelUnits = new HashMap<FuelUnit, com.mmxlabs.models.lng.schedule.FuelUnit>();

	static {
		portFuelComponentNames.put(Fuel.BASE_FUEL, new FuelComponent[] { FuelComponent.Base });
		portFuelComponentNames.put(Fuel.NBO, new FuelComponent[] { FuelComponent.NBO });

		travelFuelComponentNames.put(Fuel.BASE_FUEL, new FuelComponent[] { FuelComponent.Base, FuelComponent.Base_Supplemental });
		travelFuelComponentNames.put(Fuel.PILOT_LIGHT, new FuelComponent[] { FuelComponent.PilotLight });

		travelFuelComponentNames.put(Fuel.NBO, new FuelComponent[] { FuelComponent.NBO });
		travelFuelComponentNames.put(Fuel.FBO, new FuelComponent[] { FuelComponent.FBO });

		idleFuelComponentNames.put(Fuel.BASE_FUEL, new FuelComponent[] { FuelComponent.IdleBase });
		idleFuelComponentNames.put(Fuel.PILOT_LIGHT, new FuelComponent[] { FuelComponent.IdlePilotLight });
		idleFuelComponentNames.put(Fuel.NBO, new FuelComponent[] { FuelComponent.IdleNBO });

		displayFuelUnits.put(Fuel.PILOT_LIGHT, new FuelUnit[] { FuelUnit.MT });
		displayFuelUnits.put(Fuel.BASE_FUEL, new FuelUnit[] { FuelUnit.MT });
		displayFuelUnits.put(Fuel.NBO, new FuelUnit[] { FuelUnit.M3, FuelUnit.MMBTu });
		displayFuelUnits.put(Fuel.FBO, new FuelUnit[] { FuelUnit.M3, FuelUnit.MMBTu });

		modelUnits.put(FuelUnit.M3, com.mmxlabs.models.lng.schedule.FuelUnit.M3);
		modelUnits.put(FuelUnit.MT, com.mmxlabs.models.lng.schedule.FuelUnit.MT);
		modelUnits.put(FuelUnit.MMBTu, com.mmxlabs.models.lng.schedule.FuelUnit.MMBTU);
	}

	public static <T> List<FuelQuantity> exportFuelData(final T details, final Map<Fuel, FuelComponent[]> fuelMap, final ToLongTriFunction<T, FuelComponent, FuelUnit> consumptionProvider,
			final ToIntBiFunction<T, FuelComponent> priceProvider) {

		final List<FuelQuantity> result = new LinkedList<FuelQuantity>();
		for (final Map.Entry<Fuel, FuelComponent[]> entry : fuelMap.entrySet()) {
			long totalCost = 0;
			boolean matters = false;

			final FuelQuantity quantity = ScheduleFactory.eINSTANCE.createFuelQuantity();
			quantity.setFuel(entry.getKey());

			for (final FuelUnit unit : FuelUnit.values()) {
				long totalConsumption = 0;
				long totalUnitPrice = 0;
				int count = 0;

				for (final FuelComponent component : entry.getValue()) {
					final long consumption = consumptionProvider.applyAsLong(details, component, unit);

					totalConsumption += consumption;
					if (unit == component.getPricingFuelUnit()) {
						final int unitPrice = priceProvider.applyAsInt(details, component);
						if (unitPrice != 0) {
							totalUnitPrice += unitPrice;
							count++;

							final long cost = Calculator.costFromConsumption(unitPrice, consumption);
							totalCost += cost;
						}
					}
				}
				if (totalConsumption > 0) {
					final FuelAmount amount = ScheduleFactory.eINSTANCE.createFuelAmount();
					amount.setQuantity(OptimiserUnitConvertor.convertToExternalVolume(totalConsumption));
					amount.setUnit(modelUnits.get(unit));
					// Average price
					if (count != 0) {
						amount.setUnitPrice(OptimiserUnitConvertor.convertToExternalPrice((int) (totalUnitPrice / count)));
					}
					quantity.getAmounts().add(amount);
					matters = true;
				}
				if (matters) {
					quantity.setCost(OptimiserUnitConvertor.convertToExternalFixedCost(totalCost));
					result.add(quantity);
				}
			}
		}
		return result;
	}
}
