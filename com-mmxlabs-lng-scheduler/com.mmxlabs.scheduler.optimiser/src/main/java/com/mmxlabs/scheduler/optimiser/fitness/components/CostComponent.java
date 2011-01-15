/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
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
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;

/**
 * 
 * {@link ICargoSchedulerFitnessComponent} implementation to calculate a fitness
 * based on lateness.
 * 
 * @author Simon Goodall
 * 
 * @param <T>
 *            Sequence element type
 */
public final class CostComponent<T> extends
		AbstractPerRouteSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	// private final List<FuelComponent> fuelComponents;
	private final FuelComponent[] fuelComponents;
	private final FuelUnit[] defaultUnits;
	private final int fuelComponentCount;

	public CostComponent(final String name,
			final List<FuelComponent> fuelComponents,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
		// this.fuelComponents = fuelComponents;
		this.fuelComponentCount = fuelComponents.size();
		this.fuelComponents = new FuelComponent[fuelComponentCount];
		this.defaultUnits = new FuelUnit[fuelComponentCount];
		int i = 0;
		for (FuelComponent fc : fuelComponents) {
			this.fuelComponents[i] = fc;
			this.defaultUnits[i++] = fc.getDefaultFuelUnit();
		}
	}

	public List<FuelComponent> getFuelComponents() {
		return Collections.unmodifiableList(Arrays.asList(fuelComponents));
	}

	private long accumulator;
	
	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyStartSequence(com.mmxlabs.optimiser.core.IResource)
	 */
	@Override
	protected boolean reallyStartSequence(final IResource resource) {
		accumulator = 0;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#reallyEvaluateObject(java.lang.Object, int)
	 */
	@Override
	protected boolean reallyEvaluateObject(Object object, int time) {
		if (object instanceof VoyageDetails) {
			@SuppressWarnings("unchecked")
			final VoyageDetails<T> detail = (VoyageDetails<T>) object;

			for (int i = 0; i < fuelComponentCount; i++) {
				final FuelComponent fuel = fuelComponents[i];
				final FuelUnit defaultFuelUnit = defaultUnits[i];
				final long consumption = detail.getFuelConsumption(fuel,
						defaultFuelUnit);
				final long fuelCost = Calculator.costFromConsumption(
						consumption, detail.getFuelUnitPrice(fuel));

//				addDiscountedValue(time, fuelCost);
				accumulator += fuelCost;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.scheduler.optimiser.fitness.components.AbstractPerRouteSchedulerFitnessComponent#endSequenceAndGetCost()
	 */
	@Override
	protected long endSequenceAndGetCost() {
		return accumulator / Calculator.ScaleFactor;
	}
}
