/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.optimiser.core.IAnnotatedSolution;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.ISequenceElement;
import com.mmxlabs.optimiser.core.ISequencesAttributesProvider;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVesselCharter;
import com.mmxlabs.scheduler.optimiser.components.VesselInstanceType;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.ICharterCostCalculator;
import com.mmxlabs.scheduler.optimiser.entities.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IVolumeAllocator;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.CargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.impl.ICargoValueAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.impl.IVoyagePlanner;
import com.mmxlabs.scheduler.optimiser.providers.IPortSlotProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.providers.PortType;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimesRecord;
import com.mmxlabs.scheduler.optimiser.voyage.impl.IDetailsSequenceElement;
import com.mmxlabs.scheduler.optimiser.voyage.impl.PortDetails;
import com.mmxlabs.scheduler.optimiser.voyage.impl.VoyagePlan;

@NonNullByDefault
public class DefaultBreakEvenEvaluator implements IBreakEvenEvaluator {

	@Inject
	private IVolumeAllocator volumeAllocator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private IPortSlotProvider portSlotProvider;

	@Inject
	private IVesselProvider vesselProvider;

	@Inject
	private IVoyagePlanner voyagePlanner;

	@FunctionalInterface
	private interface Setter {
		void accept(@Nullable ILoadOption load, @Nullable IDischargeOption discharge, long price);
	}

	@Override
	public @Nullable Pair<VoyagePlan, IAllocationAnnotation> processSchedule(final IVesselCharter vesselCharter, final VoyagePlan vp, final IPortTimesRecord portTimesRecord,
			ISequencesAttributesProvider sequencesAttributesProvider, @Nullable IAnnotatedSolution annotatedSolution) {
		final long startingHeelInM3 = vp.getStartingHeelInM3();
		final ICharterCostCalculator charterCostCalculator = vesselCharter.getCharterCostCalculator();

		boolean isCargoPlan = false;
		boolean missingPurchasePrice = false;
		boolean missingSalesPrice = false;

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
				if (idx != (currentSequence.length - offset)) {
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

		final long[] initialHeelVolumeRangeInM3 = new long[2];
		initialHeelVolumeRangeInM3[0] = startingHeelInM3;
		initialHeelVolumeRangeInM3[1] = startingHeelInM3;
		final int lastCV = 0;
		final boolean lastPlan = false;

		if (originalLoad != null || originalDischarge != null) {
			// Perform a binary search on sales price
			// First find a valid interval
			final int minPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(0.0);// Integer.MAX_VALUE;
			final int maxPricePerMMBTu = OptimiserUnitConvertor.convertToInternalPrice(50.0);// Integer.MAX_VALUE;

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

			final Pair<VoyagePlan, ICargoValueAnnotation> minPrice_Pair = evaluateBreakEvenPrice(vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV, lastPlan,
					portTimesRecord, originalLoad, originalDischarge, minPricePerMMBTu, priceSetter, sequencesAttributesProvider, null);
			assert minPrice_Pair != null;
			final long minPrice_Value = minPrice_Pair.getSecond().getTotalProfitAndLoss();
			final Pair<VoyagePlan, ICargoValueAnnotation> maxPrice_Pair = evaluateBreakEvenPrice(vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV, lastPlan,
					portTimesRecord, originalLoad, originalDischarge, maxPricePerMMBTu, priceSetter, sequencesAttributesProvider, null);

			assert maxPrice_Pair != null;
			final long maxPrice_Value = maxPrice_Pair.getSecond().getTotalProfitAndLoss();

			final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, minPrice_Value, maxPricePerMMBTu, maxPrice_Value, vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV,
					lastPlan, portTimesRecord, originalLoad, originalDischarge, isPurchase, priceSetter, sequencesAttributesProvider);

			priceSetter.accept(originalLoad, originalDischarge, breakEvenPricePerMMBtu);
			final Pair<VoyagePlan, ICargoValueAnnotation> p = evaluateBreakEvenPrice(vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV, lastPlan, portTimesRecord,
					originalLoad, originalDischarge, breakEvenPricePerMMBtu, priceSetter, sequencesAttributesProvider, annotatedSolution);
			assert p != null;

			p.getFirst().setIgnoreEnd(vp.isIgnoreEnd());

			return Pair.of(p.getFirst(), p.getSecond());
		}
		return null;
	}

