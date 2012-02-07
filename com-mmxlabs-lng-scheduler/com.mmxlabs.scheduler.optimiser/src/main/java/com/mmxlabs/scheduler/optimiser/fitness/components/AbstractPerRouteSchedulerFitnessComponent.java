/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class AbstractPerRouteSchedulerFitnessComponent extends AbstractSchedulerFitnessComponent implements ICargoSchedulerFitnessComponent {
	private final Map<IResource, Long> evaluatedFitnesses = new HashMap<IResource, Long>();

	private final Map<IResource, Long> acceptedFitnesses = new HashMap<IResource, Long>();

	private IResource currentResource;
	private long accumulator = 0;

	public AbstractPerRouteSchedulerFitnessComponent(final String name, final IFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startSequence(final IResource resource, final boolean sequenceHasChanged) {
		if (sequenceHasChanged) {
			if (reallyStartSequence(resource)) {
				currentResource = resource;
			} else {
				currentResource = null;
				evaluatedFitnesses.put(resource, 0l);
			}
		} else {
			currentResource = null;
			accumulator += acceptedFitnesses.get(resource);
		}
	}

	/**
	 * @param resource
	 */
	protected abstract boolean reallyStartSequence(IResource resource);

	@Override
	public final boolean nextObject(final Object object, final int time) {
		if (currentResource != null) {
			return reallyEvaluateObject(object, time);
		}
		return true;
	}

	@Override
	public boolean nextVoyagePlan(final VoyagePlan voyagePlan, final int time) {
		if (currentResource != null) {
			return reallyEvaluateVoyagePlan(voyagePlan, time);
		} else {
			return true;
		}
	}

	/**
	 * for subclasses which might be interested in the details of a voyageplan, for whatever reason.
	 * 
	 * @param voyagePlan
	 * @param time
	 * @return
	 */
	protected boolean reallyEvaluateVoyagePlan(final VoyagePlan voyagePlan, final int time) {
		return true;
	}

	/**
	 * Subclasses should use this to accumulate the cost associated with this object. Return false to indicate an unacceptable solution
	 * 
	 * @param object
	 * @param time
	 * @return
	 */
	protected abstract boolean reallyEvaluateObject(Object object, int time);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent #startEvaluation()
	 */
	@Override
	public void startEvaluation() {
		accumulator = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent #endSequence()
	 */
	@Override
	public boolean endSequence() {
		if (currentResource != null) {
			final long sequenceValue = endSequenceAndGetCost();
			if (sequenceValue == Long.MAX_VALUE) {
				return false;
			} else {
				accumulator += sequenceValue;
				evaluatedFitnesses.put(currentResource, sequenceValue);
				return true;
			}
		}
		return true;
	}

	protected abstract long endSequenceAndGetCost();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent #endEvaluationAndGetCost()
	 */
	@Override
	public long endEvaluationAndGetCost() {
		return setLastEvaluatedFitness(accumulator);
	}

	@Override
	public void rejectLastEvaluation() {
		evaluatedFitnesses.putAll(acceptedFitnesses);
		super.rejectLastEvaluation();
	}

	@Override
	public void acceptLastEvaluation() {
		acceptedFitnesses.putAll(evaluatedFitnesses);
		super.acceptLastEvaluation();
	}

	/**
	 * Adds the per-route fitness of this objective to the map associated with {@link SchedulerConstants#G_AI_fitnessPerRoute}
	 */
	@Override
	public void endEvaluationAndAnnotate(final IAnnotatedSolution solution) {
		super.endEvaluationAndAnnotate(solution);
		setLastEvaluatedFitness(accumulator);
		@SuppressWarnings("unchecked")
		final Map<IResource, Map<String, Long>> fitnessByRoute = solution.getGeneralAnnotation(SchedulerConstants.G_AI_fitnessPerRoute, Map.class);

		for (final Map.Entry<IResource, Long> entry : evaluatedFitnesses.entrySet()) {
			fitnessByRoute.get(entry.getKey()).put(getName(), entry.getValue());
		}
	}
}
