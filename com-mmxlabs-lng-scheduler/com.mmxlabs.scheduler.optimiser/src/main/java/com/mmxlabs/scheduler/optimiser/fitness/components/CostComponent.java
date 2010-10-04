package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Arrays;
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
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

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

//	private final List<FuelComponent> fuelComponents;
	private final FuelComponent[] fuelComponents;
	private final FuelUnit[] defaultUnits;
	private final int fuelComponentCount;
	
	public CostComponent(final String name,
			final List<FuelComponent> fuelComponents,
			final CargoSchedulerFitnessCore<T> core) {
		super(name, core);
//		this.fuelComponents = fuelComponents;
		this.fuelComponentCount = fuelComponents.size();
		this.fuelComponents = new FuelComponent[fuelComponentCount];
		this.defaultUnits = new FuelUnit[fuelComponentCount];
		int i = 0;
		for (FuelComponent fc : fuelComponents) {
			this.fuelComponents[i] = fc;
			this.defaultUnits[i++] = fc.getDefaultFuelUnit();
		}
	}

	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence, final List<VoyagePlan> plans, final int startTime) {

//		long cost = 0;

//		for (final VoyagePlan plan : plans) {
//			for (final Object obj : plan.getSequence()) {
//				if (obj instanceof VoyageDetails) {
//					@SuppressWarnings("unchecked")
//					final VoyageDetails<T> detail = (VoyageDetails<T>) obj;
//
//					for (final FuelComponent fuel : fuelComponents) {
//
//						final FuelUnit defaultFuelUnit = fuel
//								.getDefaultFuelUnit();
//						final long consumption = detail.getFuelConsumption(
//								fuel, defaultFuelUnit);
//						final long fuelCost = Calculator.costFromConsumption(
//								consumption, detail.getFuelUnitPrice(fuel));
//
//						cost += fuelCost;
//					}
//				}
//			}
//		}

		// Remove scale factor from result (back into external units)
		// TODO: Use Calculator to convert back
//		cost /= Calculator.ScaleFactor;

		return cost;
	}

	@Override
	public void init(final IOptimisationData<T> data) {

	}

	public List<FuelComponent> getFuelComponents() {
		return Collections.unmodifiableList(Arrays.asList(fuelComponents));
	}

	@Override
	public boolean shouldIterate() {
		return true;
	}
	long cost = 0;
	@Override
	public void beginIterating(IResource resource) {
		cost = 0;
	}

	@Override
	public void evaluateNextObject(final Object obj, final int startTime) {
		// TODO Auto-generated method stub
		if (obj instanceof VoyageDetails) {
		@SuppressWarnings("unchecked")
		final VoyageDetails<T> detail = (VoyageDetails<T>) obj;

		for (int i = 0; i<fuelComponentCount; i++) {
			final FuelComponent fuel = fuelComponents[i];
			final FuelUnit defaultFuelUnit = defaultUnits[i];
			final long consumption = detail.getFuelConsumption(
					fuel, defaultFuelUnit);
			final long fuelCost = Calculator.costFromConsumption(
					consumption, detail.getFuelUnitPrice(fuel));

			cost += fuelCost;
		}
	}
	}

	@Override
	public void endIterating() {
		cost /= Calculator.ScaleFactor;
	}
}
