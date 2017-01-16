/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.fitness.VolumeAllocatedSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * Implementations represent load-side contracts, which are invoked after scheduling and shipping level calculations.
 * 
 * @author hinton
 * 
 */
public interface ILoadPriceCalculator extends ICalculator {
	/**
	 * This will be called with a {@link ISequences} instance once before any of the load slots therein contained have their prices calculated with the
	 * {@link #calculateLoadUnitPrice(ILoadSlot, IDischargeSlot, int, int, int)} method.
	 * 
	 * @param sequences
	 */
	default void preparePurchaseForEvaluation(@NonNull ISequences sequences) {
	}

	/**
	 * Find the price (in $/M3) for loading at the given slot. Although every argument here except loadSlot and loadVolume can be found in the previous call to {@link #prepareEvaluation(ISequences)},
	 * the most relevant ones are reproduced here for convenience.
	 * 
	 * 
	 * @param loadSlot
	 * @param dischargeSlot
	 * @param loadTime
	 * @param dischargeTime
	 * @param dischargePricePerMMBTu
	 * @param loadVolumeInM3
	 * @param dischargeVolumeInM3
	 * 
	 * @return
	 */
	public int calculateFOBPricePerMMBTu(@NonNull ILoadSlot loadSlot, @NonNull IDischargeSlot dischargeSlot, int dischargePricePerMMBTu, @NonNull IAllocationAnnotation allocationAnnotation,
			@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @Nullable VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable IDetailTree annotations);

	/**
	 * Find the price in $/m3 for loading at the given slot and discharging at the given slot, when the cargo is a DES Purchase
	 * 
	 * 
	 * @param loadOption
	 * @param dischargeSlot
	 * @param transferTime
	 * @param transferVolumeInM3
	 *            The volume transfered between counter-parties
	 * @param annotations
	 *            Optional {@link IDetailTree} to store detailed calculation information e.g. during schedule export
	 * @return
	 */
	public int calculateDESPurchasePricePerMMBTu(@NonNull ILoadOption loadOption, @NonNull IDischargeSlot dischargeSlot, int dischargePricePerMMBTu,
			@NonNull IAllocationAnnotation allocationAnnotation, @Nullable VolumeAllocatedSequences volumeAllocatedSequences, @Nullable IDetailTree annotations);

	/**
	 * Find the price in $/m3 for loading at the given slot and discharging at the given slot, when a the cargo is a FOB Sale
	 * 
	 * @param loadOption
	 * @param dischargeOption
	 * @param transferTime
	 * @param transferVolumeInM3
	 *            The volume transfered between counter-parties
	 * @param annotations
	 *            Optional {@link IDetailTree} to store detailed calculation information e.g. during schedule export
	 * @return
	 */
	public int calculatePriceForFOBSalePerMMBTu(@NonNull ILoadSlot loadSlot, @NonNull IDischargeOption dischargeOption, int dischargePricePerMMBTu, @NonNull IAllocationAnnotation allocationAnnotation,
			@Nullable VolumeAllocatedSequences volumeAllocatedSequences, @Nullable IDetailTree annotations);

	/**
	 * Questions -> Volume allocator interaction? -> EntityValueCalcuator interaction -> Statefull/less?
	 * 
	 * TODO: Could break out into Cargo, FOB, DES API's TODO: Check array/list in API pass through for least amount of conversions (see base volume allocator & default enitiy value calculator)
	 * 
	 * @return Positive value for profit, negative value for loss (Normal scale factor) // TODO: Copy API for calulcateLoadPrice
	 */
	static final int IDX_OTHER_VALUE = 0;
	static final int IDX_SHIPPING_VALUE = 1;
	static final int IDX_UPSIDE_VALUE = 2;
	static final int IDX_UPSTREAM_VALUE = 3;
	static final int ADDITIONAL_PNL_COMPONENT_SIZE = 4;
	static final long @NonNull [] EMPTY_ADDITIONAL_PNL_RESULT = new long[ADDITIONAL_PNL_COMPONENT_SIZE];

	default long @NonNull [] calculateAdditionalProfitAndLoss(@NonNull ILoadOption loadOption, @NonNull IAllocationAnnotation allocationAnnotation, int @NonNull [] slotPricesPerMMBTu,
			@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @Nullable VolumeAllocatedSequences volumeAllocatedSequences,
			@Nullable IDetailTree annotations) {
		return EMPTY_ADDITIONAL_PNL_RESULT;
	}

	/**
	 * Invoked before P&L calculations are about to begin, but after {@link #preparePurchaseForEvaluation(ISequences)}. The calculate methods may have been invoked to obtain P&L estimates, now we want
	 * to clean any cached data prior to the real calculations.
	 */
	default void prepareRealPurchasePNL() {
	}

	/**
	 * Provides a set PricingEventType for a calculator
	 * 
	 * @param loadOption
	 *            TODO
	 * @param dischargeOption
	 *            TODO
	 */
	@Nullable
	default PricingEventType getCalculatorPricingEventType(@NonNull ILoadOption loadOption, @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		return null;
	}

	/**
	 * Get a rough estimate of the price at a given point in time Note that this is assumed to be in price curve time
	 * 
	 * @param loadOption
	 * @param dischargeOption
	 *            TODO
	 * @param timeInHours
	 * @return
	 */
	default int getEstimatedPurchasePrice(@NonNull ILoadOption loadOption, @NonNull IDischargeOption dischargeOption, int timeInHours) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	/**
	 * A contract may specify the pricing date of a purchase
	 * 
	 * @param loadOption
	 * @param dischargeOption
	 * @return
	 */
	default int getCalculatorPricingDate(@NonNull ILoadOption loadOption, @NonNull IPortTimeWindowsRecord portTimeWindowsRecord) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
//	/**
//	 * Provides an estimate of a shipping netback per MMBTu (divided through by purchase volume)
//	 * 
//	 * @param loadOption
//	 * @param dischargeOption
//	 * @return
//	 */
//	default long getEstimatedShippingNetbackPerMMBTu(@NonNull ILoadOption loadOption, @NonNull IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowsRecord, int startTime, long estimatedShippingCostPerMMBTu) {
//		return 0L;
//	}
//
//	/**
//	 * Provides an estimate of a profit share per MMBTu (divided through by purchase volume)
//	 * 
//	 * @param loadOption
//	 * @param dischargeOption
//	 * @return
//	 */
//	default long getEstimatedProfitShareContributionPerMMBTu(@NonNull ILoadOption loadOption, @NonNull IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowsRecord, int startTime, int purchasePricePerMMBTu, int salesPricePerMMBTu, long estimatedShippingCostPerMMBTu) {
//		return 0L;
//	}

}
