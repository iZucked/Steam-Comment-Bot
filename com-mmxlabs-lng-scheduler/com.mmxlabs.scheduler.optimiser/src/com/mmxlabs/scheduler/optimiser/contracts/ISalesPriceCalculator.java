/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.contracts;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.detailtree.IDetailTree;
import com.mmxlabs.optimiser.core.ISequences;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanOptimiser;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;

/**
 * An interface for sales contract price calculations. This calculator only have access to sequence and time information, because it will be used to calculate shipping costs.
 * 
 * @author hinton
 * 
 */
public interface ISalesPriceCalculator extends ICalculator {
	/**
	 * This method will be called once before any of the slots in the argument are evaluated using {@link #estimateSalesUnitPrice(IDischargeOption, int, IDetailTree)}, to allow for shared
	 * pre-computation.
	 * 
	 * @param sequences
	 */
	default void prepareSalesForEvaluation(@NonNull ISequences sequences) {
	}

	/**
	 * Find the unit price in dollars per mmbtu for gas at the given slot, at the given time - volume independent. This method should always return a value as it will be used by the
	 * {@link IVoyagePlanOptimiser} and {@link ILNGVoyageCalculator} to value LNG. Final sales price will be calculated using {@link #calculateSalesUnitPrice(IDischargeOption, int, long, IDetailTree)}
	 * once volume decisions have been made.
	 * 
	 * @param time
	 * @param annotations
	 *            TODO
	 * @param slot
	 */
	public int estimateSalesUnitPrice(IDischargeOption option, IPortTimesRecord voyageRecord, @Nullable IDetailTree annotations);

	/**
	 * Another variant of {@link #estimateSalesUnitPrice(IDischargeOption, int, IDetailTree)} taking a discharge volume to calculate the exact price. This method should not be called before volume
	 * decisions have been made.
	 * 
	 * @param time
	 * @param annotations
	 *            TODO
	 * @param slot
	 */
	public int calculateSalesUnitPrice(IDischargeOption option, IAllocationAnnotation allocationAnnotation, @Nullable IDetailTree annotations);

	/**
	 * Invoked before final P&L calculations are about to begin, but after {@link #prepareEvaluation(ISequences)}. The calculate methods may have been invoked to obtain P&L estimates, now we want to
	 * clean any cached data prior to the real calculations.
	 */
	default void prepareRealSalesPNL() {
	}

	public PricingEventType getCalculatorPricingEventType(IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowsRecord);

	/**
	 * Get the estimated sales price in dollars per mmbtu at a given point in time in hours
	 * 
	 * @param loadOption
	 * @param dischargeOption
	 * @param timeInHours
	 * @return
	 */
	public int getEstimatedSalesPrice(ILoadOption loadOption, IDischargeOption dischargeOption, int timeInHours);

	/**
	 * A contract may specify the pricing date of a purchase
	 * 
	 * @param dischargeOption
	 * @param portTimeWindowsRecord
	 * @return
	 */
	public int getCalculatorPricingDate(IDischargeOption dischargeOption, IPortTimeWindowsRecord portTimeWindowsRecord);
}
