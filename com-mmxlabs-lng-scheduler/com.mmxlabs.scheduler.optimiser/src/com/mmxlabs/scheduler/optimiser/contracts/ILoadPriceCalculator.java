/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
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
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
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
	public void prepareEvaluation(@NonNull ISequences sequences);

	/**
	 * Find the price (in $/M3) for loading at the given slot. Although every argument here except loadSlot and loadVolume can be found in the previous call to
	 * {@link #prepareEvaluation(ScheduledSequences)}, the most relevant ones are reproduced here for convenience.
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
			@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @Nullable IDetailTree annotations);

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
			@NonNull IAllocationAnnotation allocationAnnotation, @Nullable IDetailTree annotations);

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
			@Nullable IDetailTree annotations);

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
	static final int ADDITIONAL_PNL_COMPONENT_SIZE = 3;
	static final long[] EMPTY_ADDITIONAL_PNL_RESULT = new long[ADDITIONAL_PNL_COMPONENT_SIZE];

	public long[] calculateAdditionalProfitAndLoss(@NonNull ILoadOption loadOption, @NonNull IAllocationAnnotation allocationAnnotation, @NonNull int[] slotPricesPerMMBTu,
			@NonNull IVesselAvailability vesselAvailability, int vesselStartTime, @NonNull VoyagePlan plan, @Nullable IDetailTree annotations);

	/**
	 * Invoked before P&L calculations are about to begin, but after {@link #prepareEvaluation(ISequences)}. The calculate methods may have been invoked to obtain P&L estimates, now we want to clean
	 * any cached data prior to the real calculations.
	 */
	public void prepareRealPNL();
	
	/**
	 * Provides a set PricingEventType for a calculator
	 */
	PricingEventType getCalculatorPricingEventType();
	
	/**
	 * Get a rough estimate of the price at a given point in time
	 * Note that this is assumed to be in price curve time
	 * @param loadOption
	 * @param timeInHours
	 * @return
	 */
	int getEstimatedPurchasePrice(ILoadOption loadOption, int timeInHours);
}
