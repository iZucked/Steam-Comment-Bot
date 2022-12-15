/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.moves.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.paperdeals.BasicPaperDealAllocationEntry;
import com.mmxlabs.common.paperdeals.BasicPaperDealData;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintChecker;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.OptimiserConstants;
import com.mmxlabs.optimiser.core.constraints.IConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IEvaluatedStateConstraintChecker;
import com.mmxlabs.optimiser.core.constraints.IInitialSequencesConstraintChecker;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.optimiser.core.inject.scopes.ThreadLocalScope;
import com.mmxlabs.optimiser.core.scenario.IPhaseOptimisationData;
import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.constraints.impl.CapacityEvaluatedStateChecker;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintChecker;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManager;
import com.mmxlabs.scheduler.optimiser.providers.ILazyExpressionManagerContainer;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPriceExpressionProvider;
import com.mmxlabs.scheduler.optimiser.providers.PriceCurveKey;
import com.mmxlabs.scheduler.optimiser.schedule.CapacityViolationChecker;
import com.mmxlabs.scheduler.optimiser.voyage.impl.CapacityViolationType;

@ThreadLocalScope
public class EvaluationHelper {
	protected static final Logger LOG = LoggerFactory.getLogger(EvaluationHelper.class);

	@Inject
	private List<IConstraintChecker> constraintCheckers;

	@Inject
	private List<IEvaluationProcess> evaluationProcesses;

	@Inject
	private List<@NonNull IEvaluatedStateConstraintChecker> evaluatedStateConstraintCheckers;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private ISequencesManipulator sequenceManipulator;

	@Inject
	private IPhaseOptimisationData phaseOptimisationData;

	@Inject
	private IPriceExpressionProvider priceExpressionProvider;

	@Inject
	ILazyExpressionManagerContainer lazyManagerContainer;

	@Inject
	@Named(SchedulerConstants.GENERATED_PAPERS_IN_PNL)
	private boolean generatedPapersInPNL;

	private boolean strictChecking = false;

	private int flexibleViolationCount;

