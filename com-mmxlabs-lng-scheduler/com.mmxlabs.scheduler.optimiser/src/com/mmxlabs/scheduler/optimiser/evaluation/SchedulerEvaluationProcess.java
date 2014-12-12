package com.mmxlabs.scheduler.optimiser.evaluation;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.fitness.ISequenceScheduler;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;

public class SchedulerEvaluationProcess implements IEvaluationProcess {

	public static final String SCHEDULED_SEQUENCES = "com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess-scheduled-sequences";

	@Inject
	private ISequenceScheduler scheduler;

	@Override
	public boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState) {
		return evaluate(sequences, evaluationState, null);
	}

	@Override
	public void annotate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @NonNull IAnnotatedSolution solution) {
		evaluate(sequences, evaluationState, solution);
	}

	private boolean evaluate(@NonNull final ISequences sequences, @NonNull final IEvaluationState evaluationState, @Nullable IAnnotatedSolution solution) {

		final ScheduledSequences scheduledSequences = scheduler.schedule(sequences, solution);
		if (scheduledSequences == null) {
			return false;
		}

		// Store evaluated state
		assert SCHEDULED_SEQUENCES != null;
		evaluationState.setData(SCHEDULED_SEQUENCES, scheduledSequences);

		return true;
	}

}