	private @Nullable Pair<VoyagePlan, ICargoValueAnnotation> evaluateBreakEvenPrice(final IVesselCharter vesselCharter, final ICharterCostCalculator charterCostCalculator,
			final long[] initialHeelVolumeRangeInM3, final int lastCV, final boolean lastPlan, final IPortTimesRecord portTimesRecord, final @Nullable ILoadOption originalLoad,
			final @Nullable IDischargeOption originalDischarge, final int currentPricePerMMBTu, final Setter priceSetter, ISequencesAttributesProvider sequencesAttributesProvider,
			@Nullable IAnnotatedSolution annotatedSolution) {

		// Overwrite current break even price with test price
		priceSetter.accept(originalLoad, originalDischarge, currentPricePerMMBTu);

		VoyagePlan newVoyagePlan;
		IAllocationAnnotation newAllocationAnnotation;
		if (vesselCharter.getVesselInstanceType() == VesselInstanceType.DES_PURCHASE || vesselCharter.getVesselInstanceType() == VesselInstanceType.FOB_SALE) {
			final Pair<@NonNull VoyagePlan, @NonNull IAllocationAnnotation> p = voyagePlanner.makeNonShippedVoyagePlan(vesselProvider.getResource(vesselCharter), portTimesRecord, false,
					sequencesAttributesProvider, annotatedSolution);
			newVoyagePlan = p.getFirst();
			newAllocationAnnotation = p.getSecond();
		} else {
			final IResource resource = vesselProvider.getResource(vesselCharter);

			final VoyagePlan[] plans = new VoyagePlan[1];
			final IAllocationAnnotation[] allocations = new IAllocationAnnotation[1];

			final Consumer<List<@NonNull Pair<VoyagePlan, IPortTimesRecord>>> hook = vpList -> {

				final Pair<VoyagePlan, IPortTimesRecord> lastPP = vpList.get(vpList.size() - 1);
				assert vpList.size() == 1;

				for (final Pair<VoyagePlan, IPortTimesRecord> pp : vpList) {
					final VoyagePlan vp = pp.getFirst();
					plans[0] = vp;
					final IPortTimesRecord ptr = pp.getSecond();

					if (lastPlan && lastPP == pp) {
						vp.setIgnoreEnd(false);
					}
					// Some code paths may have already calculated volume and/or P&L
					// This happens if e.g. GCO or charter length has kicked in
					IAllocationAnnotation allocationAnnotation = null;
					ICargoValueAnnotation cargoValueAnnotation = null;
					if (pp.getSecond() instanceof ICargoValueAnnotation) {
						cargoValueAnnotation = (ICargoValueAnnotation) pp.getSecond();
						allocationAnnotation = cargoValueAnnotation;
					}
					if (allocationAnnotation == null && pp.getSecond() instanceof IAllocationAnnotation) {
						allocationAnnotation = (IAllocationAnnotation) pp.getSecond();
					}
					if (allocationAnnotation == null) {
						allocationAnnotation = volumeAllocator.allocate(vesselCharter, vp, ptr, annotatedSolution);
					}
					if (cargoValueAnnotation == null && allocationAnnotation != null) {
						final Pair<CargoValueAnnotation, Long> p = entityValueCalculator.evaluate(vp, allocationAnnotation, vesselCharter, null, annotatedSolution);
						cargoValueAnnotation = p.getFirst();
					}
					allocations[0] = cargoValueAnnotation;
				}

			};

			final boolean evaluateAll = false;
			final boolean extendedEvaluation = false;
			voyagePlanner.makeShippedVoyagePlans(resource, charterCostCalculator, portTimesRecord, initialHeelVolumeRangeInM3, lastCV, lastPlan, evaluateAll, extendedEvaluation, hook,
					sequencesAttributesProvider, annotatedSolution);

			newVoyagePlan = plans[0];
			newAllocationAnnotation = allocations[0];
		}
		assert newVoyagePlan != null;
		if (newAllocationAnnotation == null) {
			newAllocationAnnotation = volumeAllocator.allocate(vesselCharter, newVoyagePlan, portTimesRecord, annotatedSolution);
			assert newAllocationAnnotation != null;
		}

		if (!(newAllocationAnnotation instanceof ICargoValueAnnotation)) {
			final Pair<@NonNull CargoValueAnnotation, @NonNull Long> p = entityValueCalculator.evaluate(newVoyagePlan, newAllocationAnnotation, vesselCharter, null, annotatedSolution);
			newAllocationAnnotation = p.getFirst();

		}
		final ICargoValueAnnotation cargoAnnotation = (ICargoValueAnnotation) newAllocationAnnotation;

		for (final IPortSlot slot : cargoAnnotation.getSlots()) {
			final int slotPricePerMMBTu = cargoAnnotation.getSlotPricePerMMBTu(slot);
			if (slotPricePerMMBTu < 0) {
				return null;
			}
		}

		return Pair.of(newVoyagePlan, cargoAnnotation);
	}

	private int search(final int min, final long minValue, final int max, final long maxValue, final IVesselCharter vesselCharter, final ICharterCostCalculator charterCostCalculator,
			final long[] initialHeelVolumeRangeInM3, final int lastCV, final boolean lastPlan, final IPortTimesRecord portTimesRecord, final @Nullable ILoadOption originalLoad,
			final @Nullable IDischargeOption originalDischarge, final boolean isPurchase, final Setter priceSetter, ISequencesAttributesProvider sequencesAttributesProvider) {

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

		final Pair<VoyagePlan, ICargoValueAnnotation> p = evaluateBreakEvenPrice(vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV, lastPlan, portTimesRecord, originalLoad,
				originalDischarge, mid, priceSetter, sequencesAttributesProvider, null);
		assert p != null;
		long midValue = p.getSecond().getTotalProfitAndLoss();

		assert midValue != Long.MAX_VALUE;
		if (isPurchase) {
			midValue = -midValue;
		}

		if (midValue > 0) {
			return search(min, minValue, mid, midValue, vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV, lastPlan, portTimesRecord, originalLoad, originalDischarge,
					isPurchase, priceSetter, sequencesAttributesProvider);
		} else {
			return search(mid, midValue, max, maxValue, vesselCharter, charterCostCalculator, initialHeelVolumeRangeInM3, lastCV, lastPlan, portTimesRecord, originalLoad, originalDischarge,
					isPurchase, priceSetter, sequencesAttributesProvider);
		}
	}
}