	public void setFlexibleCapacityViolationCount(final int flexibleSoftViolations) {

		flexibleViolationCount = flexibleSoftViolations;
		for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
			if (checker instanceof CapacityEvaluatedStateChecker capacityEvaluatedStateChecker) {
				capacityEvaluatedStateChecker.setFlexibleSoftViolations(flexibleSoftViolations);
			}
		}
	}

	/**
	 * Instruct the constraint checkers to accept the given sequence as the valid
	 * starting state.
	 * 
	 * @param currentRawSequences
	 * @param currentFullSequences
	 * @return
	 */
	public boolean acceptSequences(@NonNull final ISequences currentRawSequences, @NonNull final ISequences currentFullSequences) {

		for (final IConstraintChecker checker : constraintCheckers) {
			if (checker instanceof IInitialSequencesConstraintChecker initialSequencesConstraintChecker) {
				initialSequencesConstraintChecker.sequencesAccepted(currentRawSequences, currentFullSequences, new ArrayList<>());
			}
		}

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, currentFullSequences, evaluationState)) {
				return false;
			}
		}

		for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
			checker.acceptSequences(currentRawSequences, currentFullSequences, evaluationState);
		}
		return true;
	}

	public boolean checkConstraints(@NonNull final ISequences currentFullSequences, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: checkConstraints", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(currentFullSequences, currentChangedResources, messages)) {
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				// Break out
				return false;
			}
		}
		return true;
	}

	/**
	 * To be used only for Manual Sandbox, when user expects results similar to
	 * manual rewiring
	 * 
	 * @param currentFullSequences
	 * @param currentChangedResources
	 * @return
	 */
	public boolean checkConstraintsForRelaxedConstraints(@NonNull final ISequences currentFullSequences, @Nullable final Collection<@NonNull IResource> currentChangedResources) {
		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: checkConstraintsForRelaxedConstraints", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Apply hard constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {

			if (checker instanceof RoundTripVesselPermissionConstraintChecker) {
				continue;
			}
			if (checker instanceof OrderedSequenceElementsConstraintChecker) {
				continue;
			}

			if (!checker.checkConstraints(currentFullSequences, currentChangedResources, messages)) {
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty())
					messages.stream().forEach(LOG::debug);
				// Break out
				return false;
			}
		}
		return true;
	}

	public boolean doesMovePassConstraints(final @NonNull ISequences rawSequences) {
		final List<@NonNull String> messages;
		if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES) {
			messages = new ArrayList<>();
			messages.add(String.format("%s: doesMovePassConstraints", this.getClass().getName()));
		} else {
			messages = null;
		}
		// Do normal manipulation
		final ISequences movedFullSequences = sequenceManipulator.createManipulatedSequences(rawSequences);

		// Run through the constraint checkers
		for (final IConstraintChecker checker : constraintCheckers) {
			if (!checker.checkConstraints(movedFullSequences, null, messages)) {
				if (OptimiserConstants.SHOW_CONSTRAINTS_FAIL_MESSAGES && !messages.isEmpty()) {
					messages.stream().forEach(LOG::debug);
				}
				return false;
			}
		}

		return true;
	}

	public long calculateUnusedCompulsarySlot(final @NonNull ISequences rawSequences) {

		int thisUnusedCompulsarySlotCount = 0;
		for (final ISequenceElement e : rawSequences.getUnusedElements()) {
			if (phaseOptimisationData.isElementRequired(e)) {
				thisUnusedCompulsarySlotCount++;
			}
		}
		return thisUnusedCompulsarySlotCount;
	}

	public @Nullable Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState> evalulateSequences(@NonNull final ISequences sequences,
			@NonNull List<@NonNull PriceCurveKey> priceCurveCombination) {
		try (final ILazyExpressionManager expressionManager = lazyManagerContainer.getExpressionManager()) {
			for (final PriceCurveKey key : priceCurveCombination) {
				expressionManager.setPriceCurve(key.getIndexName().toLowerCase(), key.seriesType(), priceExpressionProvider.getExpression(key));
			}
			expressionManager.initialiseAllPricingData();

			final IEvaluationState evaluationState = new EvaluationState();
			for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
				if (!evaluationProcess.evaluate(sequences, evaluationState)) {
					return null;
				}
			}
			final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
			assert volumeAllocatedSequences != null;

			return Pair.of(volumeAllocatedSequences, evaluationState);
		} catch (Exception e) {
			return null;
		}
	}

	public @Nullable Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState> evaluateSequences(@NonNull final ISequences currentRawSequences, @NonNull final ISequences currentFullSequences,
			final boolean checkEvaluatedStateCheckers) {

		final IEvaluationState evaluationState = new EvaluationState();
		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, currentFullSequences, evaluationState)) {
				return null;
			}
		}

		if (checkEvaluatedStateCheckers) {
			for (final IEvaluatedStateConstraintChecker checker : evaluatedStateConstraintCheckers) {
				if (!checker.checkConstraints(currentRawSequences, currentFullSequences, evaluationState)) {
					return null;
				}
			}
		}

		final ProfitAndLossSequences volumeAllocatedSequences = evaluationState.getData(SchedulerEvaluationProcess.VOLUME_ALLOCATED_SEQUENCES, ProfitAndLossSequences.class);
		assert volumeAllocatedSequences != null;

		return new Pair<>(volumeAllocatedSequences, evaluationState);
	}

	public @Nullable ProfitAndLossSequences evaluateSequences(@NonNull final ISequences currentFullSequences, @NonNull final Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState> p) {

		final @NonNull IEvaluationState evaluationState = p.getSecond();
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

		for (final VolumeAllocatedSequence scheduledSequence : scheduledSequences) {
			for (final VoyagePlanRecord p : scheduledSequence.getVoyagePlanRecords()) {
				sumPNL = Math.addExact(sumPNL, p.getProfitAndLoss());
			}
		}

		for (final ISequenceElement element : fullSequences.getUnusedElements()) {
			assert element != null;
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			assert portSlot != null;
			sumPNL = Math.addExact(sumPNL, scheduledSequences.getUnusedSlotGroupValue(portSlot));
		}

		sumPNL = Math.addExact(sumPNL, computePaperPnL(scheduledSequences));

		return sumPNL;
	}

	public long calculateScheduleLateness(final @NonNull ProfitAndLossSequences volumeAllocatedSequences) {
		long sumCost = 0;

		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			for (final VoyagePlanRecord vpr : volumeAllocatedSequence.getVoyagePlanRecords()) {
				for (final IPortSlot lateSlot : vpr.getLateSlotsSet()) {
					sumCost += vpr.getLatenessWithFlex(lateSlot);
				}
			}

			if (volumeAllocatedSequence.getMaxDurationLatenessRecord() != null) {
				sumCost += volumeAllocatedSequence.getMaxDurationLatenessRecord().latenessWithFlex;
			}
		}

		return sumCost;
	}

	public long calculateScheduleCapacity(final @NonNull ProfitAndLossSequences volumeAllocatedSequences) {
		long sumCost = 0;
		int flexAvail = this.flexibleViolationCount;
		for (final VolumeAllocatedSequence volumeAllocatedSequence : volumeAllocatedSequences) {
			for (final VoyagePlanRecord vpr : volumeAllocatedSequence.getVoyagePlanRecords()) {
				for (final IPortSlot portSlot : vpr.getPortTimesRecord().getSlots()) {
					final List<CapacityViolationType> violations = vpr.getCapacityViolations(portSlot);
					for (final CapacityViolationType violation : violations) {
						if (CapacityViolationChecker.isHardViolation(violation)) {
							++sumCost;
						} else {
							if (flexAvail <= 0) {
								++sumCost;
							} else {
								if (flexAvail != Integer.MAX_VALUE) {
									--flexAvail;
								}
							}
						}
					}
				}
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

	public long @Nullable [] evaluateState(@NonNull final ISequences currentRawSequences, @Nullable final Collection<@NonNull IResource> currentChangedResources, final boolean checkConstraintCheckers,
			final boolean checkEvaluatedStateCheckers, final long @Nullable [] referenceMetrics, @Nullable final ISearchStatisticsLogger logger) {

		// Do normal manipulation
		final @NonNull ISequences currentFullSequences = sequenceManipulator.createManipulatedSequences(currentRawSequences);

		return evaluateState(currentRawSequences, currentFullSequences, currentChangedResources, checkConstraintCheckers, checkEvaluatedStateCheckers, referenceMetrics, logger);
	}

	public long @Nullable [] evaluateState(@NonNull final ISequences currentRawSequences, final @NonNull ISequences currentFullSequences,
			@Nullable final Collection<@NonNull IResource> currentChangedResources, final boolean checkConstraintCheckers, final boolean checkEvaluatedStateCheckers,
			final long @Nullable [] referenceMetrics, @Nullable final ISearchStatisticsLogger logger) {
		// Apply hard constraint checkers
		if (checkConstraintCheckers) {
			if (!checkConstraints(currentFullSequences, currentChangedResources)) {
				if (logger != null) {
					logger.logEvaluationsFailedConstraints();
				}
				return null;
			}
		}
		final long thisUnusedCompulsarySlotCount = calculateUnusedCompulsarySlot(currentRawSequences);
		if (referenceMetrics != null && thisUnusedCompulsarySlotCount > referenceMetrics[MetricType.COMPULSARY_SLOT.ordinal()]) {
			return null;
		}

		final Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState> p1 = evaluateSequences(currentRawSequences, currentFullSequences, checkEvaluatedStateCheckers);

		if (p1 == null) {
			return null;
		}

		boolean failedEvaluation = false;
		final long thisLateness = calculateScheduleLateness(p1.getFirst());

		if (referenceMetrics != null && thisLateness > referenceMetrics[MetricType.LATENESS.ordinal()]) {
			failedEvaluation = true;
		}

		final long thisCapacity = calculateScheduleCapacity(p1.getFirst());
		if (referenceMetrics != null && thisCapacity > referenceMetrics[MetricType.CAPACITY.ordinal()]) {
			failedEvaluation = true;
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

	public void setStrictChecking(final boolean strictChecking) {
		this.strictChecking = strictChecking;
	}

	private long computePaperPnL(final ProfitAndLossSequences profitAndLossSequences) {
		long paperPnL = 0;
		if (generatedPapersInPNL) {
			final Map<BasicPaperDealData, List<BasicPaperDealAllocationEntry>> paperDealAllocations = profitAndLossSequences.getPaperDealRecords();

			for (final BasicPaperDealData basicPaperDealData : paperDealAllocations.keySet()) {
				for (final BasicPaperDealAllocationEntry entry : paperDealAllocations.get(basicPaperDealData)) {
					paperPnL += entry.getValue();
				}
			}
		}

		return paperPnL;
	}
}