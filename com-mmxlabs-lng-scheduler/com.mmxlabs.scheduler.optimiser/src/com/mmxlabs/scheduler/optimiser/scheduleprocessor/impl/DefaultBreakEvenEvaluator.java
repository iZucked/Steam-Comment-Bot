/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
 * @since 2.0
 */
public class DefaultBreakEvenEvaluator implements IBreakEvenEvaluator {

	@Inject
	private IVolumeAllocator cargoAllocator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private VoyagePlanner voyagePlanner;

	@Override
	public void processSchedule(final ScheduledSequences scheduledSequences) {
		// Charter Out Optimisation... Detect potential charter out opportunities.
		for (final ScheduledSequence seq : scheduledSequences) {

			final IVessel vessel = vesselProvider.getVessel(seq.getResource());
			if (vessel == null) {
				// Error?
				continue;
			}

			int currentTime = seq.getStartTime();

			for (int vpIdx = 0; vpIdx < seq.getVoyagePlans().size(); ++vpIdx) {
				// for (final VoyagePlan vp : seq.getVoyagePlans()) {
				VoyagePlan vp = seq.getVoyagePlans().get(vpIdx);

				boolean isCargoPlan = false;
				boolean missingPurchasePrice = false;
				boolean missingSalesPrice = false;

				// Grab the current list of arrival times and update the rolling currentTime
				// 5 as we know that is the max we need (currently - a single cargo)
				final List<Integer> arrivalTimes = new ArrayList<Integer>();
				int dischargeIdx = -1;
				final Object[] currentSequence = vp.getSequence();
				List<ISequenceElement> sequenceElements = new LinkedList<>();

				ILoadOption originalLoad = null;
				IDischargeOption originalDischarge = null;
				// Note: We do not handle multiple loads correctly!
				int cvValue = 0;

				for (int idx = 0; idx < currentSequence.length; ++idx) {

					final Object obj = currentSequence[idx];
					if (obj instanceof PortDetails) {
						final PortDetails details = (PortDetails) obj;
						arrivalTimes.add(currentTime);
						if (idx != (currentSequence.length - 1)) {
							currentTime += details.getOptions().getVisitDuration();
							if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
								isCargoPlan = true;
								ILoadOption loadOption = (ILoadOption) details.getOptions().getPortSlot();
								// TODO: Average?
								cvValue = loadOption.getCargoCVValue();
								if (loadOption.getLoadPriceCalculator() instanceof IBreakEvenPriceCalculator) {
									if (missingPurchasePrice || missingSalesPrice) {
										// Already one missing price!
										throw new IllegalStateException("Unable to breakeven with more than one missing price");
									}
									missingPurchasePrice = true;
									originalLoad = loadOption;
								}
							} else if (details.getOptions().getPortSlot().getPortType() == PortType.Discharge) {
								IDischargeOption dischargeOption = (IDischargeOption) details.getOptions().getPortSlot();
								if (dischargeOption.getDischargePriceCalculator() instanceof IBreakEvenPriceCalculator) {
									if (missingPurchasePrice || missingSalesPrice) {
										// Already one missing price!
										throw new IllegalStateException("Unable to breakeven with more than one missing price");
									}
									missingSalesPrice = true;
									originalDischarge = dischargeOption;
									dischargeIdx = idx;
								}
							}
						}
						sequenceElements.add(portSlotProvider.getElement(details.getOptions().getPortSlot()));
					} else if (obj instanceof VoyageDetails) {
						final VoyageDetails details = (VoyageDetails) obj;
						currentTime += details.getOptions().getAvailableTime();
					}
				}

				if (!isCargoPlan || (!missingPurchasePrice && !missingSalesPrice)) {
					// No missing prices
					continue;
				}
				if (missingPurchasePrice && missingSalesPrice) {
					assert false; // Should not get here
					// Both prices missing - no supported
					throw new IllegalStateException("Unable to breakeven with more than one missing price");
				}

				final Object[] newSequence = currentSequence.clone();
				final IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vp, arrivalTimes);

				if (originalLoad != null) {

					// Get the new cargo allocation.

					// Purchase price in mmbtu = (sales revenue - shipping cost) / load volume in mmbtu
					final long totalShippingCost = entityValueCalculator.getShippingCosts(vp, vessel, false, true, seq.getStartTime(), null);
					long totalSalesRevenue = 0;
					long loadVolumeInM3 = 0;

					{
						for (int idx = 0; idx < currentSequence.length; ++idx) {

							final Object obj = currentSequence[idx];
							if (obj instanceof PortDetails) {
								final PortDetails details = (PortDetails) obj;
								if (idx != (currentSequence.length - 1)) {

									if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
										IPortSlot portSlot = details.getOptions().getPortSlot();
										loadVolumeInM3 += currentAllocation.getSlotVolumeInM3(portSlot);
										// TODO: Average?
									} else if (details.getOptions().getPortSlot().getPortType() == PortType.Discharge) {
										final IPortSlot portSlot = details.getOptions().getPortSlot();
										final long dischargeVolumeInM3 = currentAllocation.getSlotVolumeInM3(portSlot);
										final int dischargePricePerM3 = currentAllocation.getSlotPricePerM3(portSlot);
										totalSalesRevenue += Calculator.convertM3ToM3Price(dischargeVolumeInM3, dischargePricePerM3);
									}
								}
							}
						}
					}

					final long breakEvenPurchaseCost = totalSalesRevenue - totalShippingCost;

					final int breakEvenPurchasePricePerM3 = Calculator.getPerM3FromTotalAndVolumeInM3(breakEvenPurchaseCost, loadVolumeInM3);
					final long breakEvenPurchasePricePerMMBTu = Calculator.costPerMMBTuFromM3(breakEvenPurchasePricePerM3, cvValue);

					((IBreakEvenPriceCalculator) originalLoad.getLoadPriceCalculator()).setPrice((int) breakEvenPurchasePricePerMMBTu);

					// Overwrite existing data
					if (vessel.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vessel.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
						vp.setSequence(newSequence);
					} else {
						voyageCalculator.calculateVoyagePlan(vp, vessel, arrivalTimes, newSequence);
					}
				} else if (originalDischarge != null) {

					// Perform a binary search on sales price
					// First find a valid interval
					int minPricePerMMBTu = Integer.MAX_VALUE;
					for (int idx = 0; idx < currentSequence.length; ++idx) {

						final Object obj = currentSequence[idx];
						if (obj instanceof PortDetails) {
							final PortDetails details = (PortDetails) obj;
							if (idx != (currentSequence.length - 1)) {

								if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
									ILoadOption loadOption = (ILoadOption) details.getOptions().getPortSlot();
									// TODO: Average?
									cvValue = loadOption.getCargoCVValue();
									// TODO: Average?
									int p = Calculator.costPerMMBTuFromM3(currentAllocation.getSlotPricePerM3(loadOption), loadOption.getCargoCVValue());
									if (p < minPricePerMMBTu) {
										minPricePerMMBTu = p;
									}
								}
							}
						}
					}
					long minPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, sequenceElements, originalDischarge, minPricePerMMBTu);
					while (minPrice_Value > 0) {
						// Subtract $5
						minPricePerMMBTu -= 5000;
						minPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, sequenceElements, originalDischarge, minPricePerMMBTu);
					}
					// Do not go below zero
					minPricePerMMBTu = Math.max(0, minPricePerMMBTu);

					int maxPricePerMMBTu = 10 * minPricePerMMBTu;
					long maxPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, sequenceElements, originalDischarge, maxPricePerMMBTu);
					while (maxPrice_Value < 0) {
						// Add $5
						maxPricePerMMBTu += 5000;
						maxPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, sequenceElements, originalDischarge, maxPricePerMMBTu);
					}
					// $90/mmBTu is max - anything much larger can cause overflow issues
					maxPricePerMMBTu = Math.min(OptimiserUnitConvertor.convertToInternalPrice(90.0), maxPricePerMMBTu);

					final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, minPrice_Value, maxPricePerMMBTu, maxPrice_Value, seq, vessel, arrivalTimes, sequenceElements, originalDischarge);

					// final IDischargeOption beSlot;
					((IBreakEvenPriceCalculator) originalDischarge.getDischargePriceCalculator()).setPrice(breakEvenPricePerMMBtu);
					// Redundant? Search should have found this....
					final VoyagePlan newVoyagePlan = voyagePlanner.makeVoyage(seq.getResource(), sequenceElements, seq.getStartTime(), arrivalTimes);
					seq.getVoyagePlans().set(vpIdx, newVoyagePlan);
				}
			}
		}
	}

	private long evaluateSalesPrice(final ScheduledSequence seq, final IVessel vessel, final List<Integer> arrivalTimes, List<ISequenceElement> sequenceElements,
			final IDischargeOption originalDischarge, final int currentPricePerMMBTu) {

		// Overwrite current break even price with test price
		((IBreakEvenPriceCalculator) originalDischarge.getDischargePriceCalculator()).setPrice(currentPricePerMMBTu);

		final VoyagePlan newVoyagePlan = voyagePlanner.makeVoyage(seq.getResource(), sequenceElements, seq.getStartTime(), arrivalTimes);

		final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, newVoyagePlan, arrivalTimes);
		final long newPnLValue = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, seq.getStartTime(), null);
		return newPnLValue;
	}

	private int search(final int min, final long minValue, final int max, final long maxValue, final ScheduledSequence seq, final IVessel vessel, final List<Integer> arrivalTimes,
			final List<ISequenceElement> sequenceElements, final IDischargeOption originalDischarge) {

		final int mid = min + ((max - min) / 2);

		// TODO: Check mid == min || mid == max) - terminate condition.
		if (mid == min || mid == max) {
			// Zero somewhere in this interval, pick closest value.
			if (Math.abs(minValue) < Math.abs(maxValue)) {
				// Pick min value
				return min;
			} else {
				// pick max value
				return max;
			}
		}

		final long midValue = evaluateSalesPrice(seq, vessel, arrivalTimes, sequenceElements, originalDischarge, mid);

		if (midValue > 0) {
			return search(min, minValue, mid, midValue, seq, vessel, arrivalTimes, sequenceElements, originalDischarge);
		} else {
			return search(mid, midValue, max, maxValue, seq, vessel, arrivalTimes, sequenceElements, originalDischarge);
		}

	}
}
