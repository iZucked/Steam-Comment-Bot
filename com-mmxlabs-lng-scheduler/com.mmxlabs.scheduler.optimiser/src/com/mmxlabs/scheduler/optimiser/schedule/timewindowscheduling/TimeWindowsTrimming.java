/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Methods to trim time windows based on purchase and sales prices and canal costs
 * 
 * @author achurchill
 *
 */
public class TimeWindowsTrimming {

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderHelper;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	@Inject
	private ITimeWindowSchedulingCanalDistanceProvider schedulingCanalDistanceProvider;

	/**
	 * Trim time windows for a given set of slots
	 * 
	 * @param portTimeWindowRecord
	 * @return
	 */
	public IPortTimeWindowsRecord processCargo(final IPortTimeWindowsRecord portTimeWindowRecord) {
		boolean seenLoad = false;
		boolean seenDischarge = false;
		boolean complexCargo = false;
		final List<Triple<IPortSlot, Class<?>, PricingEventType>> slotData = new LinkedList<>();

		for (final IPortSlot slot : portTimeWindowRecord.getSlots()) {
			if (slot instanceof ILoadOption) {
				slotData.add(new Triple<IPortSlot, Class<?>, PricingEventType>(slot, slot.getClass(), ((ILoadOption) slot).getPricingEvent()));
				if (seenLoad) {
					complexCargo = true;
				} else {
					seenLoad = true;
				}
			} else if (slot instanceof IDischargeOption) {
				slotData.add(new Triple<IPortSlot, Class<?>, PricingEventType>(slot, slot.getClass(), ((IDischargeOption) slot).getPricingEvent()));
				if (seenDischarge) {
					complexCargo = true;
				} else {
					seenDischarge = true;
				}
			}
		}

		if (complexCargo) {
			for (final Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
				if (slotRow.getSecond().isAssignableFrom(ILoadOption.class)) {
					if (!priceIntervalProviderHelper.isLoadPricingEventTime((ILoadOption) slotRow.getFirst(), portTimeWindowRecord) && !priceIntervalProviderHelper
							.isPricingDateSpecified(slotRow.getFirst(), PriceIntervalProviderHelper.getPriceEventFromSlotOrContract((ILoadOption) slotRow.getFirst(), portTimeWindowRecord))) {
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					if (!priceIntervalProviderHelper.isDischargePricingEventTime((IDischargeOption) slotRow.getFirst(), portTimeWindowRecord) && !priceIntervalProviderHelper
							.isPricingDateSpecified(slotRow.getFirst(), PriceIntervalProviderHelper.getPriceEventFromSlotOrContract((ILoadOption) slotRow.getFirst(), portTimeWindowRecord))) {
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				}
			}
			for (final Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
				if (slotRow.getSecond().isAssignableFrom(ILoadOption.class)) {
					final ILoadOption slot = (ILoadOption) slotRow.getFirst();
					if (slot.getLoadPriceCalculator() instanceof IPriceIntervalProvider) {
						trimLoadWindowIndependentOfDischarge(portTimeWindowRecord, slot);
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					final IDischargeOption slot = (IDischargeOption) slotRow.getFirst();
					if (slot.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
						trimDischargeWindowIndependentOfLoad(portTimeWindowRecord, slot);
					}
				}
			}
		} else {
			final Pair<ILoadOption, IDischargeOption> slots = priceIntervalProviderHelper.getLoadAndDischarge(portTimeWindowRecord, slotData);
			final ILoadOption load = slots.getFirst();
			final IDischargeOption discharge = slots.getSecond();
			if (load != null && discharge != null && load.getLoadPriceCalculator() instanceof IPriceIntervalProvider && discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
				if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
								|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// simplest case
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load),
							getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge), false);
				} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord)
						&& (priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord))) {
					// complex case (L -> D; D -> L)
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load),
							getDischargePriceIntervalsBasedOnLoad(portTimeWindowRecord, discharge), false);
				} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord)
						&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
								|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// complex case (L -> D; D -> D)
					final List<int[]> loadZeroPrices = new LinkedList<>();
					loadZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getStart(), 0 });
					final List<int[]> loadIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getStart(),
							portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getEnd(), loadZeroPrices);
					final List<int[]> dischargeIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
							(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, false);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, false);
				} else if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord)) {
					// complex case (L -> L; D -> L)
					final List<int[]> dischargeZeroPrices = new LinkedList<>();
					dischargeZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getStart(), 0 });
					final List<int[]> dischargeIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getStart(),
							portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getEnd(), dischargeZeroPrices);
					final List<int[]> loadIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
							(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, true);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, false);
				}
			}
		}
		return portTimeWindowRecord;
	}

	/**
	 * Trim time windows given different route options
	 * 
	 * @param portTimeWindowsRecord
	 * @param vessel
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervals
	 * @param dischargePriceIntervals
	 * @return
	 */
	int[] trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(final IPortTimeWindowsRecord portTimeWindowsRecord, final IVessel vessel, final ILoadOption load, final IDischargeOption discharge,
			final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals, final boolean inverted) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final long[][] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel, loadTimeWindow.getStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		final int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals);
		final List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			Pair<Integer, Integer> loadBounds;
			Pair<Integer, Integer> dischargeBounds;
			if (!inverted) {
				loadBounds = priceIntervalProviderHelper.getLowestPriceInterval(loadPriceIntervals);
				dischargeBounds = priceIntervalProviderHelper.getHighestPriceInterval(dischargePriceIntervals);
			} else {
				loadBounds = priceIntervalProviderHelper.getHighestPriceInterval(loadPriceIntervals);
				dischargeBounds = priceIntervalProviderHelper.getLowestPriceInterval(dischargePriceIntervals);
			}
			// return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(loadBounds.getFirst(), loadBounds.getSecond(), dischargeBounds.getFirst(), dischargeBounds.getSecond(), loadDuration,
			// (int) sortedCanalTimes[canalsWeCanUse.get(0)][0]);
			return getCargoBounds(loadBounds.getFirst(), loadBounds.getSecond(), dischargeBounds.getFirst(), dischargeBounds.getSecond(), loadDuration,
					(int) sortedCanalTimes[canalsWeCanUse.get(0)][0], (int) sortedCanalTimes[canalsWeCanUse.get(0)][2]);
		} else {
			// we could go via canal but should we?
			final List<int[]> purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			final List<int[]> salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			if (!inverted) {
				return findBestBucketPairWithRouteAndBoiloffConsiderations(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, salesIntervals);
			} else {
				return findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(vessel, load, sortedCanalTimes, loadDuration, purchaseIntervals, salesIntervals);
			}
		}
	}

	/**
	 * Loops through the different pairs of purchase and sales pricing buckets and finds the option with the best margin with the purchase and sales intervals inverted
	 * 
	 * @param vessel
	 * @param load
	 * @param sortedCanalTimes
	 * @param loadDuration
	 * @param purchaseIntervals
	 * @param salesIntervals
	 * @return
	 */
	private int[] findBestBucketPairWithRouteAndBoiloffConsiderationsInverted(final IVessel vessel, final ILoadOption load, final long[][] sortedCanalTimes, final int loadDuration,
			final List<int[]> purchaseIntervals, final List<int[]> salesIntervals) {

		assert !purchaseIntervals.isEmpty();

		int bestPurchaseDetailsIdx = 0;
		int bestSalesDetailsIdx = salesIntervals.size() - 1;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		long[] bestCanalDetails = null;
		long bestMargin = Long.MIN_VALUE;
		for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex < purchaseIntervals.size(); purchaseIndex++) {
			for (int salesIndex = bestSalesDetailsIdx; salesIndex >= 0; salesIndex--) {
				final int salesPrice = purchaseIntervals.get(purchaseIndex)[2]; // inverted!
				final long[] newCanalDetails = priceIntervalProviderHelper.getBestCanalDetailsWithBoiloff(purchaseIntervals.get(purchaseIndex), salesIntervals.get(salesIndex), loadDuration,
						salesPrice, sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), load.getCargoCVValue(), loadVolumeMMBTU);
				final long boiloffCost = newCanalDetails[3];
				final long boiloffMargin = boiloffCost / loadVolumeMMBTU;
				final long canalMargin = (newCanalDetails[2] / loadVolumeMMBTU);
				final long newMargin = purchaseIntervals.get(purchaseIndex)[2] - salesIntervals.get(salesIndex)[2] - canalMargin - boiloffMargin; // inverted!
				if (newMargin > bestMargin) {
					bestMargin = newMargin;
					bestPurchaseDetailsIdx = purchaseIndex;
					bestSalesDetailsIdx = salesIndex;
					bestCanalDetails = newCanalDetails;
				}
			}
		}
		assert bestCanalDetails != null;
		// return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
		// salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0]);
		return getCargoBounds(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1], salesIntervals.get(bestSalesDetailsIdx)[0],
				salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0], (int) bestCanalDetails[1]);
	}

	/**
	 * Loops through the different pairs of purchase and sales pricing buckets and finds the option with the best margin
	 * 
	 * @param vessel
	 * @param load
	 * @param sortedCanalTimes
	 * @param loadDuration
	 * @param purchaseIntervals
	 * @param salesIntervals
	 * @return
	 */
	private int[] findBestBucketPairWithRouteAndBoiloffConsiderations(final IVessel vessel, final ILoadOption load, final long[][] sortedCanalTimes, final int loadDuration,
			final List<int[]> purchaseIntervals, final List<int[]> salesIntervals) {

		assert !purchaseIntervals.isEmpty();

		int bestPurchaseDetailsIdx = purchaseIntervals.size() - 1;
		int bestSalesDetailsIdx = 0;
		final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
		long[] bestCanalDetails = null;
		long bestMargin = Long.MIN_VALUE;
		for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
			for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
				final long[] newCanalDetails = priceIntervalProviderHelper.getBestCanalDetailsWithBoiloff(purchaseIntervals.get(purchaseIndex), salesIntervals.get(salesIndex), loadDuration,
						salesIntervals.get(salesIndex)[2], sortedCanalTimes, vessel.getVesselClass().getNBORate(VesselState.Laden), load.getCargoCVValue(), loadVolumeMMBTU);
				final long boiloffCost = newCanalDetails[3];
				final long boiloffMargin = boiloffCost / loadVolumeMMBTU;
				final long canalMargin = newCanalDetails[2] / loadVolumeMMBTU;
				final long newMargin = salesIntervals.get(salesIndex)[2] - purchaseIntervals.get(purchaseIndex)[2] - canalMargin - boiloffMargin;
				if (newMargin > bestMargin) {
					bestMargin = newMargin;
					bestPurchaseDetailsIdx = purchaseIndex;
					bestSalesDetailsIdx = salesIndex;
					bestCanalDetails = newCanalDetails;
				}
			}
		}

		assert bestCanalDetails != null;

		// return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
		// salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0]);
		return getCargoBounds(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1], salesIntervals.get(bestSalesDetailsIdx)[0],
				salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0], (int) bestCanalDetails[1]);
	}

	private int[] getCargoBounds(final int purchaseStart, final int purchaseEnd, final int salesStart, final int salesEnd, final int loadDuration, final int maxSpeedCanal, final int nboSpeedCanal) {
		// return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
		// salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0])
		final int[] idealTimes = priceIntervalProviderHelper.getIdealLoadAndDischargeTimesGivenCanal(purchaseStart, purchaseEnd, salesStart, salesEnd, loadDuration, maxSpeedCanal, nboSpeedCanal);
		return new int[] { idealTimes[0], idealTimes[0], idealTimes[1], idealTimes[1] };
	}

	/**
	 * Trim time windows given different route options
	 * 
	 * @param portTimeWindowsRecord
	 * @param vessel
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervals
	 * @param dischargePriceIntervals
	 * @return
	 */
	int[] trimCargoTimeWindowsWithRouteOptimisation(final IPortTimeWindowsRecord portTimeWindowsRecord, final IVessel vessel, final ILoadOption load, final IDischargeOption discharge,
			final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final long[][] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel, loadTimeWindow.getStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		final int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals);
		final List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			final Pair<Integer, Integer> lowestPriceInterval = priceIntervalProviderHelper.getLowestPriceInterval(loadPriceIntervals);
			final Pair<Integer, Integer> highestPriceInterval = priceIntervalProviderHelper.getHighestPriceInterval(dischargePriceIntervals);
			return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(lowestPriceInterval.getFirst(), lowestPriceInterval.getSecond(), highestPriceInterval.getFirst(),
					highestPriceInterval.getSecond(), loadDuration, (int) sortedCanalTimes[canalsWeCanUse.get(0)][0]);
		} else {
			// we could go via canal but should we?
			final List<int[]> purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			final List<int[]> salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			int bestPurchaseDetailsIdx = priceIntervalProviderHelper.getMinIndexOfPriceIntervalList(purchaseIntervals);
			int bestSalesDetailsIdx = priceIntervalProviderHelper.getMaxIndexOfPriceIntervalList(salesIntervals);
			if (priceIntervalProviderHelper.isFeasibleTravelTime(purchaseIntervals.get(bestPurchaseDetailsIdx), salesIntervals.get(bestSalesDetailsIdx), loadDuration, sortedCanalTimes[0][0])) {
				// we can choose best bucket and go direct if we want
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
						salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) sortedCanalTimes[0][0]);
			} else {
				// we are going to have to go via a canal. is it worth it?
				final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				final int[] bestPurchaseDetails = purchaseIntervals.get(bestPurchaseDetailsIdx);
				final int[] bestSalesDetails = salesIntervals.get(bestSalesDetailsIdx);
				long[] bestCanalDetails = priceIntervalProviderHelper.getBestCanalDetails(bestPurchaseDetails, bestSalesDetails, loadDuration, sortedCanalTimes);
				long bestMargin = salesIntervals.get(bestSalesDetailsIdx)[2] - purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - (bestCanalDetails[1] / loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
						final long[] newCanalDetails = priceIntervalProviderHelper.getBestCanalDetails(purchaseIntervals.get(purchaseIndex), salesIntervals.get(salesIndex), loadDuration,
								sortedCanalTimes);
						final long newMargin = salesIntervals.get(salesIndex)[2] - purchaseIntervals.get(purchaseIndex)[2] - (newCanalDetails[1] / loadVolumeMMBTU);
						if (newMargin > bestMargin) {
							bestMargin = newMargin;
							bestPurchaseDetailsIdx = purchaseIndex;
							bestSalesDetailsIdx = salesIndex;
							bestCanalDetails = newCanalDetails;
						}
					}
				}
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
						salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0]);
			}
		}
	}

	/**
	 * Trim time windows given different route options when load depends on discharge and discharge depends on load
	 * 
	 * @param portTimeWindowsRecord
	 * @param vessel
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervals
	 * @param dischargePriceIntervals
	 * @return
	 */
	private int[] trimCargoTimeWindowsWithRouteOptimisationForInvertedCase(final IPortTimeWindowsRecord portTimeWindowsRecord, final IVessel vessel, final ILoadOption load,
			final IDischargeOption discharge, final List<int[]> loadPriceIntervals, final List<int[]> dischargePriceIntervals) {
		final ITimeWindow loadTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		assert loadTimeWindow != null;
		final long[][] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumLadenTravelTimes(load.getPort(), discharge.getPort(), vessel, loadTimeWindow.getStart());
		assert sortedCanalTimes.length > 0;
		final int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		final int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		final int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration;
		final List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			final Pair<Integer, Integer> highestPriceInterval = priceIntervalProviderHelper.getHighestPriceInterval(loadPriceIntervals);
			final Pair<Integer, Integer> lowestPriceInterval = priceIntervalProviderHelper.getLowestPriceInterval(dischargePriceIntervals);
			return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(highestPriceInterval.getFirst(), highestPriceInterval.getSecond(), lowestPriceInterval.getFirst(),
					lowestPriceInterval.getSecond(), loadDuration, (int) sortedCanalTimes[0][0]);
		} else {
			// we could go via canal but should we?
			final List<int[]> purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals); // discharge prices!!
			final List<int[]> salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals); // load prices!!
			int bestPurchaseDetailsIdx = priceIntervalProviderHelper.getMaxIndexOfPriceIntervalList(purchaseIntervals);
			int bestSalesDetailsIdx = priceIntervalProviderHelper.getMinIndexOfPriceIntervalList(salesIntervals);
			if (priceIntervalProviderHelper.isFeasibleTravelTime(purchaseIntervals.get(bestPurchaseDetailsIdx), salesIntervals.get(bestSalesDetailsIdx), loadDuration, sortedCanalTimes[0][0])) {
				// we can choose best bucket and go direct if we want
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
						salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) sortedCanalTimes[0][0]);
			} else {
				// we are going to have to go via a canal. is it worth it?
				final int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				final int[] bestPurchaseDetails = purchaseIntervals.get(bestPurchaseDetailsIdx);
				final int[] bestSalesDetails = salesIntervals.get(bestSalesDetailsIdx);
				long[] bestCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes, bestSalesDetails[1] - bestPurchaseDetails[0] + loadDuration);
				long bestMargin = purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - salesIntervals.get(bestSalesDetailsIdx)[2] - (bestCanalDetails[1] / loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
						final long[] newCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes,
								salesIntervals.get(salesIndex)[1] - purchaseIntervals.get(purchaseIndex)[0] + loadDuration);
						final long newMargin = purchaseIntervals.get(purchaseIndex)[2] - salesIntervals.get(salesIndex)[2] - (newCanalDetails[1] / loadVolumeMMBTU);
						if (newMargin > bestMargin) {
							bestMargin = newMargin;
							bestPurchaseDetailsIdx = purchaseIndex;
							bestSalesDetailsIdx = salesIndex;
							bestCanalDetails = newCanalDetails;
						}
					}
				}
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
						salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) bestCanalDetails[0]);
			}
		}
	}

	/**
	 * Trim a time window for a cargo considering different route options
	 * 
	 * @param portTimeWindowRecord
	 * @param load
	 * @param discharge
	 * @param loadIntervals
	 * @param dischargeIntervals
	 * @param switched
	 */
	private void trimLoadAndDischargeWindowsWithRouteChoice(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load, final IDischargeOption discharge,
			final List<int[]> loadIntervals, final List<int[]> dischargeIntervals, final boolean switched) {
		final IResource resource = portTimeWindowRecord.getResource();
		assert resource != null;
		final IVessel vessel = priceIntervalProviderHelper.getVessel(resource);
		assert vessel != null;
		int[] bounds;
		if (switched) {
			bounds = trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, vessel, load, discharge, loadIntervals, dischargeIntervals, true);
		} else {
			bounds = trimCargoTimeWindowsWithRouteOptimisationAndBoilOff(portTimeWindowRecord, vessel, load, discharge, loadIntervals, dischargeIntervals, false);
		}
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, bounds[0], bounds[1]);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, bounds[2], bounds[3]);
	}

	@NonNull
	private List<int[]> getLoadPriceIntervalsIndependentOfDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getStart(), loadTimeWindow.getEnd(),
				priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(load, portTimeWindowRecord));
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsIndependentOfLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getStart(), dischargeTimeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowIndependentOfLoad(discharge, portTimeWindowRecord));
	}

	private void trimLoadWindowIndependentOfDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	@NonNull
	private List<int[]> getLoadPriceIntervalsBasedOnDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load) {
		final ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getStart(), loadTimeWindow.getEnd(),
				priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord));
	}

	private void trimLoadWindowBasedOnDischarge(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load, final IDischargeOption discharge) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	private void trimDischargeWindowIndependentOfLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsBasedOnLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final IDischargeOption discharge) {
		final ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getStart(), dischargeTimeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord));
	}

	private void trimDischargeWindowBasedOnLoad(final IPortTimeWindowsRecord portTimeWindowRecord, final ILoadOption load, final IDischargeOption discharge) {
		final ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(priceIntervalProviderHelper.getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord)));
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	private void loadOrDischargeDeterminesBothPricingEvents(final ILoadOption load, final IDischargeOption discharge, final IPriceIntervalProvider loadPriceIntervalProvider,
			final IPriceIntervalProvider dischargePriceIntervalProvider, final IPortTimeWindowsRecord portTimeWindowRecord, final boolean dateFromLoad) {
		final List<int[]> complexPricingIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider,
				portTimeWindowRecord, dateFromLoad);
		final Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(complexPricingIntervals);
		final int start = bounds.getFirst();
		final int end = bounds.getSecond();
		if (dateFromLoad) {
			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
		} else {
			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
		}
	}

}
