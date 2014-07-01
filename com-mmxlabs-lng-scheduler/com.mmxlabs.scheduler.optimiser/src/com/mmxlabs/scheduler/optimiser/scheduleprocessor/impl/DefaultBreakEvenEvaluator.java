/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import com.mmxlabs.common.Pair;
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
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.ILNGVoyageCalculator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyageDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

/**
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

	@Inject
	private ShippingCostHelper shippingCostHelper;

	@Inject
	private IActualsDataProvider actualsDataProvider;

	@Override
	public Pair<VoyagePlan, IAllocationAnnotation> processSchedule(int vesselStartTime, final IVessel vessel, final VoyagePlan vp, final IPortTimesRecord portTimesRecord) {
		long startingHeelInM3 = vp.getStartingHeelInM3();

		boolean isCargoPlan = false;
		boolean missingPurchasePrice = false;
		boolean missingSalesPrice = false;

		// Grab the current list of arrival times and update the rolling currentTime
		// 5 as we know that is the max we need (currently - a single cargo)
		// final List<Integer> arrivalTimes = new ArrayList<Integer>();
		final IDetailsSequenceElement[] currentSequence = vp.getSequence();
		List<ISequenceElement> sequenceElements = new LinkedList<>();

		ILoadOption originalLoad = null;
		IDischargeOption originalDischarge = null;
		// Note: We do not handle multiple loads correctly!
		int cvValue = 0;

		for (int idx = 0; idx < currentSequence.length; ++idx) {

			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails) {
				final PortDetails details = (PortDetails) obj;
				// arrivalTimes.add(currentTime);
				if (idx != (currentSequence.length - 1)) {
					// currentTime += details.getOptions().getVisitDuration();
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
						}
					}
				}
				sequenceElements.add(portSlotProvider.getElement(details.getOptions().getPortSlot()));
			} else if (obj instanceof VoyageDetails) {
				final VoyageDetails details = (VoyageDetails) obj;
				// currentTime += details.getOptions().getAvailableTime();
			}
		}

		if (!isCargoPlan || (!missingPurchasePrice && !missingSalesPrice)) {
			// No missing prices
			return null;
		}
		if (missingPurchasePrice && missingSalesPrice) {
			assert false; // Should not get here
			// Both prices missing - no supported
			throw new IllegalStateException("Unable to breakeven with more than one missing price");
		}

		final IDetailsSequenceElement[] newSequence = currentSequence.clone();
		final IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vesselStartTime, vp, portTimesRecord);

		if (originalLoad != null) {

			// Get the new cargo allocation.

			// Purchase price in mmbtu = (sales revenue - shipping cost) / load volume in mmbtu
			final long totalShippingCost = shippingCostHelper.getShippingCosts(vp, vessel, false, true);
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
								final long dischargeVolumeInMMBTu = currentAllocation.getSlotVolumeInMMBTu(portSlot);

								IDischargeOption dischargeOption = ((IDischargeOption) portSlot);
								final int dischargePricePerMMBTu = dischargeOption.getDischargePriceCalculator().estimateSalesUnitPrice(dischargeOption, portTimesRecord, null);
								totalSalesRevenue += Calculator.costFromConsumption(dischargeVolumeInMMBTu, dischargePricePerMMBTu);
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
				// TODO: Extract out further for custom base fuel pricing logic?
				// Use forecast BF, but check for actuals later
				final int baseFuelUnitPricePerMT;
				if (actualsDataProvider.hasActuals(originalLoad)) {
					baseFuelUnitPricePerMT = actualsDataProvider.getBaseFuelPricePerMT(originalLoad);
				} else {
					baseFuelUnitPricePerMT = vessel.getVesselClass().getBaseFuelUnitPrice();
				}

				voyageCalculator.calculateVoyagePlan(vp, vessel, startingHeelInM3, baseFuelUnitPricePerMT, portTimesRecord, newSequence);
			}
			final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, vesselStartTime, vp, portTimesRecord);
			return new Pair<>(vp, newAllocation);
		} else if (originalDischarge != null) {

			// Perform a binary search on sales price
			// First find a valid interval
			int minPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(5.0);// Integer.MAX_VALUE;
			// for (int idx = 0; idx < currentSequence.length; ++idx) {
			//
			// final Object obj = currentSequence[idx];
			// if (obj instanceof PortDetails) {
			// final PortDetails details = (PortDetails) obj;
			// if (idx != (currentSequence.length - 1)) {
			//
			// if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
			// ILoadOption loadOption = (ILoadOption) details.getOptions().getPortSlot();
			// // TODO: Average?
			// cvValue = loadOption.getCargoCVValue();
			// // TODO: Average?
			// int p = currentAllocation.getSlotPricePerMMBTu(loadOption);
			// if (p < minPricePerMMBTu) {
			// minPricePerMMBTu = p;
			// }
			// }
			// }
			// }
			// }
			long minPrice_Value = evaluateSalesPrice(vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge, minPricePerMMBTu);
			while (minPrice_Value > 0) {
				// Subtract $1
				minPricePerMMBTu -= OptimiserUnitConvertor.convertToInternalPrice(1.0);
				minPrice_Value = evaluateSalesPrice(vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge, minPricePerMMBTu);
			}
			// Do not go below zero
			minPricePerMMBTu = Math.max(0, minPricePerMMBTu);

			int maxPricePerMMBTu = 10 * minPricePerMMBTu;
			long maxPrice_Value = evaluateSalesPrice(vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge, maxPricePerMMBTu);
			while (maxPrice_Value < 0) {
				// Add $1
				maxPricePerMMBTu += OptimiserUnitConvertor.convertToInternalPrice(1.0);
				maxPrice_Value = evaluateSalesPrice(vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge, maxPricePerMMBTu);
			}
			// $90/mmBTu is max - anything much larger can cause overflow issues
			maxPricePerMMBTu = Math.min(OptimiserUnitConvertor.convertToInternalPrice(90.0), maxPricePerMMBTu);

			final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, minPrice_Value, maxPricePerMMBTu, maxPrice_Value, vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge);

			// final IDischargeOption beSlot;
			((IBreakEvenPriceCalculator) originalDischarge.getDischargePriceCalculator()).setPrice(breakEvenPricePerMMBtu);
			// Redundant? Search should have found this....
			final VoyagePlan newVoyagePlan = voyagePlanner.makeVoyage(vesselProvider.getResource(vessel), sequenceElements, vesselStartTime, portTimesRecord, 0);
			final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, vesselStartTime, newVoyagePlan, portTimesRecord);
			return new Pair<>(newVoyagePlan, newAllocation);
			// seq.getVoyagePlans().set(vpIdx, newVoyagePlan);
		}
		// }
		// }
		return null;
	}

	private long evaluateSalesPrice(final IVessel vessel, int vesselStartTime, final IPortTimesRecord portTimesRecord, List<ISequenceElement> sequenceElements,
			final IDischargeOption originalDischarge, final int currentPricePerMMBTu) {

		// Overwrite current break even price with test price
		((IBreakEvenPriceCalculator) originalDischarge.getDischargePriceCalculator()).setPrice(currentPricePerMMBTu);

		final VoyagePlan newVoyagePlan = voyagePlanner.makeVoyage(vesselProvider.getResource(vessel), sequenceElements, vesselStartTime, portTimesRecord, 0);

		final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, vesselStartTime, newVoyagePlan, portTimesRecord);
		final long newPnLValue = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, vesselStartTime, null);
		return newPnLValue;
	}

	private int search(final int min, final long minValue, final int max, final long maxValue, final IVessel vessel, int vesselStartTime, final IPortTimesRecord portTimesRecord,
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

		final long midValue = evaluateSalesPrice(vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge, mid);

		if (midValue > 0) {
			return search(min, minValue, mid, midValue, vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge);
		} else {
			return search(mid, midValue, max, maxValue, vessel, vesselStartTime, portTimesRecord, sequenceElements, originalDischarge);
		}

	}
}
