/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.optimiser.core.ISequencesManipulator;
import com.mmxlabs.optimiser.core.evaluation.IEvaluationState;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.impl.DischargeOption;
import com.mmxlabs.scheduler.optimiser.components.impl.LoadOption;
import com.mmxlabs.scheduler.optimiser.evaluation.VoyagePlanRecord;
import com.mmxlabs.scheduler.optimiser.fitness.ProfitAndLossSequences;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequence;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.moves.util.EvaluationHelper;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
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
	private IVesselProvider vesselProvider;

	@Inject
	private ShippingCostHelper shippingCostHelper;

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

		final IVesselCharter va = vesselProvider.getVesselCharter(resource);
		assert va != null;

		final VolumeAllocatedSequence scheduledSequence = profitAndLossSequences.getScheduledSequenceForResource(resource);
		VoyagePlanRecord vpr = scheduledSequence.getVoyagePlanRecord(target);
		assert vpr != null;
		final ICargoValueAnnotation cargoValueAnnotation = scheduledSequence.getVoyagePlanRecord(target).getCargoValueAnnotation();

		final int arrivalTime = vpr.getPortTimesRecord().getSlotTime(target);
		final long volumeInMMBTU = cargoValueAnnotation.getPhysicalSlotVolumeInMMBTu(target);
		VoyagePlan voyagePlan = vpr.getVoyagePlan();
		assert voyagePlan != null;
		final long shippingCost = shippingCostHelper.getShippingCosts(voyagePlan, va, true);

		final int slotPricePerMMBTu = cargoValueAnnotation.getSlotPricePerMMBTu(target);

		final int shippingCostPerMMBTu = volumeInMMBTU == 0 ? 0 : Calculator.getPerMMBTuFromTotalAndVolumeInMMBTu(shippingCost, volumeInMMBTU);

		// might want different volume for the shipping cost
		if (target instanceof LoadOption) {
			return saveResult(slotPricePerMMBTu, arrivalTime, volumeInMMBTU, shippingCost);
		}
		if (target instanceof DischargeOption) {
			return saveResult(slotPricePerMMBTu, arrivalTime, volumeInMMBTU, shippingCost);
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
