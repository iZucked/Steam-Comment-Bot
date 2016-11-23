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
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.cache.NotCaching;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator.EvaluationMode;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.VoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
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
	private VoyagePlanner voyagePlanner;

	@FunctionalInterface
	private interface Setter {
		void accept(ILoadOption load, IDischargeOption discharge, long price);
	}

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

		if (originalLoad != null || originalDischarge != null) {
			// Perform a binary search on sales price
			// First find a valid interval
			int minPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(0.0);// Integer.MAX_VALUE;
			int maxPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(40.0);// Integer.MAX_VALUE;

			boolean isPurchase = false;
			Setter priceSetter = null;
			if (originalLoad != null) {
				isPurchase = true;
				priceSetter = (l, d, p) -> {
					((IBreakEvenPriceCalculator) l.getLoadPriceCalculator()).setPrice((int) p);
				};
			} else if (originalDischarge != null) {
				priceSetter = (l, d, p) -> {
					((IBreakEvenPriceCalculator) d.getDischargePriceCalculator()).setPrice((int) p);
				};
			}
			assert priceSetter != null;

			long minPrice_Value = evaluateBreakEvenPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startingHeelInM3, portTimesRecord, sequenceElements, originalLoad,
					originalDischarge, minPricePerMMBTu, priceSetter);

			// TODO: We originally had a chunk of code to extend price range so that we could (almost) ensure PNL of zero was in range - unless values were really crazy. Disable now we search for
			// purchase price too.

			// if (isPurchase) {
			// while (minPrice_Value > 0) {
			// // Subtract $1
			// minPricePerMMBTu -= OptimiserUnitConvertor.convertToInternalPrice(1.0);
			// minPrice_Value = evaluateBreakEvenPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startingHeelInM3, portTimesRecord, sequenceElements, originalLoad,
			// originalDischarge, minPricePerMMBTu, priceSetter);
			// }
			// // Do not go below zero
			// minPricePerMMBTu = Math.max(0, minPricePerMMBTu);
			//
			// int maxPricePerMMBTu = 10 * minPricePerMMBTu;
			long maxPrice_Value = evaluateBreakEvenPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startingHeelInM3, portTimesRecord, sequenceElements, originalLoad,
					originalDischarge, maxPricePerMMBTu, priceSetter);
			// while (maxPrice_Value < 0) {
			// // Add $1
			// maxPricePerMMBTu += OptimiserUnitConvertor.convertToInternalPrice(1.0);
			// maxPrice_Value = evaluateBreakEvenPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startingHeelInM3, portTimesRecord, sequenceElements, originalLoad,
			// originalDischarge, maxPricePerMMBTu, priceSetter);
			// }
			// // $90/mmBTu is max - anything much larger can cause overflow issues
			// maxPricePerMMBTu = Math.min(OptimiserUnitConvertor.convertToInternalPrice(90.0), maxPricePerMMBTu);

			final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, minPrice_Value, maxPricePerMMBTu, maxPrice_Value, vesselAvailability, vesselStartTime, vesselCharterRatePerDay,
					startingHeelInM3, portTimesRecord, sequenceElements, originalLoad, originalDischarge, isPurchase, priceSetter);

			// final IDischargeOption beSlot;
			priceSetter.accept(originalLoad, originalDischarge, breakEvenPricePerMMBtu);
			// Redundant? Search should have found this....

			final VoyagePlan newVoyagePlan;
			if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
				newVoyagePlan = voyagePlanner.makeDESOrFOBVoyagePlan(vesselProvider.getResource(vesselAvailability), portTimesRecord);
			} else {
				newVoyagePlan = voyagePlanner.makeVoyage(vesselProvider.getResource(vesselAvailability), vesselCharterRatePerDay, portTimesRecord, startingHeelInM3);
			}
			assert newVoyagePlan != null;
			newVoyagePlan.setIgnoreEnd(vp.isIgnoreEnd());

			final IAllocationAnnotation newAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, newVoyagePlan, portTimesRecord);
			return new Pair<>(newVoyagePlan, newAllocation);
		}
		return null;
	}

	private long evaluateBreakEvenPrice(final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime, final long vesselCharterRatePerDay, final long startHeelInM3,
			final @NonNull IPortTimesRecord portTimesRecord, final @NonNull List<@NonNull ISequenceElement> sequenceElements,

			final @Nullable ILoadOption originalLoad, final @Nullable IDischargeOption originalDischarge, final int currentPricePerMMBTu, final Setter priceSetter) {

		// Overwrite current break even price with test price
		priceSetter.accept(originalLoad, originalDischarge, currentPricePerMMBTu);

		final VoyagePlan newVoyagePlan;
		if (vesselAvailability.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselAvailability.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			newVoyagePlan = voyagePlanner.makeDESOrFOBVoyagePlan(vesselProvider.getResource(vesselAvailability), portTimesRecord);
		} else {
			newVoyagePlan = voyagePlanner.makeVoyage(vesselProvider.getResource(vesselAvailability), vesselCharterRatePerDay, portTimesRecord, startHeelInM3);
		}
		assert newVoyagePlan != null;

		final IAllocationAnnotation newAllocation = volumeAllocator.get().allocate(vesselAvailability, vesselStartTime, newVoyagePlan, portTimesRecord);
		assert newAllocation != null;

		final Pair<@NonNull CargoValueAnnotation, @NonNull Long> cargoAnnotation = entityValueCalculator.get().evaluate(EvaluationMode.Estimate, newVoyagePlan, newAllocation, vesselAvailability,
				vesselStartTime, null, null);
		assert cargoAnnotation != null;

		for (final IPortSlot slot : cargoAnnotation.getFirst().getSlots()) {
			final int slotPricePerMMBTu = cargoAnnotation.getFirst().getSlotPricePerMMBTu(slot);
			if (slotPricePerMMBTu < 0) {
				return Long.MAX_VALUE;
			}
		}

		final long newPnLValue = cargoAnnotation.getSecond();
		return newPnLValue;
	}

	private int search(final int min, final long minValue, final int max, final long maxValue, final @NonNull IVesselAvailability vesselAvailability, final int vesselStartTime,
			final long vesselCharterRatePerDay, final long startHeelInM3, final @NonNull IPortTimesRecord portTimesRecord, final @NonNull List<@NonNull ISequenceElement> sequenceElements,
			final ILoadOption originalLoad, final IDischargeOption originalDischarge, boolean isPurchase, final Setter priceSetter) {

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

		long midValue = evaluateBreakEvenPrice(vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startHeelInM3, portTimesRecord, sequenceElements, originalLoad, originalDischarge, mid,
				priceSetter);
		assert midValue != Long.MAX_VALUE;

		if (isPurchase) {
			midValue = -midValue;
		}

		if (midValue > 0) {
			return search(min, minValue, mid, midValue, vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startHeelInM3, portTimesRecord, sequenceElements, originalLoad, originalDischarge,
					isPurchase, priceSetter);
		} else {
			return search(mid, midValue, max, maxValue, vesselAvailability, vesselStartTime, vesselCharterRatePerDay, startHeelInM3, portTimesRecord, sequenceElements, originalLoad, originalDischarge,
					isPurchase, priceSetter);
		}
	}
}
