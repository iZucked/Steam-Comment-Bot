/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IModifiableSequence;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.impl.ListModifiableSequence;
import com.mmxlabs.optimiser.core.impl.ModifiableSequences;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.fitness.NonShippedScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.IFobSaleRotationProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IStartEndRequirementProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.schedule.ProfitAndLossCalculator;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;
import com.mmxlabs.scheduler.optimiser.scheduling.MinTravelTimeData;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

public class SchedulerEvaluationProcess implements IEvaluationProcess {

	// Constants used for keys into IEvaluationState
	@NonNull
	public static final String VOLUME_ALLOCATED_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-volume-allocated-sequences";
	@NonNull
	public static final String PROFIT_AND_LOSS_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-profit-and-loss-sequences";

	@NonNull
	public static final String NONSHIPPED_SCHEDULED_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-nonshipped-scheduled-sequences";

	@NonNull
	public static final String ALL_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-all-elements";

	@NonNull
	public static final String OPTIMISATION_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-optimisation-elements";

	@NonNull
	public static final String ADDITIONAL_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-additional-elements";

	@Inject
	private ScheduleCalculator scheduleCalculator;

	@Inject
	private ProfitAndLossCalculator profitAndLossCalculator;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IPhaseOptimisationData optimisationData;

	@Inject
	private IFobSaleRotationProvider fobSaleRotationProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IStartEndRequirementProvider startEndRequirementProvider;

	@Inject(optional = true)
	@Named("hint-nonshipped-rotations")
	private boolean withNonshippedRotations = false;

	@Override
	public boolean evaluate(@NonNull final Phase phase, @NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		return evaluate(phase, sequences, evaluationState, null);
	}

	@Override
	public void annotate(@NonNull final Phase phase, @NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		if (!evaluate(phase, sequences, evaluationState, solution)) {
			throw new RuntimeException("Unable to evaluate state");
		}
	}

	private boolean evaluate(@NonNull final Phase phase, @NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final IAnnotatedSolution solution) {
		if (phase == Phase.Checked_Evaluation) {

			@Nullable
			final ProfitAndLossSequences volumeAllocatedSequences = scheduleCalculator.schedule(sequences, solution);
			if (volumeAllocatedSequences == null) {
				return false;
			}

			// Store evaluated state
			evaluationState.setData(VOLUME_ALLOCATED_SEQUENCES, volumeAllocatedSequences);

			if (solution != null) {
				if (withNonshippedRotations) {
					final ISequences nonShippedSequences = buildNonShippedSequences(volumeAllocatedSequences);
					if (!nonShippedSequences.getResources().isEmpty()) {
						final @NonNull Map<@NonNull IResource, @Nullable Pair<@NonNull MinTravelTimeData, @NonNull List<@NonNull IPortTimesRecord>>> scheduled = scheduleCalculator
								.getNonShippedEstimatedFeasibility(nonShippedSequences);
						evaluationState.setData(NONSHIPPED_SCHEDULED_SEQUENCES, new NonShippedScheduledSequences(scheduled));
					}
				}
				setEvaluationElements(evaluationState, solution);
			}

		} else if (phase == Phase.Final_Evaluation) {

			final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
			if (volumeAllocatedSequences == null) {
				return false;
			}
			final ProfitAndLossSequences profitAndLossSequences = profitAndLossCalculator.calculateProfitAndLoss(sequences, volumeAllocatedSequences, solution);
			if (profitAndLossSequences == null) {
				return false;
			}

			// Store evaluated state
			evaluationState.setData(PROFIT_AND_LOSS_SEQUENCES, profitAndLossSequences);

		}
		return true;

	}

