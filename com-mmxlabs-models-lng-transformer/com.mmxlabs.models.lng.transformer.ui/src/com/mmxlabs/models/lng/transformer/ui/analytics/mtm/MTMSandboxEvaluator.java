/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.analytics.mtm;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.transformer.ui.analytics.mtm.MTMSanboxUnit.SingleResult;
import com.mmxlabs.optimiser.core.IModifiableSequences;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
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
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class MTMSandboxEvaluator {

	protected static final Logger LOG = LoggerFactory.getLogger(MTMSandboxEvaluator.class);

	@Inject
	private Injector injector;

	@Inject
	private EvaluationHelper evaluationHelper;
	
	@Inject
	private ShippingCostHelper shippingCostHelper;

	public @Nullable SingleResult evaluate(final ISequences currentRawSequences, final IPortSlot target) {
		final ISequencesManipulator manipulator = injector.getInstance(ISequencesManipulator.class);
		

		final @NonNull IModifiableSequences currentFullSequences = manipulator.createManipulatedSequences(currentRawSequences);
		if (!evaluationHelper.checkConstraints(currentFullSequences, null)) {
			return null;
		}
		final Pair<@NonNull VolumeAllocatedSequences, @NonNull IEvaluationState> p1 = evaluationHelper.evaluateSequences(currentRawSequences, currentFullSequences, true);

		if (p1 == null) {
			return null;
		}

		final ProfitAndLossSequences profitAndLossSequences = evaluationHelper.evaluateSequences(currentFullSequences, p1);

		assert profitAndLossSequences != null;
		
		final VoyagePlan voyagePlan = p1.getFirst().getVoyagePlan(target);
		assert voyagePlan != null;
		final IVesselAvailability va = p1.getFirst().getVesselAvailability(target);
		assert va != null;

		final VolumeAllocatedSequence scheduledSequence = profitAndLossSequences.getVolumeAllocatedSequences().getScheduledSequence(target);
		final CargoValueAnnotation cargoValueAnnotation = profitAndLossSequences.getCargoValueAnnotation(scheduledSequence.getVoyagePlan(target));
		final int arrivalTime = scheduledSequence.getArrivalTime(target);
		final long volumeInMMBTU = cargoValueAnnotation.getPhysicalSlotVolumeInMMBTu(target);
		final long shippingCost = shippingCostHelper.getShippingCosts(voyagePlan, va, true);

		
		final int slotPricePerMMBTu = cargoValueAnnotation.getSlotPricePerMMBTu(target);
		
		final int shippingCostPerMMBTu = Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(shippingCost, volumeInMMBTU);
		
		//might want different volume for the shipping cost
		if (target instanceof LoadOption) {
			final int netbackPrice = slotPricePerMMBTu + shippingCostPerMMBTu;
			
			return saveResult(netbackPrice, arrivalTime, volumeInMMBTU, shippingCost);
		}
		if (target instanceof DischargeOption) {
			final int netbackPrice = slotPricePerMMBTu - shippingCostPerMMBTu;
			
			return saveResult(netbackPrice, arrivalTime, volumeInMMBTU, shippingCost);
		}
		
		if (cargoValueAnnotation != null) {
			return saveResult(cargoValueAnnotation.getSlotPricePerMMBTu(target), arrivalTime, volumeInMMBTU, shippingCost);
		}
		return null;
	}
	
	private SingleResult saveResult(final int price, final int arrivalTime, final long volumeInMMBTU, final long shippingCost) {
		final SingleResult result = new SingleResult();
		result.price = price;
		result.arrivalTime = arrivalTime;
		result.volumeInMMBTU = volumeInMMBTU;
		result.shippingCost = shippingCost;
		return result;
	}

}
