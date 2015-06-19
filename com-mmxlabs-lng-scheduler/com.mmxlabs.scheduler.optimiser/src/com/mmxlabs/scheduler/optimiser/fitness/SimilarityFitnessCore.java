/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.name.Named;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An {@link IFitnessComponent} and {@link IFitnessCore} combined implementation which applies a penalty for moving away from the initial solution
 * 
 * @author Alex Churchill
 * @since 3.7
 */
public class SimilarityFitnessCore implements IFitnessCore, IFitnessComponent {
	public final static String SIMILARITY_THRESHOLD_NUM_CHANGES = "similarityThresholdNumChanges";
	public final static String SIMILARITY_THRESHOLD = "similarityThreshold";

	@Inject
	@Named(SimilarityFitnessCore.SIMILARITY_THRESHOLD_NUM_CHANGES)
	protected int CHANGE_THRESHOLD;
	
	
	@Inject
	@Named(SimilarityFitnessCore.SIMILARITY_THRESHOLD)
	private boolean USE_THRESHOLD;
	
	private final String name;

	@Inject
	private IPortTypeProvider portTypeProvider;
	
	private long lastFitness = 0;
	private int lastDifferences = 0;

	private Map<Integer, Integer> loadDischargeMap = null; // TODO: indexed?
	private Map<Integer, Integer> loadResourceMap = null; // TODO: indexed?
	private List<IResource> resources;
	
	public SimilarityFitnessCore(final String name) {
		this.name = name;
	}

	public SimilarityFitnessCore(final String name, boolean threshold) {
		this.name = name;
		this.USE_THRESHOLD = threshold;
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
	public void init(@NonNull final ISequences sequences) {
		for (IResource resource : resources) {
			ISequence sequence = sequences.getSequence(resource.getIndex());
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
	}

	@Override
	public void dispose() {

	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return Collections.<IFitnessComponent> singleton(this);
	}

	@Override
	public boolean evaluate(@NonNull ISequences sequences, @NonNull IEvaluationState evaluationState) {
		evaluation(sequences);
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {
		evaluation(sequences);
		return true;
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
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
			init(sequences);
			lastFitness = 0;
		} else {
			int cargoDifferences = 0;
			int vesselDifferences = 0;
			for (IResource resource : resources) {
				ISequence sequence = sequences.getSequence(resource.getIndex());
				ISequenceElement prev = null;
				for (final ISequenceElement current : sequence) {
					if (prev != null) {
						if (getPortType(prev) == PortType.Load) {
							Integer matchedDischarge = loadDischargeMap.get(prev.getIndex());
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
			if (USE_THRESHOLD){
				lastFitness = processDifferencesWithThreshold(numberOfChanges);
			} else {
				lastFitness = numberOfChanges;
			}
		}
		lastDifferences = numberOfChanges;
	}
	
	public int processDifferencesWithThreshold(int diff) {
		return diff <= CHANGE_THRESHOLD ? 0 : diff - CHANGE_THRESHOLD;
	}
	public PortType getPortType(ISequenceElement element) {
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
