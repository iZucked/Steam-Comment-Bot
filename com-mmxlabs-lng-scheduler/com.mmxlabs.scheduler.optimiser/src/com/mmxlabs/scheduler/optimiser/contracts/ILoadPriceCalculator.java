/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import java.util.List;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
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
	 * Note the {@link ScheduledSequences} object will be in an incomplete state at this point in time.
	 * 
	 * @param sequences
	 * @param scheduledSequences
	 */
	public void prepareEvaluation(ISequences sequences, ScheduledSequences scheduledSequences);

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
	 * @since 7.1
	 */
	public int calculateFOBPricePerMMBTu(ILoadSlot loadSlot, IDischargeSlot dischargeSlot, int loadTime, int dischargeTime, int dischargePricePerMMBTu, long loadVolumeInM3, long dischargeVolumeInM3,
			IVessel vessel, VoyagePlan plan, IDetailTree annotations);

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
	 * @since 2.0
	 */
	public int calculateDESPurchasePricePerMMBTu(ILoadOption loadOption, final IDischargeSlot dischargeSlot, final int transferTime, final int dischargePricePerMMBTu, long transferVolumeInM3,
			IDetailTree annotations);

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
	 * @since 2.0
	 */
	public int calculatePriceForFOBSalePerMMBTu(ILoadSlot loadSlot, final IDischargeOption dischargeOption, final int transferTime, final int dischargePricePerMMBTu, long transferVolumeInM3,
			IDetailTree annotations);

	/**
	 * Questions -> Volume allocator interaction? -> EntityValueCalcuator interaction -> Statefull/less?
	 * 
	 * TODO: Could break out into Cargo, FOB, DES API's TODO: Check array/list in API pass through for least amount of conversions (see base volume allocator & default enitiy value calculator)
	 * 
	 * @return Positive value for profit, negative value for loss (Normal scale factor) // TODO: Copy API for calulcateLoadPrice
	 */
	public long calculateAdditionalProfitAndLoss(ILoadOption loadOption, List<IPortSlot> slots, int[] arrivalTimes, long[] volumes, int[] dischargePricesPerMMBTu, IVessel vessel, VoyagePlan plan,
			IDetailTree annotations);
}
