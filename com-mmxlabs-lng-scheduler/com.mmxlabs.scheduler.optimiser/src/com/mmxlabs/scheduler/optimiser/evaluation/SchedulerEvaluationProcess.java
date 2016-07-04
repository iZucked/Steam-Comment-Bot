/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.evaluation;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.schedule.ProfitAndLossCalculator;
import com.mmxlabs.scheduler.optimiser.schedule.ScheduleCalculator;

public class SchedulerEvaluationProcess implements IEvaluationProcess {

	// Constants used for keys into IEvaluationState
	@NonNull
	public static final String VOLUME_ALLOCATED_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-volume-allocated-sequences";
	@NonNull
	public static final String PROFIT_AND_LOSS_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-profit-and-loss-sequences";

	@NonNull
	public static final String ALL_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-all-elements";

	@NonNull
	public static final String OPTIMISATION_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-optimisation-elements";

	@NonNull
	public static final String ADDITIONAL_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-additional-elements";

	@Inject
	@NonNull
	private ISequenceScheduler scheduler;

	@Inject
	private ScheduleCalculator scheduleCalculator;

	@Inject
	private ProfitAndLossCalculator profitAndLossCalculator;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

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
			// Calculate arrival times for sequences
			final int @Nullable [][] arrivalTimes = scheduler.schedule(sequences);
			if (arrivalTimes == null) {
				return false;
			}

			@Nullable
			final VolumeAllocatedSequences volumeAllocatedSequences = scheduleCalculator.schedule(sequences, arrivalTimes, solution);
			if (volumeAllocatedSequences == null) {
				return false;
			}

			// Store evaluated state
			evaluationState.setData(VOLUME_ALLOCATED_SEQUENCES, volumeAllocatedSequences);

			if (solution != null) {
				setEvaluationElements(evaluationState, solution);
			}

		} else if (phase == Phase.Final_Evaluation) {

			final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
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
		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
		assert volumeAllocatedSequences != null;
		for (final VolumeAllocatedSequence scheduledSequence : volumeAllocatedSequences) {
			for (final IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
				allElements.add(portSlotProvider.getElement(portSlot));
			}
		}
		allElements.addAll(annotatedSolution.getContext().getOptimisationData().getSequenceElements());
		return allElements;
	}

	@NonNull
	private Set<@NonNull ISequenceElement> getOptimisationSequenceElements(final @NonNull IAnnotatedSolution solution) {
		final Set<@NonNull ISequenceElement> optimisationElements = new HashSet<>(solution.getContext().getOptimisationData().getSequenceElements());
		return optimisationElements;
	}

	@NonNull
	private Set<@NonNull ISequenceElement> getAdditionalSequenceElements(final Set<@NonNull ISequenceElement> allElements, final Set<@NonNull ISequenceElement> optimisationElements) {
		final Set<@NonNull ISequenceElement> additionalElements = new HashSet<>(allElements);
		additionalElements.removeAll(optimisationElements);
		return additionalElements;
	}

}
