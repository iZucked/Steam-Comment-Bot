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
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

public class SchedulerEvaluationProcess implements IEvaluationProcess {

	public static final String SCHEDULED_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-scheduled-sequences";
	public static final String ALL_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-all-elements";
	public static final String OPTIMISATION_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-optimisation-elements";
	public static final String ADDITIONAL_ELEMENTS = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-additional-elements";

	@Inject
	private ISequenceScheduler scheduler;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		return evaluate(sequences, evaluationState, null);
	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		evaluate(sequences, evaluationState, solution);
	}

	private boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable final IAnnotatedSolution solution) {

		final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, solution);
		if (scheduledSequences == null) {
			return false;
		}

		// Store evaluated state
		assert SCHEDULED_SEQUENCES != null;
		evaluationState.setData(SCHEDULED_SEQUENCES, scheduledSequences);
		if (solution != null) {
			setEvaluationElements(evaluationState, solution);
		}
		return true;
	}

	public void setEvaluationElements(@NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution solution) {
		final Set<ISequenceElement> allElements = getAllScheduledSequenceElements(evaluationState, solution);
		evaluationState.setData(ALL_ELEMENTS, allElements);
		final Set<ISequenceElement> optimisationElements = getOptimisationSequenceElements(solution);
		evaluationState.setData(OPTIMISATION_ELEMENTS, optimisationElements);
		final Set<ISequenceElement> additionalElements = getAdditionalSequenceElements(allElements, optimisationElements);
		evaluationState.setData(ADDITIONAL_ELEMENTS, additionalElements);
	}

	private Set<ISequenceElement> getAllScheduledSequenceElements(@NonNull final IEvaluationState evaluationState, @NonNull final IAnnotatedSolution annotatedSolution) {
		final Set<ISequenceElement> allElements = new HashSet<ISequenceElement>();
		final ScheduledSequences scheduledSequences = evaluationState.getData(SchedulerEvaluationProcess.SCHEDULED_SEQUENCES, ScheduledSequences.class);
		assert scheduledSequences != null;
		for (final ScheduledSequence scheduledSequence : scheduledSequences) {
			for (final IPortSlot portSlot : scheduledSequence.getSequenceSlots()) {
				allElements.add(portSlotProvider.getElement(portSlot));
			}
		}
		allElements.addAll(annotatedSolution.getContext().getOptimisationData().getSequenceElements());
		return allElements;
	}

	private Set<ISequenceElement> getOptimisationSequenceElements(final IAnnotatedSolution solution) {
		final Set<ISequenceElement> optimisationElements = new HashSet<ISequenceElement>(solution.getContext().getOptimisationData().getSequenceElements());
		return optimisationElements;
	}

	private Set<ISequenceElement> getAdditionalSequenceElements(final Set<ISequenceElement> allElements, final Set<ISequenceElement> optimisationElements) {
		final Set<ISequenceElement> additionalElements = new HashSet<ISequenceElement>(allElements);
		additionalElements.removeAll(optimisationElements);
		return additionalElements;
	}

}
