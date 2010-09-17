package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Collections;
import java.util.List;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelUnit;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.IVoyagePlan;
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
		AbstractCargoSchedulerFitnessComponent<T> implements
		IFitnessComponent<T> {

	private final List<FuelComponent> fuelComponents;

	public CostComponent(final String name,
			final List<FuelComponent> fuelComponents,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
		this.fuelComponents = fuelComponents;
	}

	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final List<IVoyagePlan> plans) {

		long cost = 0;

		for (final IVoyagePlan plan : plans) {
			for (final Object obj : plan.getSequence()) {
				if (obj instanceof VoyageDetails) {
					@SuppressWarnings("unchecked")
					final IVoyageDetails<T> detail = (IVoyageDetails<T>) obj;

					for (final FuelComponent fuel : fuelComponents) {

						final FuelUnit defaultFuelUnit = fuel
								.getDefaultFuelUnit();
						final long consumption = detail.getFuelConsumption(
								fuel, defaultFuelUnit);
						final long fuelCost = Calculator.costFromConsumption(
								consumption, detail.getFuelUnitPrice(fuel));

						cost += fuelCost;
					}
				}
			}
		}

		// Remove scale factor from result (back into external units)
		// TODO: Use Calculator to convert back
		cost /= Calculator.ScaleFactor;

		return cost;
	}

	@Override
	public void init(final IOptimisationData<T> data) {
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public List<FuelComponent> getFuelComponents() {
		return Collections.unmodifiableList(fuelComponents);
	}
}
