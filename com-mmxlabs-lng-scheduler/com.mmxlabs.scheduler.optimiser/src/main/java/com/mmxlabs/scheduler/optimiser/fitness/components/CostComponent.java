package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.Collections;
import java.util.List;

import com.mmxlabs.optimiser.core.IAnnotatedSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

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

	@SuppressWarnings("unchecked")
	@Override
	public long rawEvaluateSequence(final IResource resource,
			final ISequence<T> sequence,
			final IAnnotatedSequence<T> annotatedSequence) {

		long cost = 0;

		for (final T element : sequence) {

			if (annotatedSequence.hasAnnotation(element,
					SchedulerConstants.AI_journeyInfo)) {
				final IJourneyEvent<T> e = annotatedSequence.getAnnotation(
						element, SchedulerConstants.AI_journeyInfo,
						IJourneyEvent.class);

				for (final FuelComponent fuel : fuelComponents) {
					cost += e.getFuelCost(fuel);
				}
			}
			if (annotatedSequence.hasAnnotation(element,
					SchedulerConstants.AI_idleInfo)) {
				final IIdleEvent<T> e = annotatedSequence.getAnnotation(
						element, SchedulerConstants.AI_idleInfo,
						IIdleEvent.class);

				for (final FuelComponent fuel : fuelComponents) {
					cost += e.getFuelCost(fuel);
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
