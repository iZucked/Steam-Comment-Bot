/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.export;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelAmount;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.transformer.ModelEntityMap;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.events.IFuelUsingEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;

/**
 * Convenience class for holding some useful variables.
 * 
 * @author hinton
 * 
 */
public abstract class BaseAnnotationExporter implements IAnnotationExporter {
	protected IAnnotatedSolution annotatedSolution;
	protected MMXRootObject inputScenario;
	protected Schedule output;
	protected ModelEntityMap entities;
	protected final ScheduleFactory scheduleFactory = SchedulePackage.eINSTANCE.getScheduleFactory();

	protected final ScheduleFactory factory = SchedulePackage.eINSTANCE.getScheduleFactory();

	private static final Map<Fuel, FuelComponent[]> fuelComponentNames = new HashMap<Fuel, FuelComponent[]>();

	private static final Map<Fuel, FuelUnit[]> displayFuelUnits = new HashMap<Fuel, FuelUnit[]>();

	private static final Map<FuelUnit, com.mmxlabs.models.lng.schedule.FuelUnit> modelUnits = new HashMap<FuelUnit, com.mmxlabs.models.lng.schedule.FuelUnit>();

	static {
		fuelComponentNames.put(Fuel.BASE_FUEL, FuelComponent.getBaseFuelComponentsNoPilot());
		fuelComponentNames.put(Fuel.PILOT_LIGHT, FuelComponent.getPilotLightFuelComponents());

		fuelComponentNames.put(Fuel.NBO, new FuelComponent[] { FuelComponent.NBO, FuelComponent.IdleNBO });
		fuelComponentNames.put(Fuel.FBO, new FuelComponent[] { FuelComponent.FBO });

		displayFuelUnits.put(Fuel.PILOT_LIGHT, new FuelUnit[] { FuelUnit.MT });
		displayFuelUnits.put(Fuel.BASE_FUEL, new FuelUnit[] { FuelUnit.MT });
		displayFuelUnits.put(Fuel.NBO, new FuelUnit[] { FuelUnit.M3, FuelUnit.MMBTu });
		displayFuelUnits.put(Fuel.FBO, new FuelUnit[] { FuelUnit.M3, FuelUnit.MMBTu });

		modelUnits.put(FuelUnit.M3, com.mmxlabs.models.lng.schedule.FuelUnit.M3);
		modelUnits.put(FuelUnit.MT, com.mmxlabs.models.lng.schedule.FuelUnit.MT);
		modelUnits.put(FuelUnit.MMBTu, com.mmxlabs.models.lng.schedule.FuelUnit.MMBTU);
	}

	@Override
	public void setAnnotatedSolution(final IAnnotatedSolution annotatedSolution) {
		this.annotatedSolution = annotatedSolution;
	}

	@Override
	public void setOutput(final Schedule output) {
		this.output = output;
	}

	@Override
	public void setScenario(final MMXRootObject inputScenario) {
		this.inputScenario = inputScenario;
	}

	@Override
	public void setModelEntityMap(final ModelEntityMap entities) {
		this.entities = entities;
	}

	protected List<FuelQuantity> createFuelQuantities(final IFuelUsingEvent event) {
		final List<FuelQuantity> result = new LinkedList<FuelQuantity>();

		for (final Map.Entry<Fuel, FuelComponent[]> entry : fuelComponentNames.entrySet()) {
			long totalCost = 0;
			boolean matters = false;

			for (final FuelComponent component : entry.getValue()) {
				totalCost += event.getFuelCost(component);
			}

			final FuelQuantity quantity = ScheduleFactory.eINSTANCE.createFuelQuantity();
			quantity.setFuel(entry.getKey());
			quantity.setCost((int) (totalCost / Calculator.ScaleFactor));
			if (totalCost > 0)
				matters = true;

			for (final FuelUnit unit : displayFuelUnits.get(entry.getKey())) {
				long totalUsage = 0;
				for (final FuelComponent component : entry.getValue()) {
					totalUsage += event.getFuelConsumption(component, unit);
				}
				if (totalUsage > 0) {
					final FuelAmount amount = ScheduleFactory.eINSTANCE.createFuelAmount();
					amount.setQuantity((int) (totalUsage / Calculator.ScaleFactor));
					amount.setUnit(modelUnits.get(unit));
					quantity.getAmounts().add(amount);
					matters = true;
				}
			}

			if (matters)
				result.add(quantity);
		}

		return result;
	}
}
