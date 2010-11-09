/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.HashMap;
import java.util.Map;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class AbstractPerRouteSchedulerFitnessComponent<T> extends
		AbstractSchedulerFitnessComponent<T> implements
		ICargoSchedulerFitnessComponent<T> {
	private Map<IResource, Long> evaluatedFitnesses = new HashMap<IResource, Long>();

	private Map<IResource, Long> acceptedFitnesses = new HashMap<IResource, Long>();

	private IResource currentResource;
	private long accumulator = 0;

	public AbstractPerRouteSchedulerFitnessComponent(final String name,
			final IFitnessCore<T> core) {
		super(name, core);
	}

	@Override
	public void startSequence(final IResource resource,
			final boolean sequenceHasChanged) {
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
	public boolean nextVoyagePlan(VoyagePlan voyagePlan, int time) {
		if (currentResource != null) {
			return reallyEvaluateVoyagePlan(voyagePlan, time);
		} else {
			return true;
		}
	}

	/**
	 * for subclasses which might be interested in the details of a voyageplan,
	 * for whatever reason.
	 * 
	 * @param voyagePlan
	 * @param time
	 * @return
	 */
	protected boolean reallyEvaluateVoyagePlan(VoyagePlan voyagePlan, int time) {
		return true;
	}

	protected abstract boolean reallyEvaluateObject(Object object, int time);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #startEvaluation()
	 */
	@Override
	public void startEvaluation() {
		accumulator = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #endSequence()
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
	 * @see
	 * com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent
	 * #endEvaluationAndGetCost()
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
}
