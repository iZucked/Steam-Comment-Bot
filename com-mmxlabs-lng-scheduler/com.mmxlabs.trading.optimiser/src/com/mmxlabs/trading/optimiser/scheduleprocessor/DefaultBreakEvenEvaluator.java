package com.mmxlabs.trading.optimiser.scheduleprocessor;

import javax.inject.Inject;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.IDischargeSlot;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IBreakEvenPriceCalculator;
import com.mmxlabs.scheduler.optimiser.contracts.IEntityValueCalculator;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequence;
import com.mmxlabs.scheduler.optimiser.fitness.ScheduledSequences;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.IAllocationAnnotation;
import com.mmxlabs.scheduler.optimiser.fitness.components.allocation.ICargoAllocator;
import com.mmxlabs.scheduler.optimiser.providers.ICharterMarketProvider;
import com.mmxlabs.scheduler.optimiser.providers.IPortCostProvider;
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
	private IVesselProvider vesselProvider;

	@Inject
	private ILNGVoyageCalculator voyageCalculator;

	@Inject
	private IEntityValueCalculator entityValueCalculator;

	@Inject
	private ICargoAllocator cargoAllocator;

	@Inject
	private ICharterMarketProvider charterMarketProvider;

	@Inject
	private IPortCostProvider portCostProvider;

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

			for (final VoyagePlan vp : seq.getVoyagePlans()) {

				boolean isCargoPlan = false;
				boolean missingPurchasePrice = false;
				boolean missingSalesPrice = false;
				// Grab the current list of arrival times and update the rolling currentTime
				// 5 as we know that is the max we need (currently - a single cargo)
				final int[] arrivalTimes = new int[5];
				int idx = -1;
				int loadIdx = -1;
				int dischargeIdx = -1;
				arrivalTimes[++idx] = currentTime;
				final Object[] currentSequence = vp.getSequence();

				ILoadOption originalLoad = null;
				IDischargeOption originalDischarge = null;

				for (final Object obj : currentSequence) {
					if (obj instanceof PortDetails) {
						final PortDetails details = (PortDetails) obj;
						if (idx != (currentSequence.length - 1)) {
							currentTime += details.getVisitDuration();
							arrivalTimes[++idx] = currentTime;

							if (details.getPortSlot().getPortType() == PortType.Load) {
								isCargoPlan = true;
								originalLoad = (ILoadOption) details.getPortSlot();
								loadIdx = idx - 1;
							} else if (details.getPortSlot().getPortType() == PortType.Discharge) {
								originalDischarge = (IDischargeOption) details.getPortSlot();
								dischargeIdx = idx - 1;
							}
						}
					} else if (obj instanceof VoyageDetails) {
						final VoyageDetails details = (VoyageDetails) obj;
						currentTime += details.getOptions().getAvailableTime();
						arrivalTimes[++idx] = currentTime;

						// record last ballast leg
						if (details.getOptions().getVesselState() == VesselState.Ballast) {
						}
					}
				}

				if (originalLoad != null) {
					if (originalLoad.getLoadPriceCalculator() instanceof IBreakEvenPriceCalculator) {
						missingPurchasePrice = true;
					}
				} else {
					continue;
				}
				if (originalDischarge != null) {
					if (originalDischarge.getDischargePriceCalculator() instanceof IBreakEvenPriceCalculator) {
						missingSalesPrice = true;
					}
				} else {
					continue;
				}

				if (!isCargoPlan || (!missingPurchasePrice && !missingSalesPrice)) {
					// No missing prices
					continue;
				}
				if (missingPurchasePrice && missingSalesPrice) {
					// Both prices missing - no supported
					throw new IllegalStateException("Unable to breakeven when both prices are missing");
				}
				//

				final Object[] newSequence = currentSequence.clone();
				final IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vp, arrivalTimes);
				final int cvValue = currentAllocation.getLoadOption().getCargoCVValue();
				final long dischargeVolume = currentAllocation.getDischargeVolume();
				final long loadVolume = currentAllocation.getLoadVolume();

				if (missingPurchasePrice) {

					// Get the new cargo allocation.

					// Purchase price in mmbtu = (sales revenue - shipping cost) / load volume in mmbtu

					final int dischargePricePerM3 = currentAllocation.getDischargeM3Price();
					// final int loadPricePerM3 = currentAllocation.getLoadM3Price();

					final long totalShippingCost = entityValueCalculator.getShippingCosts(vp, vessel, false, seq.getStartTime(), null);
					final long totalSalesRevenue = Calculator.multiply(dischargePricePerM3, dischargeVolume);

					final long breakEvenPurchaseCost = totalSalesRevenue - totalShippingCost;

					final long breakEvenPurchasePricePerM3 = Calculator.divide(breakEvenPurchaseCost, loadVolume);
					final long breakEvenPurchasePricePerMMBTu = Calculator.divide(breakEvenPurchasePricePerM3, cvValue);

					
					((IBreakEvenPriceCalculator)originalLoad.getLoadPriceCalculator()).setPrice((int)breakEvenPurchasePricePerMMBTu);

//					final ILoadOption beSlot;
//					if (originalLoad instanceof ILoadSlot) {
//						beSlot = new BELoadSlot((ILoadSlot) originalLoad, (int) breakEvenPurchasePricePerMMBTu);
//					} else {
//						beSlot = new BELoadOption(originalLoad, (int) breakEvenPurchasePricePerMMBTu);
//					}
//					PortDetails detail = (PortDetails) (currentSequence[loadIdx]);
//					detail = detail.clone();
//					detail.setPortSlot(beSlot);
//					newSequence[loadIdx] = detail;

					// Overwrite existing data
					voyageCalculator.calculateVoyagePlan(vp, vessel, arrivalTimes, newSequence);

				} else if (missingSalesPrice) {

					// Perform a binary search on sales price
					// First find a valid interval
					int minPricePerMMBTu = (int) Calculator.divide(currentAllocation.getLoadM3Price(), cvValue);
					long minPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence, minPricePerMMBTu);
					while (minPrice_Value > 0) {
						// Subtract $5
						minPricePerMMBTu -= 5000;
						minPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence, minPricePerMMBTu);
					}
					int maxPricePerMMBTu = 10 * minPricePerMMBTu;
					long maxPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence, maxPricePerMMBTu);
					while (maxPrice_Value < 0) {
						// Add $5
						maxPricePerMMBTu += 5000;
						maxPrice_Value = evaluateSalesPrice(seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence, maxPricePerMMBTu);
					}

					final int breakEvenPricePerMMBtu = search(minPricePerMMBTu, minPrice_Value, maxPricePerMMBTu, maxPrice_Value, seq, vessel, arrivalTimes, dischargeIdx, currentSequence,
							originalDischarge, newSequence);

