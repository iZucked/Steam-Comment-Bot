package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.dcproviders.IOptionalElementsProvider;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.inject.scopes.PerChainUnitScope;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.ILatenessComponentParameters.Interval;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@PerChainUnitScope
public class EvaluationHelper {

	@Inject
	@NonNull
	private List<IConstraintChecker> constraintCheckers;

	@Inject
	@NonNull
	private List<IEvaluationProcess> evaluationProcesses;

	@Inject
	@NonNull
	private IPortSlotProvider portSlotProvider;

	@Inject
	@NonNull
	private ISequencesManipulator sequenceManipulator;

	// @Inject
	// @NonNull
	// private IPortTypeProvider portTypeProvider;

	@Inject
	@NonNull
	private IOptionalElementsProvider optionalElementsProvider;

	private boolean isReevaluating;

	private boolean strictChecking = false;

	public EvaluationHelper() {
		this.isReevaluating = false;
	}

	public EvaluationHelper(boolean isReevaluating) {
		this.isReevaluating = isReevaluating;
	}

	public boolean checkConstraints(@NonNull final ISequences currentFullSequences, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {

			if (!isStrictChecking()) {
				// For the action set threads, we do not want this constraint checker to apply

				// Should be feature check? (no-nominal-in-prompt)

				// TODO: Use marker interface?
				if (checker instanceof PromptRoundTripVesselPermissionConstraintChecker) {
					continue;
				}
			}

			if (checker.checkConstraints(currentFullSequences, currentChangedResources) == false) {
				// Break out
				return false;
			}
		}
		return true;
	}

