/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.stochasticactionsets;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequence;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationProcess.Phase;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.optimiser.core.evaluation.impl.EvaluationState;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.annotations.IHeelLevelAnnotation;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.evaluation.SchedulerEvaluationProcess;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

public class BreakEvenOptimiser {

	@Inject
	@NonNull
	private List<IEvaluationProcess> evaluationProcesses;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private ISequencesManipulator manipulator;

	public void optimise(final ISequences rawSequences, final long targetProfitAndLoss) {

		boolean isPurchase = false;
		boolean isSale = false;
		List<IBreakEvenPriceCalculator> calculators = new LinkedList<>();

		for (IResource r : rawSequences.getResources()) {
			ISequence seq = rawSequences.getSequence(r);
			for (ISequenceElement element : seq) {
				IPortSlot portSlot = portSlotProvider.getPortSlot(element);
				if (portSlot instanceof ILoadOption) {
					ILoadOption loadOption = (ILoadOption) portSlot;
					if (loadOption.getLoadPriceCalculator() instanceof IBreakEvenPriceCalculator) {
						calculators.add((IBreakEvenPriceCalculator) loadOption.getLoadPriceCalculator());
						isPurchase = true;
						if (isSale) {
							// No mixed mode
							return;
						}

					}
				} else if (portSlot instanceof IDischargeOption) {
					IDischargeOption dischargeOption = (IDischargeOption) portSlot;
					if (dischargeOption.getDischargePriceCalculator() instanceof IBreakEvenPriceCalculator) {
						calculators.add((IBreakEvenPriceCalculator) dischargeOption.getDischargePriceCalculator());
						isSale = true;
						if (isPurchase) {
							// No mixed mode
							return;
						}
					}
				}
			}
		}

		optimise(rawSequences, targetProfitAndLoss, isPurchase, calculators);
		return;

	}

	public void optimise(final ISequences rawSequences, final long targetProfitAndLoss, boolean isPurchase, final List<IBreakEvenPriceCalculator> calculators) {

		final ISequences fullSequences = manipulator.createManipulatedSequences(rawSequences);

		final int minPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(0.0);
		final int maxPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(30.0);

		final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, maxPricePerMMBTu, targetProfitAndLoss, fullSequences, isPurchase, calculators);
		calculators.forEach(c -> c.setPrice(breakEvenPricePerMMBtu));
	}

	private int search(final int min, final int max, final long targetValue, final ISequences fullSequences, boolean isPurchase, final List<IBreakEvenPriceCalculator> calculators) {

		final int mid = min + ((max - min) / 2);

		// TODO: Check mid == min || mid == max) - terminate condition.
		if (mid == min || mid == max) {
			// Zero somewhere in this interval, pick closest value.
			// if (Math.abs(minValue) < Math.abs(maxValue)) {
			// Pick min value
			return min;
			// } else {
			// pick max value
			// return max;
			// }
		}

		final long midValue = evaluate(fullSequences, calculators, mid);
		long delta = midValue - targetValue;

		// Search direction - flip depending on purchase or sale
		if (!isPurchase) {
			delta = -delta;
		}

		if (delta < 0) {
			return search(min, mid, targetValue, fullSequences, isPurchase, calculators);
		} else {
			return search(mid, max, targetValue, fullSequences, isPurchase, calculators);
		}
	}

	public long evaluate(@NonNull final ISequences fullSequences, final List<IBreakEvenPriceCalculator> breakEvenPriceCalculators, final int pricePerMMBTu) {

		breakEvenPriceCalculators.forEach(c -> c.setPrice(pricePerMMBTu));

		final IEvaluationState evaluationState = new EvaluationState();

		for (final IEvaluationProcess evaluationProcess : evaluationProcesses) {
			if (!evaluationProcess.evaluate(Phase.Checked_Evaluation, fullSequences, evaluationState)) {
				break;
			}
			if (!evaluationProcess.evaluate(Phase.Final_Evaluation, fullSequences, evaluationState)) {
				break;
			}
		}

		final ProfitAndLossSequences profitAndLossSequences = evaluationState.getData(SchedulerEvaluationProcess.PROFIT_AND_LOSS_SEQUENCES, ProfitAndLossSequences.class);
		assert profitAndLossSequences != null;

		return calculateSchedulePNL(fullSequences, profitAndLossSequences);

	}

	public long calculateSchedulePNL(@NonNull final ISequences fullSequences, @NonNull final ProfitAndLossSequences scheduledSequences) {
		long sumPNL = 0;

		for (final VolumeAllocatedSequence volumeAllocatedSequence : scheduledSequences.getVolumeAllocatedSequences()) {

			for (final Triple<VoyagePlan, Map<IPortSlot, IHeelLevelAnnotation>, IPortTimesRecord> p : volumeAllocatedSequence.getVoyagePlans()) {
				sumPNL += scheduledSequences.getVoyagePlanGroupValue(p.getFirst());
			}
		}

		for (final ISequenceElement element : fullSequences.getUnusedElements()) {
			final IPortSlot portSlot = portSlotProvider.getPortSlot(element);
			assert portSlot != null;
			sumPNL += scheduledSequences.getUnusedSlotGroupValue(portSlot);
		}
		return sumPNL;
	}

}
