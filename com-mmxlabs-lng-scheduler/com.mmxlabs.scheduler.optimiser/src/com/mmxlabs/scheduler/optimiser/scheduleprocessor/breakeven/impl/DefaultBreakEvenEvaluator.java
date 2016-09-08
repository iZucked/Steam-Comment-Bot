/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IVesselBaseFuelCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator.EvaluationMode;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IActualsDataProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.schedule.ShippingCostHelper;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
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
	@NotCaching
	private Provider<IVolumeAllocator> volumeAllocator;

	@Inject
	private Provider<IEntityValueCalculator> entityValueCalculator;

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

	@Inject
	private IVesselBaseFuelCalculator vesselBaseFuelCalculator;

	@Override
	public Pair<VoyagePlan, IAllocationAnnotation> processSchedule(final int vesselStartTime, final @NonNull IVesselAvailability vesselAvailability, final @NonNull VoyagePlan vp,
			final @NonNull IPortTimesRecord portTimesRecord) {
		final long startingHeelInM3 = vp.getStartingHeelInM3();
		final long vesselCharterRatePerDay = vp.getCharterInRatePerDay();

		boolean isCargoPlan = false;
		boolean missingPurchasePrice = false;
		boolean missingSalesPrice = false;

		// Grab the current list of arrival times and update the rolling currentTime
		// 5 as we know that is the max we need (currently - a single cargo)
		// final List<Integer> arrivalTimes = new ArrayList<Integer>();
		final IDetailsSequenceElement[] currentSequence = vp.getSequence();
		final List<ISequenceElement> sequenceElements = new LinkedList<>();

		ILoadOption originalLoad = null;
		IDischargeOption originalDischarge = null;
		// Note: We do not handle multiple loads correctly!
		int cvValue = 0;

		for (int idx = 0; idx < currentSequence.length; ++idx) {

			final int offset = vp.isIgnoreEnd() ? 1 : 0;
			final Object obj = currentSequence[idx];
			if (obj instanceof PortDetails) {
				final PortDetails details = (PortDetails) obj;
				// arrivalTimes.add(currentTime);
				if (idx != (currentSequence.length - offset)) {
					// currentTime += details.getOptions().getVisitDuration();
					if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
						isCargoPlan = true;
						final ILoadOption loadOption = (ILoadOption) details.getOptions().getPortSlot();
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
						final IDischargeOption dischargeOption = (IDischargeOption) details.getOptions().getPortSlot();
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
		final IAllocationAnnotation currentAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, vp, portTimesRecord);

		if (originalLoad != null) {

			// Get the new cargo allocation.

			// Purchase price in mmbtu = (sales revenue - shipping cost) / load volume in mmbtu
			final long totalShippingCost = shippingCostHelper.getShippingCosts(vp, vesselAvailability, false, true);
			long totalSalesRevenue = 0;
			long loadVolumeInM3 = 0;

			{
				final int offset = vp.isIgnoreEnd() ? 1 : 0;
				for (int idx = 0; idx < currentSequence.length; ++idx) {

					final Object obj = currentSequence[idx];
					if (obj instanceof PortDetails) {
						final PortDetails details = (PortDetails) obj;
						if (idx != (currentSequence.length - offset)) {

							if (details.getOptions().getPortSlot().getPortType() == PortType.Load) {
								final IPortSlot portSlot = details.getOptions().getPortSlot();
								loadVolumeInM3 += currentAllocation.getSlotVolumeInM3(portSlot);
								// TODO: Average?
							} else if (details.getOptions().getPortSlot().getPortType() == PortType.Discharge) {
								final IPortSlot portSlot = details.getOptions().getPortSlot();
								final long dischargeVolumeInMMBTu = currentAllocation.getSlotVolumeInMMBTu(portSlot);

								final IDischargeOption dischargeOption = ((IDischargeOption) portSlot);
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
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				vp.setSequence(newSequence);
			} else {
				// TODO: Extract out further for custom base fuel pricing logic?
				// Use forecast BF, but check for actuals later
				final int baseFuelUnitPricePerMT;
				if (actualsDataProvider.hasActuals(originalLoad)) {
					baseFuelUnitPricePerMT = actualsDataProvider.getBaseFuelPricePerMT(originalLoad);
				} else {
					baseFuelUnitPricePerMT = vesselBaseFuelCalculator.getBaseFuelPrice(vesselAvailability.getVessel(), portTimesRecord);
				}

				voyageCalculator.calculateVoyagePlan(vp, vesselAvailability.getVessel(), startingHeelInM3, baseFuelUnitPricePerMT, portTimesRecord, newSequence);
			}
			final IAllocationAnnotation newAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, vp, portTimesRecord);
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
			long minPrice_Value = evaluateSalesPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge, minPricePerMMBTu);
			while (minPrice_Value > 0) {
				// Subtract $1
				int l_minPricePerMMBTu = minPricePerMMBTu - OptimiserUnitConvertor.convertToInternalPrice(0.1);
				long l_minPrice_Value = evaluateSalesPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge, l_minPricePerMMBTu);
				if (l_minPrice_Value == Long.MAX_VALUE) {
					break;
				}
				minPricePerMMBTu = l_minPricePerMMBTu;
				minPrice_Value = l_minPrice_Value;
			}
			// Do not go below zero
			minPricePerMMBTu = Math.max(0, minPricePerMMBTu);

			int maxPricePerMMBTu = 10 * Math.max(OptimiserUnitConvertor.convertToInternalPrice(5.0), minPricePerMMBTu);
			long maxPrice_Value = evaluateSalesPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge, maxPricePerMMBTu);
			while (maxPrice_Value < 0) {
				// Add $1
				int l_maxPricePerMMBTu = maxPricePerMMBTu + OptimiserUnitConvertor.convertToInternalPrice(1.0);
				long l_maxPrice_Value = evaluateSalesPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge, l_maxPricePerMMBTu);
				if (l_maxPrice_Value != Long.MAX_VALUE) {
					maxPrice_Value = l_maxPrice_Value;
					maxPricePerMMBTu = l_maxPricePerMMBTu;
				}

			}
			// $90/mmBTu is max - anything much larger can cause overflow issues
			maxPricePerMMBTu = Math.min(OptimiserUnitConvertor.convertToInternalPrice(90.0), maxPricePerMMBTu);

			final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, minPrice_Value, maxPricePerMMBTu, maxPrice_Value, vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord,
					sequenceElements, originalDischarge);

			// final IDischargeOption beSlot;
			((IBreakEvenPriceCalculator) originalDischarge.getDischargePriceCalculator()).setPrice(breakEvenPricePerMMBtu);
			// Redundant? Search should have found this....

			final VoyagePlan newVoyagePlan;
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				newVoyagePlan = voyagePlanner.makeDESOrFOBVoyagePlan(vesselProvider.getResource(vesselAvailability), portTimesRecord);
			} else {
				newVoyagePlan = voyagePlanner.makeVoyage(vesselProvider.getResource(vesselAvailability), vesselCharterRatePerDay, portTimesRecord, 0);
			}
			assert newVoyagePlan != null;
			newVoyagePlan.setIgnoreEnd(vp.isIgnoreEnd());

			final IAllocationAnnotation newAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, newVoyagePlan, portTimesRecord);
			return new Pair<>(newVoyagePlan, newAllocation);
		}
		return null;
	}

	private long evaluateSalesPrice(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime, final long vesselCharterRatePerDay,
			final @NonNull IPortTimesRecord portTimesRecord, final @NonNull List<@NonNull ISequenceElement> sequenceElements, final @NonNull IDischargeOption originalDischarge,
			final int currentPricePerMMBTu) {

		// Overwrite current break even price with test price
		((IBreakEvenPriceCalculator) originalDischarge.getDischargePriceCalculator()).setPrice(currentPricePerMMBTu);

		final VoyagePlan newVoyagePlan;
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			newVoyagePlan = voyagePlanner.makeDESOrFOBVoyagePlan(vesselProvider.getResource(vesselAvailability), portTimesRecord);
		} else {
			newVoyagePlan = voyagePlanner.makeVoyage(vesselProvider.getResource(vesselAvailability), vesselCharterRatePerDay, portTimesRecord, 0);
		}
		assert newVoyagePlan != null;

		final IAllocationAnnotation newAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, newVoyagePlan, portTimesRecord);
		assert newAllocation != null;

		final Pair<@NonNull CargoValueAnnotation, @NonNull Long> cargoAnnotation = entityValueCalculator.get().evaluate(EvaluationMode.Estimate, newVoyagePlan, newAllocation, vesselAvailability,
				vesselStartTime, null, null);
		assert cargoAnnotation != null;

		int slotPricePerMMBTu = cargoAnnotation.getFirst().getSlotPricePerMMBTu(cargoAnnotation.getFirst().getFirstSlot());
		if (slotPricePerMMBTu < 0) {
			return Long.MAX_VALUE;
		}

		final long newPnLValue = cargoAnnotation.getSecond();
		return newPnLValue;
	}

	private int search(final int min, final long minValue, final int max, final long maxValue, final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime,
			final long vesselCharterRatePerDay, final @NonNull IPortTimesRecord portTimesRecord, final @NonNull List<@NonNull ISequenceElement> sequenceElements,
			final IDischargeOption originalDischarge) {

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

		final long midValue = evaluateSalesPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge, mid);
		assert midValue != Long.MAX_VALUE;
		if (midValue > 0) {
			return search(min, minValue, mid, midValue, vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge);
		} else {
			return search(mid, midValue, max, maxValue, vesselAvailability, vesselStartTime, vesselCharterRatePerDay, portTimesRecord, sequenceElements, originalDischarge);
		}

	}
}
