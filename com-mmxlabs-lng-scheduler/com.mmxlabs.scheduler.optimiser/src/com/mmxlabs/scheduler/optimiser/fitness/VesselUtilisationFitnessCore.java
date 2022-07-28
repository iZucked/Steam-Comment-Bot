/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.providers.IPortTypeProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselSlotCountFitnessProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;

/**
 * An {@link IFitnessComponent} and {@link IFitnessCore} combined implementation which applies a reward for using particular vessels
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

	private final Map<IResource, Integer> fitnessVessels = new HashMap<>();

	private final Map<IResource, Long> perVesselWeight = new HashMap<>();

	public VesselUtilisationFitnessCore(final String name) {
		this.name = name;
	}

	@Override
	public void init(@NonNull final IPhaseOptimisationData data) {
		final List<IResource> resources = data.getResources();
		for (int i = 0; i < resources.size(); i++) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(resources.get(i));
			final int countForVessel = vesselInformationProvider.getCountForVessel(vesselCharter);
			if (countForVessel > 0) {
				perVesselWeight.put(resources.get(i), vesselInformationProvider.getWeightForVessel(vesselCharter) * -1L); // -1 for minimise
				fitnessVessels.put(resources.get(i), countForVessel);
			}
		}
	}

	@Override
	public Collection<IFitnessComponent> getFitnessComponents() {
		return Collections.<IFitnessComponent> singleton(this);
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {

		final @Nullable ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
		if (volumeAllocatedSequences == null) {
			return false;
		}
		this.lastFitness = evaluation(volumeAllocatedSequences);
		return true;
	}

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final Collection<IResource> affectedResources) {

		final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
		if (volumeAllocatedSequences == null) {
			return false;
		}

		final long currentLastHours = evaluation(volumeAllocatedSequences);
		this.lastFitness = currentLastHours;
		this.lastHours = currentLastHours;
		return true;
	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		evaluate(sequences, evaluationState);
		solution.setGeneralAnnotation(AI_UTILISATION_COST_PER_HOUR, lastHours);
	}

	private long evaluation(final ProfitAndLossSequences volumeAllocatedSequences) {
		long weight = 0;
		for (final Map.Entry<IResource, Integer> e : fitnessVessels.entrySet()) {
			final IResource iResource = e.getKey();
			final VolumeAllocatedSequence scheduledSequenceForResource = volumeAllocatedSequences.getScheduledSequenceForResource(iResource);
			long numLoads = 0;
			for (final VoyagePlanRecord vpr : scheduledSequenceForResource.getVoyagePlanRecords()) {
				numLoads += vpr.getPortTimesRecord().getSlots().stream().filter(ILoadOption.class::isInstance).count();
			}
			weight += (Math.min(numLoads, e.getValue()) * perVesselWeight.get(iResource));
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