	public boolean doesMovePassConstraints(final ISequences rawSequences) {

		// Do normal manipulation
		final ISequences movedFullSequences = sequenceManipulator.createManipulatedSequences(rawSequences);

		// Run through the constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(movedFullSequences, null)) {
				return false;
			}
		}

		return true;
	}

	public long calculateUnusedCompulsarySlot(final @NonNull ISequences rawSequences) {

		int thisUnusedCompulsarySlotCount = 0;
		for (final ISequenceElement e : rawSequences.getUnusedElements()) {
			if (optionalElementsProvider.isElementRequired(e)) {
				thisUnusedCompulsarySlotCount++;
			}
		}
		return thisUnusedCompulsarySlotCount;
	}

	public @Nullable Pair<@NonNull VolumeAllocatedSequences, @NonNull IEvaluationState> evaluateSequences(@NonNull final ISequences currentFullSequences) {
		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, currentFullSequences, evaluationState)) {
				return null;
			}
		}

		final VolumeAllocatedSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, VolumeAllocatedSequences.class);
		assert volumeAllocatedSequences != null;

		return new Pair<>(volumeAllocatedSequences, evaluationState);
	}

	public @Nullable ProfitAndLossSequences evaluateSequences(@NonNull final ISequences currentFullSequences, @NonNull final Pair<@NonNull VolumeAllocatedSequences, @NonNull IEvaluationState> p) {
		@NonNull
		final IEvaluationState evaluationState = p.getSecond();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(Phase.Final_Evaluation, currentFullSequences, evaluationState)) {
				return null;
			}
		}
		final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
		assert profitAndLossSequences != null;

		return profitAndLossSequences;
	}

	public long calculateSchedulePNL(@NonNull final ISequences fullSequences, @NonNull final ProfitAndLossSequences scheduledSequences) {
		long sumPNL = 0;

		for (final VolumeAllocatedSequence scheduledSequence : scheduledSequences.getVolumeAllocatedSequences()) {
			for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> p : scheduledSequence.getVoyagePlans()) {
				sumPNL += scheduledSequences.getVoyagePlanGroupValue(p.getFirst());
			}
		}

		for (final ISequenceElement element : fullSequences.getUnusedElements()) {
			assert element != null;
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			assert portSlot != null;
			sumPNL += scheduledSequences.getUnusedSlotGroupValue(portSlot);
		}
		return sumPNL;
	}

	public long calculateScheduleLateness(final @NonNull ISequences fullSequences, final @NonNull VolumeAllocatedSequences volumeAllocatedSequences) {
		long sumCost = 0;

		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			for (final IPortSlot lateSlot : volumeAllocatedSequence.getLateSlotsSet()) {
				@Nullable
				final Pair<Interval, Long> latenessCost = volumeAllocatedSequence.getLatenessCost(lateSlot);
				if (latenessCost != null) {
					sumCost += latenessCost.getSecond();
				}
			}
		}
		return sumCost;
	}

	public long calculateScheduleCapacity(final @NonNull ISequences fullSequences, final @NonNull VolumeAllocatedSequences volumeAllocatedSequences) {
		long sumCost = 0;
		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			for (final IPortSlot portSlot : volumeAllocatedSequence.getSequenceSlots()) {
				sumCost += volumeAllocatedSequence.getCapacityViolationCount(portSlot);
			}
		}
		return sumCost;
	}

	public @Nullable IEvaluationState evaluateSequence(@NonNull final ISequences currentFullSequences) {
		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(currentFullSequences, evaluationState)) {
				return null;
			}
		}

		// Make sure this object exists if we have got this far
		final ProfitAndLossSequences ss = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
		assert ss != null;

		return evaluationState;
	}

	public long @Nullable [] evaluateState(@NonNull final ISequences currentRawSequences, @Nullable final Collection<@NonNull IResource> currentChangedResources,
			final long @Nullable [] referenceMetrics, @Nullable ISearchStatisticsLogger logger) {

		// Do normal manipulation
		final @NonNull ISequences currentFullSequences = sequenceManipulator.createManipulatedSequences(currentRawSequences);

		return evaluateState(currentRawSequences, currentFullSequences, currentChangedResources, referenceMetrics, logger);
	}

	public long @Nullable [] evaluateState(@NonNull final ISequences currentRawSequences, final @NonNull ISequences currentFullSequences,
			@Nullable final Collection<@NonNull IResource> currentChangedResources, final long @Nullable [] referenceMetrics, @Nullable ISearchStatisticsLogger logger) {
		// Apply hard constraint checkers
		if (!checkConstraints(currentFullSequences, currentChangedResources)) {
			if (logger != null) {
				logger.logEvaluationsFailedConstraints();
			}
			return null;
		}
		final long thisUnusedCompulsarySlotCount = calculateUnusedCompulsarySlot(currentRawSequences);
		if (referenceMetrics != null && thisUnusedCompulsarySlotCount > referenceMetrics[MetricType.COMPULSARY_SLOT.ordinal()]) {
			return null;
		}

		final Pair<@NonNull VolumeAllocatedSequences, @NonNull IEvaluationState> p1 = evaluateSequences(currentFullSequences);
		/*
		 * This is to increase runtime temporarily
		 */
		final Pair<@NonNull VolumeAllocatedSequences, @NonNull IEvaluationState> p2 = isReevaluating ? evaluateSequences(currentFullSequences) : null;
		if (p1 == null) {
			return null;
		}

		boolean failedEvaluation = false;
		final long thisLateness = calculateScheduleLateness(currentFullSequences, p1.getFirst());

		if (referenceMetrics != null && thisLateness > referenceMetrics[MetricType.LATENESS.ordinal()]) {
			failedEvaluation = true;
		} else {
			// currentLateness = thisLateness;
		}

		final long thisCapacity = calculateScheduleCapacity(currentFullSequences, p1.getFirst());
		if (referenceMetrics != null && thisCapacity > referenceMetrics[MetricType.CAPACITY.ordinal()]) {
			failedEvaluation = true;
		} else {
			// currentLateness = thisLateness;
		}

		if (failedEvaluation) {
			if (logger != null) {
				logger.logEvaluationsFailedPNL();
			}
			return null;
		}

		if (logger != null) {
			logger.logEvaluationsPassed();
		}

		final ProfitAndLossSequences profitAndLossSequences = evaluateSequences(currentFullSequences, p1);
		assert profitAndLossSequences != null;

		if (isReevaluating) {
			if (p2 != null) {
				evaluateSequences(currentFullSequences, p2);
			}
		}

		final long thisPNL = calculateSchedulePNL(currentFullSequences, profitAndLossSequences);
		final long[] metrics = new long[MetricType.values().length];
		metrics[MetricType.CAPACITY.ordinal()] = thisCapacity;
		metrics[MetricType.COMPULSARY_SLOT.ordinal()] = thisUnusedCompulsarySlotCount;
		metrics[MetricType.LATENESS.ordinal()] = thisLateness;
		metrics[MetricType.PNL.ordinal()] = thisPNL;

		return metrics;
	}

	public boolean isStrictChecking() {
		return strictChecking;
	}

	public void setStrictChecking(boolean strictChecking) {
		this.strictChecking = strictChecking;
	}

}