	private @NonNull Map<@NonNull IVesselCharter, List<VolumeAllocatedSequence>> extractFobSaleSequences(final @NonNull ProfitAndLossSequences volumeAllocatedSequences) {
		final Map<@NonNull IVesselCharter, List<VolumeAllocatedSequence>> fobSaleSequences = new HashMap<>();
		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			final IVesselCharter vesselCharter = vesselProvider.getVesselCharter(volumeAllocatedSequence.getResource());
			if (vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				// only length 4 sequences are valid
				final ISequence seq = volumeAllocatedSequence.getSequence();
				if (seq.size() == 4) {
					// index 2 should be the fob sale
					final IPortSlot portSlot = portSlotProvider.getPortSlot(seq.get(2));
					if (portSlot instanceof @NonNull IDischargeOption dischargeOption) {
						final IVesselCharter nonShippedVesselCharter = fobSaleRotationProvider.getVesselCharter(dischargeOption);
						if (nonShippedVesselCharter != null) {
							fobSaleSequences.computeIfAbsent(nonShippedVesselCharter, vc -> new LinkedList<>()).add(volumeAllocatedSequence);
						}
					}
				}
			}
		}
		fobSaleSequences.values() //
				.forEach(seqs -> seqs.sort((vas1, vas2) -> Integer.compare(vas1.getStartTime(), vas2.getStartTime())));
		return fobSaleSequences;
	}

	private ISequences buildNonShippedSequences(final @NonNull ProfitAndLossSequences volumeAllocatedSequences) {
		final Map<@NonNull IVesselCharter, List<VolumeAllocatedSequence>> fobSaleSequences = extractFobSaleSequences(volumeAllocatedSequences);
		final List<@NonNull IResource> resources = new ArrayList<>(fobSaleSequences.size());
		final Map<@NonNull IResource, @NonNull IModifiableSequence> sequenceMap = new HashMap<>();
		for (final Entry<@NonNull IVesselCharter, List<VolumeAllocatedSequence>> entry : fobSaleSequences.entrySet()) {
			final @NonNull IVesselCharter vc = entry.getKey();
			final IResource resource = vesselProvider.getResource(vc);
			final List<ISequenceElement> elems = new ArrayList<>(entry.getValue().size() * 2 + 2);
			elems.add(startEndRequirementProvider.getStartElement(resource));
			for (final VolumeAllocatedSequence vas : entry.getValue()) {
				elems.add(vas.getSequence().get(1));
				elems.add(vas.getSequence().get(2));
			}
			elems.add(startEndRequirementProvider.getEndElement(resource));
			resources.add(resource);
			sequenceMap.put(resource, new ListModifiableSequence(elems));
		}
		return new ModifiableSequences(resources, sequenceMap);
	}

	public void setEvaluationElements(@NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		final Set<@NonNull ISequenceElement> allElements = getAllScheduledSequenceElements(evaluationState, solution);
		evaluationState.setData(ALL_ELEMENTS, allElements);
		final Set<@NonNull ISequenceElement> optimisationElements = getOptimisationSequenceElements(solution);
		evaluationState.setData(OPTIMISATION_ELEMENTS, optimisationElements);
		final Set<@NonNull ISequenceElement> additionalElements = getAdditionalSequenceElements(allElements, optimisationElements);
		evaluationState.setData(ADDITIONAL_ELEMENTS, additionalElements);
	}

	@NonNull
	private Set<@NonNull ISequenceElement> getAllScheduledSequenceElements(@NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution annotatedSolution) {
		final Set<@NonNull ISequenceElement> allElements = new HashSet<>();
		final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
		assert volumeAllocatedSequences != null;
		for (final VolumeAllocatedSequence scheduledSequence : volumeAllocatedSequences) {
			for (final VoyagePlanRecord vpr : scheduledSequence.getVoyagePlanRecords()) {
				for (final IPortSlot portSlot : vpr.getPortTimesRecord().getSlots()) {
					allElements.add(portSlotProvider.getElement(portSlot));
				}
			}
		}
		allElements.addAll(optimisationData.getSequenceElements());
		return allElements;
	}

	@NonNull
	private Set<@NonNull ISequenceElement> getOptimisationSequenceElements(final @NonNull IAnnotatedSolution solution) {
		final Set<@NonNull ISequenceElement> optimisationElements = new HashSet<>(optimisationData.getSequenceElements());
		return optimisationElements;
	}

	@NonNull
	private Set<@NonNull ISequenceElement> getAdditionalSequenceElements(final Set<@NonNull ISequenceElement> allElements, final Set<@NonNull ISequenceElement> optimisationElements) {
		final Set<@NonNull ISequenceElement> additionalElements = new HashSet<>(allElements);
		additionalElements.removeAll(optimisationElements);
		return additionalElements;
	}

}
