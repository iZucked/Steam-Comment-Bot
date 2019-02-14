/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.fitness.IFitnessComponent;
import com.mmxlabs.optimiser.core.fitness.IFitnessCore;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An {@link IFitnessComponent} and {@link IFitnessCore} combined implementation which applies a reward for using 
 * particular vessels
 * 
 * @author Alex Churchill
 */
public class VesselUtilisationFitnessCore implements IFitnessCore, IFitnessComponent {
	public static final String UTILISATION_COST_PER_HOUR = "UtilisationFitnessCostPerHour";
	public static final String AI_UTILISATION_COST_PER_HOUR = "AI_UtilisationFitnessCostPerHour";
	
	private final String name;

	@Inject
	private IVesselProvider vesselProvider;
 
	@Inject
	private IPortTypeProvider portTypeProvider;

	@Inject
	private IVesselSlotCountFitnessProvider vesselInformationProvider;
	
	private long lastFitness = 0;
	private long lastHours = 0;

	private List<IResource> resources;
	private Map<IResource, Integer> fitnessVessels = new HashMap<>();
	
	private Map<IResource, Long> perVesselWeight = new HashMap<>();

	public VesselUtilisationFitnessCore(final String name) {
		this.name = name;
	}

	public VesselUtilisationFitnessCore(final String name, final boolean threshold) {
		this.name = name;
	}

	@Override
	public void init(@NonNull final IPhaseOptimisationData data) {
		resources = data.getResources();
		for (int i = 0; i < resources.size(); i++) {
			IVesselAvailability vesselAvailability = vesselProvider.getVesselAvailability(resources.get(i));
			int countForVessel = vesselInformationProvider.getCountForVessel(vesselAvailability);
			if (countForVessel > 0) {
				perVesselWeight.put(resources.get(i),
						vesselInformationProvider.getWeightForVessel(vesselAvailability)*-1L); //-1 for minimise
				fitnessVessels.put(resources.get(i), countForVessel);
			}
		}
	}

	/***
	 * Build mapping
	 * 
	 * @param sequences
	 */
	public void initWithState(@NonNull final ISequences rawSequences) {
		
	}

	@Override
	public void dispose() {

	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return Collections.<IFitnessComponent> singleton(this);
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		@Nullable
		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
		if (volumeAllocatedSequences == null) {
			return false;
		}
		this.lastFitness = evaluation(volumeAllocatedSequences);
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {
		@Nullable
		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
		if (volumeAllocatedSequences == null) {
			return false;
		}
		
		long lastHours = evaluation(volumeAllocatedSequences);
		this.lastFitness = lastHours;
		this.lastHours = lastHours;
		return true;
	}

	@Override
	public void accepted(@NonNull final ISequences sequences, @Nullable final Collection<IResource> affectedResources) {

	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		if (solution != null) {
			evaluate(sequences, evaluationState);
			solution.setGeneralAnnotation(AI_UTILISATION_COST_PER_HOUR, lastHours);
		}
	}

	private long evaluation(@Nullable
			final VolumeAllocatedSequences volumeAllocatedSequences) {
		long weight = 0;
		for (IResource iResource : fitnessVessels.keySet()) {
			VolumeAllocatedSequence scheduledSequenceForResource = volumeAllocatedSequences.getScheduledSequenceForResource(iResource);
			long numLoads = scheduledSequenceForResource.getSequenceSlots()
										.stream()
										.filter(s->(s instanceof ILoadOption))
										.count();
			weight += (Math.min(numLoads, fitnessVessels.get(iResource))*perVesselWeight.get(iResource));
		}
		return weight;
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
