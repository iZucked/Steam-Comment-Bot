/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @author hinton
 * 
 */
public abstract class AbstractPerRouteSchedulerFitnessComponent extends AbstractSchedulerFitnessComponent {
	private final Map<IResource, Long> evaluatedFitnesses = new HashMap<IResource, Long>();

	private final Map<IResource, Long> acceptedFitnesses = new HashMap<IResource, Long>();

	protected IResource currentResource;
	private long evaluationAccumulator = 0;

	protected ProfitAndLossSequences profitAndLossSequences;

	public AbstractPerRouteSchedulerFitnessComponent(@NonNull final String name, @NonNull final IFitnessCore core) {
		super(name, core);
	}

	@Override
	public void startSequence(@NonNull final IResource resource) {
		if (reallyStartSequence(resource)) {
			currentResource = resource;
		} else {
			currentResource = null;
			evaluatedFitnesses.put(resource, 0L);
		}
	}

	/**
	 * @param resource
	 */
	protected abstract boolean reallyStartSequence(@NonNull IResource resource);

	@Override
	public final boolean nextObject(@NonNull final Object object, final int time) {
		if (currentResource != null) {
			return reallyEvaluateObject(object, time);
		}
		return true;
	}

	@Override
	public boolean nextVoyagePlan(@NonNull final VoyagePlan voyagePlan, final int time) {
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
	protected boolean reallyEvaluateVoyagePlan(@NonNull final VoyagePlan voyagePlan, final int time) {
		return true;
	}

	/**
	 * Subclasses should use this to accumulate the cost associated with this object. Return false to indicate an unacceptable solution
	 * 
	 * @param object
	 * @param time
	 * @return
	 */
	protected abstract boolean reallyEvaluateObject(@NonNull Object object, int time);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent #startEvaluation()
	 */
	@Override
	public void startEvaluation(@NonNull final ProfitAndLossSequences profitAndLossSequences) {
		this.profitAndLossSequences = profitAndLossSequences;
		evaluationAccumulator = 0;
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
				evaluationAccumulator += sequenceValue;
				evaluatedFitnesses.put(currentResource, sequenceValue);
				return true;
			}
		}
		return true;
	}

	protected abstract long endSequenceAndGetCost();

	@Override
	public boolean evaluateUnusedSlots(@NonNull final List<@NonNull ISequenceElement> unusedSlots, @NonNull final ProfitAndLossSequences scheduleSequences) {
		// By default, do nothing
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.fitness.ICargoSchedulerFitnessComponent #endEvaluationAndGetCost()
	 */
	@Override
	public long endEvaluationAndGetCost() {
		profitAndLossSequences = null;
		return setLastEvaluatedFitness(evaluationAccumulator);
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

	protected long getEvaluationAccumulator() {
		return evaluationAccumulator;
	}
}
