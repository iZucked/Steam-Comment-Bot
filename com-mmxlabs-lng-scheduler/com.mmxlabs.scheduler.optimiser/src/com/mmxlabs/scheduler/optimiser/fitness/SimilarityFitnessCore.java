/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.components.ISimilarityComponentParameters;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An {@link IFitnessComponent} and {@link IFitnessCore} combined implementation which applies a penalty for moving away from the initial solution
 * 
 * @author Alex Churchill
 * @since 3.7
 */
public class SimilarityFitnessCore implements IFitnessCore, IFitnessComponent {
	@Inject
	private ISimilarityComponentParameters similarityComponentParameters;

	private final String name;

 
	@Inject
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@NonNull
	private ISequences initialRawSequences;
	
	@Inject
	@NonNull
	private ISequencesManipulator sequenceManipulator;

	@Inject
	@Named(OptimiserConstants.SEQUENCE_TYPE_INITIAL)
	@NonNull
	private ISequences initialSequences;

	@Inject
	private IPortTypeProvider portTypeProvider;

	private long lastFitness = 0;
	private int lastDifferences = 0;

	private Map<Integer, Integer> loadDischargeMap = null; // TODO: indexed?
	private Map<Integer, Integer> loadResourceMap = null; // TODO: indexed?
	private List<IResource> resources;

	// per optimisation threshold curve constants
	private int lowThreshold;
	private int lowWeight;
	private int medThreshold;
	private int medWeight;
	private int highThreshold;
	private int highWeight;
	private int outOfBoundsWeight;
	private int highRange;
	private int medRange;
	private int lowRange;

	public SimilarityFitnessCore(final String name) {
		this.name = name;
	}

	public SimilarityFitnessCore(final String name, final boolean threshold) {
		this.name = name;
	}

	@Override
	public void init(@NonNull final IOptimisationData data) {
		resources = data.getResources();
	}

	/***
	 * Build mapping
	 * 
	 * @param sequences
	 */

	public void initWithState(@NonNull final ISequences rawSequences) {

		ISequences fullSequences = sequenceManipulator.createManipulatedSequences(rawSequences);
		
		for (final IResource resource : resources) {
			assert resource != null;

			final ISequence sequence = fullSequences.getSequence(resource);
			ISequenceElement prev = null;
			for (final ISequenceElement current : sequence) {
				if (prev != null) {
					if (getPortType(prev) == PortType.Load) {
						if (getPortType(current) == PortType.Discharge) {
							loadDischargeMap.put(prev.getIndex(), current.getIndex());
						} else {
							loadDischargeMap.put(prev.getIndex(), null);
						}
						loadResourceMap.put(prev.getIndex(), resource.getIndex());
					}
				}
				prev = current;
			}
		}
		initConstants();
	}

	private void initConstants() {
		lowThreshold = similarityComponentParameters.getThreshold(ISimilarityComponentParameters.Interval.LOW);
		lowWeight = similarityComponentParameters.getWeight(ISimilarityComponentParameters.Interval.LOW);
		medThreshold = similarityComponentParameters.getThreshold(ISimilarityComponentParameters.Interval.MEDIUM);
		medWeight = similarityComponentParameters.getWeight(ISimilarityComponentParameters.Interval.MEDIUM);
		highThreshold = similarityComponentParameters.getThreshold(ISimilarityComponentParameters.Interval.HIGH);
		highWeight = similarityComponentParameters.getWeight(ISimilarityComponentParameters.Interval.HIGH);
		outOfBoundsWeight = similarityComponentParameters.getOutOfBoundsWeight();
		highRange = highThreshold - medThreshold;
		medRange = medThreshold - lowThreshold;
		lowRange = lowThreshold;
	}

	@Override
	public void dispose() {

	}

	@SuppressWarnings("null")
	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return Collections.<IFitnessComponent> singleton(this);
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		evaluation(sequences);
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {
		evaluation(sequences);
		return true;
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		if (solution != null) {
			evaluation(sequences);
			solution.setGeneralAnnotation(SchedulerConstants.AI_similarityDifferences, lastDifferences);
		}
	}

	private void evaluation(@NonNull final ISequences sequences) {
		int numberOfChanges = 0;
		if (loadDischargeMap == null) {
			loadDischargeMap = new HashMap<Integer, Integer>();
			loadResourceMap = new HashMap<Integer, Integer>();
			initWithState(initialRawSequences);
			lastFitness = 0;
		}
		{
			int cargoDifferences = 0;
			int vesselDifferences = 0;
			for (final IResource resource : resources) {
				assert resource != null;
				final ISequence sequence = sequences.getSequence(resource);
				ISequenceElement prev = null;
				for (final ISequenceElement current : sequence) {
					if (prev != null) {
						if (getPortType(prev) == PortType.Load) {
							final Integer matchedDischarge = loadDischargeMap.get(prev.getIndex());
							if (matchedDischarge == null && getPortType(current) == PortType.Discharge) {
								cargoDifferences++;
							} else if (matchedDischarge != current.getIndex()) {
								cargoDifferences++;
							}
							if (loadResourceMap.get(prev.getIndex()) == null || loadResourceMap.get(prev.getIndex()) != resource.getIndex()) {
								vesselDifferences++;
							}
						}
					}
					prev = current;
				}
			}
			numberOfChanges = cargoDifferences + vesselDifferences;
			// store weighted fitness
			lastFitness = processDifferencesWithThreshold(numberOfChanges);
		}
		// store differences for annotation
		lastDifferences = numberOfChanges;
	}

	/**
	 * Calculate a weighted similarity curve, based on three points
	 * 
	 * @param diff
	 * @return
	 */
	public int processDifferencesWithThreshold(final int diff) {
		int outOfBounds = 0;
		int high = highRange;
		int med = medRange;
		int low = lowRange;

		if (diff >= highThreshold) {
			outOfBounds = diff - highThreshold;
		} else if (diff >= medThreshold) {
			high = diff - medThreshold;
		} else if (diff >= lowThreshold) {
			high = 0;
			med = diff - lowThreshold;
		} else {
			high = 0;
			med = 0;
			low = diff;
		}

		return (outOfBounds * outOfBoundsWeight + high * highWeight + med * medWeight + low * lowWeight);
	}

	public PortType getPortType(final ISequenceElement element) {
		return portTypeProvider.getPortType(element);
	}

	@Override
	public String getName() {
		if (name != null) {
			return name;
		} else {
			return "";
		}
	}

	@Override
	public long getFitness() {
		return lastFitness;
	}

	@Override
	public IFitnessCore getFitnessCore() {
		return this;
	}
}
