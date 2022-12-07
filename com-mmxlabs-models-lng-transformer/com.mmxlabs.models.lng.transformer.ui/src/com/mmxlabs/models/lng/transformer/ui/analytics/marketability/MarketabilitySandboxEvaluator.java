/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.marketability;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.ui.analytics.marketability.MarketabilitySandboxUnit.SingleResult;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.IResource;
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
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;

@NonNullByDefault
public class MarketabilitySandboxEvaluator {

	protected static final Logger LOG = LoggerFactory.getLogger(MarketabilitySandboxEvaluator.class);

	@Inject
	private Injector injector;

	@Inject
	private EvaluationHelper evaluationHelper;

	public @Nullable SingleResult evaluate(IResource resource, final ISequences currentRawSequences, final IPortSlot target) {
		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);

		final @NonNull IModifiableSequences currentFullSequences = manipulator.createManipulatedSequences(currentRawSequences);
		if (!evaluationHelper.checkConstraints(currentFullSequences, null)) {
			return null;
		}
		final Pair<@NonNull ProfitAndLossSequences, @NonNull IEvaluationState> p1 = evaluationHelper.evaluateSequences(currentRawSequences, currentFullSequences, true);

		if (p1 == null) {
			return null;
		}

		final ProfitAndLossSequences profitAndLossSequences = evaluationHelper.evaluateSequences(currentFullSequences, p1);
		assert profitAndLossSequences != null;

		final VolumeAllocatedSequence scheduledSequence = profitAndLossSequences.getScheduledSequenceForResource(resource);
		VoyagePlanRecord vpr = scheduledSequence.getVoyagePlanRecord(target);
		final ICargoValueAnnotation cargoValueAnnotation = vpr.getCargoValueAnnotation();
		final int arrivalTime = vpr.getPortTimesRecord().getSlotTime(target);
		final long volume = cargoValueAnnotation.getPhysicalSlotVolumeInMMBTu(target);

		if (target instanceof LoadOption) {
			final LoadOption loadOption = (LoadOption) target;
			final ILoadPriceCalculator loadPriceCalculator = loadOption.getLoadPriceCalculator();
			if (loadPriceCalculator instanceof BreakEvenLoadPriceCalculator) {
				final BreakEvenLoadPriceCalculator beCalc = (BreakEvenLoadPriceCalculator) loadPriceCalculator;
				return saveResult(beCalc.getPrice(), arrivalTime, volume);
			}
		}
		if (target instanceof DischargeOption) {
			final DischargeOption dischargeOption = (DischargeOption) target;
			final ISalesPriceCalculator salesPriceCalculator = dischargeOption.getDischargePriceCalculator();
			if (salesPriceCalculator instanceof BreakEvenSalesPriceCalculator) {
				final BreakEvenSalesPriceCalculator beCalc = (BreakEvenSalesPriceCalculator) salesPriceCalculator;
				return saveResult(beCalc.getPrice(), arrivalTime, volume);
			}
		}

		if (cargoValueAnnotation != null) {
			return saveResult(cargoValueAnnotation.getSlotPricePerMMBTu(target), arrivalTime, volume);
		}
		return null;
	}

	private SingleResult saveResult(final int price, final int arrivalTime, final long volume) {
		final SingleResult result = new SingleResult();
		result.price = price;
		result.arrivalTime = arrivalTime;
		result.volume = volume;
		return result;
	}

}