//					final IDischargeOption beSlot;
					((IBreakEvenPriceCalculator)originalDischarge.getDischargePriceCalculator()).setPrice(breakEvenPricePerMMBtu);
//					if (originalDischarge instanceof IDischargeSlot) {
//						beSlot = new BEDischargeSlot((IDischargeSlot) originalDischarge, breakEvenPricePerMMBtu);
//					} else {
//						beSlot = new BEDischargeOption(originalDischarge, breakEvenPricePerMMBtu);
//					}
//					
//if (beSlot.hashCode() != originalDischarge.hashCode()) {
//	int ii = 0;
//}
//if (!beSlot.equals(originalDischarge)) {
//	int ii = 0;
//}
//if (!originalDischarge.equals(beSlot)) {
//	
//	originalDischarge.equals(beSlot);
//	int ii = 0;
//}

//					PortDetails detail = (PortDetails) (currentSequence[dischargeIdx]);
//					detail = detail.clone();
////					detail.setPortSlot(beSlot);
//					newSequence[dischargeIdx] = detail;

					voyageCalculator.calculateVoyagePlan(vp, vessel, arrivalTimes, newSequence);
				}
				//
				// final VoyagePlan newVoyagePlan = new VoyagePlan();
				// newSequence[ballastIdx] = newDetails;
				//
				// if (isCargoPlan) {
				// // Get the new cargo allocation.
				// final IAllocationAnnotation currentAllocation = cargoAllocator.allocate(vessel, vp, arrivalTimes);
				// final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, newVoyagePlan, arrivalTimes);
				//
				// originalOption = entityValueCalculator.evaluate(vp, currentAllocation, vessel, seq.getStartTime(), null);
				// newOption = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, seq.getStartTime(), null);
				//
				// }
				// // TODO: This should be recorded based on market availability groups and then processed.
				// if (originalOption >= newOption) {
				// // Keep
				// } else {
				// // Overwrite details
				// voyageCalculator.calculateVoyagePlan(vp, vessel, arrivalTimes, newSequence);
				// }
			}
		}
	}

	private long evaluateSalesPrice(@NonNull final ScheduledSequence seq, @NonNull final IVessel vessel, final int[] arrivalTimes, final int dischargeIdx, @NonNull final Object[] currentSequence,
			@NonNull final IDischargeOption originalDischarge, @NonNull final Object[] newSequence, final int currentPricePerMMBTu) {
		// Create new discharge slot with sales price option
//		final IDischargeOption beSlot;
//		if (originalDischarge instanceof IDischargeSlot) {
//			beSlot = new BEDischargeSlot((IDischargeSlot) originalDischarge, currentPricePerMMBTu);
//		} else {
//			beSlot = new BEDischargeOption(originalDischarge, currentPricePerMMBTu);
//		}
//
//		PortDetails detail = (PortDetails) (currentSequence[dischargeIdx]);
//		detail = detail.clone();
//		detail.setPortSlot(beSlot);
//		newSequence[dischargeIdx] = detail;
		
		((IBreakEvenPriceCalculator)originalDischarge.getDischargePriceCalculator()).setPrice(currentPricePerMMBTu);

		
		final VoyagePlan newVoyagePlan = new VoyagePlan();
		voyageCalculator.calculateVoyagePlan(newVoyagePlan, vessel, arrivalTimes, newSequence);
		final IAllocationAnnotation newAllocation = cargoAllocator.allocate(vessel, newVoyagePlan, arrivalTimes);
		final long newPnLValue = entityValueCalculator.evaluate(newVoyagePlan, newAllocation, vessel, seq.getStartTime(), null);
		return newPnLValue;
	}

	private int search(final int min, final long minValue, final int max, final long maxValue, @NonNull final ScheduledSequence seq, @NonNull final IVessel vessel, final int[] arrivalTimes,
			final int dischargeIdx, @NonNull final Object[] currentSequence, @NonNull final IDischargeOption originalDischarge, @NonNull final Object[] newSequence) {

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

		final long midValue = evaluateSalesPrice(seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence, mid);

		if (midValue > 0) {
			return search(min, minValue, mid, midValue, seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence);
		} else {
			return search(mid, midValue, max, maxValue, seq, vessel, arrivalTimes, dischargeIdx, currentSequence, originalDischarge, newSequence);
		}

	}
}
