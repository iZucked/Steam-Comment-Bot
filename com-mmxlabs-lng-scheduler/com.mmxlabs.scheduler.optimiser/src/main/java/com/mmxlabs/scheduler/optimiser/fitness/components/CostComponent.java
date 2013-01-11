/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness based on lateness.
 * 
 * @author Simon Goodall
 * 
 */
public final class CostComponent extends AbstractPerRouteSchedulerFitnessComponent implements IFitnessComponent {

	// private final List<FuelComponent> fuelComponents;
	private final FuelComponent[] fuelComponents;
	private final FuelUnit[] defaultUnits;
	private final int fuelComponentCount;

	public CostComponent(final String name, final List<FuelComponent> fuelComponents, final CargoSchedulerFitnessCore core) {
		super(name, core);
		// this.fuelComponents = fuelComponents;
		this.fuelComponentCount = fuelComponents.size();
		this.fuelComponents = new FuelComponent[fuelComponentCount];
		this.defaultUnits = new FuelUnit[fuelComponentCount];
		int i = 0;
		for (final FuelComponent fc : fuelComponents) {
			this.fuelComponents[i] = fc;
			this.defaultUnits[i++] = fc.getDefaultFuelUnit();
		}
	}

	public List<FuelComponent> getFuelComponents() {
		return Collections.unmodifiableList(Arrays.asList(fuelComponents));
	}

	private long accumulator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(final Object object, final int time) {
		if (object instanceof VoyageDetails) {
			final VoyageDetails detail = (VoyageDetails) object;

			// add the (time-discounted) fuel costs for each fuel component 
			for (int i = 0; i < fuelComponentCount; i++) {
				final FuelComponent fuel = fuelComponents[i];
				final FuelUnit defaultFuelUnit = defaultUnits[i];
				final long consumption = detail.getFuelConsumption(fuel, defaultFuelUnit) + detail.getRouteAdditionalConsumption(fuel, defaultFuelUnit);
				final long fuelCost = Calculator.costFromConsumption(consumption, detail.getFuelUnitPrice(fuel));

				// calculate the discounted value of the fuel based on the time
				accumulator += getDiscountedValue(time, fuelCost);
			}
		}
		if (object instanceof PortDetails) {
			final PortDetails detail = (PortDetails) object;
			// add the (time-discounted) fuel costs for the base fuel component 
			// (if it's in the fuel components for this CostComponent object)  
			for (int i = 0; i < fuelComponentCount; i++) {
				final FuelComponent fuel = fuelComponents[i];
				if (fuel == FuelComponent.Base) {
					final long consumption = detail.getFuelConsumption(FuelComponent.Base);
					final long fuelCost = Calculator.costFromConsumption(consumption, detail.getFuelUnitPrice(FuelComponent.Base));
	
					// calculate the discounted value of the fuel based on the time
					accumulator += getDiscountedValue(time, fuelCost);
				}
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator / Calculator.ScaleFactor;
	}
}
