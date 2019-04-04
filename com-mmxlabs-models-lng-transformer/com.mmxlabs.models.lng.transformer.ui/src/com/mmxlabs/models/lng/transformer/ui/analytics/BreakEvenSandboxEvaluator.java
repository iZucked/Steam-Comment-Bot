/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.contracts.ILoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ISalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenLoadPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.impl.BreakEvenSalesPriceCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;

@NonNullByDefault
public class BreakEvenSandboxEvaluator {

	protected static final Logger LOG = LoggerFactory.getLogger(BreakEvenSandboxEvaluator.class);

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private Injector injector;

	@Inject
	private EvaluationHelper evaluationHelper;

	public @Nullable Pair<Integer, Integer> evaluate(ISequences currentRawSequences, IPortSlot target, @Nullable ISequenceElement changeTarget, int changeTargetPrice) {
		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);

		final @NonNull IModifiableSequences currentFullSequences = manipulator.createManipulatedSequences(currentRawSequences);
		if (!evaluationHelper.checkConstraints(currentFullSequences, null)) {
			return null;
		}
		final Pair<@NonNull VolumeAllocatedSequences, @NonNull IEvaluationState> p1 = evaluationHelper.evaluateSequences(currentRawSequences, currentFullSequences, false);

		if (p1 == null) {
			return null;
		}

		final ProfitAndLossSequences profitAndLossSequences = evaluationHelper.evaluateSequences(currentFullSequences, p1);
		assert profitAndLossSequences != null;

		VolumeAllocatedSequence scheduledSequence = profitAndLossSequences.getVolumeAllocatedSequences().getScheduledSequence(target);
		int arrivalTime = scheduledSequence.getArrivalTime(target);

		if (target instanceof LoadOption) {
			LoadOption loadOption = (LoadOption) target;
			ILoadPriceCalculator loadPriceCalculator = loadOption.getLoadPriceCalculator();
			if (loadPriceCalculator instanceof BreakEvenLoadPriceCalculator) {
				BreakEvenLoadPriceCalculator beCalc = (BreakEvenLoadPriceCalculator) loadPriceCalculator;
				return new Pair<>(beCalc.getPrice(), arrivalTime);
			}
		}
		if (target instanceof DischargeOption) {
			DischargeOption dischargeOption = (DischargeOption) target;
			ISalesPriceCalculator salesPriceCalculator = dischargeOption.getDischargePriceCalculator();
			if (salesPriceCalculator instanceof BreakEvenSalesPriceCalculator) {
				BreakEvenSalesPriceCalculator beCalc = (BreakEvenSalesPriceCalculator) salesPriceCalculator;
				return new Pair<>(beCalc.getPrice(), arrivalTime);
			}
		}
		CargoValueAnnotation cargoValueAnnotation = profitAndLossSequences.getCargoValueAnnotation(scheduledSequence.getVoyagePlan(target));
		if (cargoValueAnnotation != null) {
			return new Pair<>(cargoValueAnnotation.getSlotPricePerMMBTu(target), arrivalTime);
		}
		return null;
	}

}
