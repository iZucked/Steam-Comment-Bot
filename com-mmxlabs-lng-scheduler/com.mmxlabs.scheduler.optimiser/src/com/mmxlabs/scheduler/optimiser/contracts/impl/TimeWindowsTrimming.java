package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.ArrayList;
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
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * Methods to trim time windows based on purchase and sales prices and canal costs
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
	public IPortTimeWindowsRecord processCargo(IPortTimeWindowsRecord portTimeWindowRecord) {
		boolean seenLoad = false;
		boolean seenDischarge = false;
		boolean complexCargo = false;
		List<Triple<IPortSlot, Class<?>, PricingEventType>> slotData = new LinkedList<>();

		for (IPortSlot slot : portTimeWindowRecord.getSlots()) {
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
			for (Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
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
			for (Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
				if (slotRow.getSecond().isAssignableFrom(ILoadOption.class)) {
					ILoadOption slot = (ILoadOption) slotRow.getFirst();
					if (slot.getLoadPriceCalculator() instanceof IPriceIntervalProvider) {
						trimLoadWindowIndependentOfDischarge(portTimeWindowRecord, slot);
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					IDischargeOption slot = (IDischargeOption) slotRow.getFirst();
					if (slot.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
						trimDischargeWindowIndependentOfLoad(portTimeWindowRecord, slot);
					}
				}
			}
		} else {
			Pair<ILoadOption, IDischargeOption> slots = priceIntervalProviderHelper.getLoadAndDischarge(portTimeWindowRecord, slotData);
			ILoadOption load = slots.getFirst();
			IDischargeOption discharge = slots.getSecond();
			if (load != null && discharge != null && load.getLoadPriceCalculator() instanceof IPriceIntervalProvider && discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
				if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
								|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// simplest case
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load),
							getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge), false);
				} else
					if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord) && (priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord))) {
					// complex case (L -> D; D -> L)
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load),
							getDischargePriceIntervalsBasedOnLoad(portTimeWindowRecord, discharge), false);
				} else if (priceIntervalProviderHelper.isDischargePricingEventTime(load, portTimeWindowRecord)
						&& (priceIntervalProviderHelper.isDischargePricingEventTime(discharge, portTimeWindowRecord)
								|| priceIntervalProviderHelper.isPricingDateSpecified(discharge, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// complex case (L -> D; D -> D)
					List<int[]> loadZeroPrices = new LinkedList<>();
					loadZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getStart(), 0 });
					List<int[]> loadIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getStart(),
							portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getEnd(), loadZeroPrices);
					List<int[]> dischargeIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
							(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, false);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, false);
				} else if ((priceIntervalProviderHelper.isLoadPricingEventTime(load, portTimeWindowRecord)
						|| priceIntervalProviderHelper.isPricingDateSpecified(load, PriceIntervalProviderHelper.getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& priceIntervalProviderHelper.isLoadPricingEventTime(discharge, portTimeWindowRecord)) {
					// complex case (L -> L; D -> L)
					List<int[]> dischargeZeroPrices = new LinkedList<>();
					dischargeZeroPrices.add(new int[] { portTimeWindowRecord.getSlotFeasibleTimeWindow(load).getStart(), 0 });
					List<int[]> dischargeIntervals = priceIntervalProviderHelper.getFeasibleIntervalSubSet(portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getStart(),
							portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge).getEnd(), dischargeZeroPrices);
					List<int[]> loadIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(),
							(IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, true);
					trimLoadAndDischargeWindowsWithRouteChoice(portTimeWindowRecord, load, discharge, loadIntervals, dischargeIntervals, false);
				}
			}
		}
		return portTimeWindowRecord;
	}

	int[] trimCargoTimeWindowsWithRouteOptimisation(IPortTimeWindowsRecord portTimeWindowsRecord, IVesselClass vesselClass, ILoadOption load, IDischargeOption discharge,
			List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		long[][] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumTravelTimes(load.getPort(), discharge.getPort(), vesselClass);
		int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration;
		List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			Pair<Integer, Integer> lowestPriceInterval = priceIntervalProviderHelper.getLowestPriceInterval(loadPriceIntervals);
			Pair<Integer, Integer> highestPriceInterval = priceIntervalProviderHelper.getHighestPriceInterval(dischargePriceIntervals);
			return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(lowestPriceInterval.getFirst(), lowestPriceInterval.getSecond(), highestPriceInterval.getFirst(),
					highestPriceInterval.getSecond(), loadDuration, (int) sortedCanalTimes[0][0]);
		} else {
			// we could go via canal but should we?
			ArrayList<int[]> purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals);
			ArrayList<int[]> salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals);
			int bestPurchaseDetailsIdx = priceIntervalProviderHelper.getMinIndexOfPriceIntervalList(purchaseIntervals);
			int bestSalesDetailsIdx = priceIntervalProviderHelper.getMaxIndexOfPriceIntervalList(salesIntervals);
			if (priceIntervalProviderHelper.isFeasibleTravelTime(purchaseIntervals.get(bestPurchaseDetailsIdx), salesIntervals.get(bestSalesDetailsIdx), loadDuration, sortedCanalTimes[0][0])) {
				// we can choose best bucket and go direct if we want
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
						salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) sortedCanalTimes[0][0]);
			} else {
				// we are going to have to go via a canal. is it worth it?
				int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				int[] bestPurchaseDetails = purchaseIntervals.get(bestPurchaseDetailsIdx);
				int[] bestSalesDetails = salesIntervals.get(bestSalesDetailsIdx);
				long[] bestCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes, bestSalesDetails[1] - bestPurchaseDetails[0] + loadDuration);
				long bestMargin = salesIntervals.get(bestSalesDetailsIdx)[2] - purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - (bestCanalDetails[1] / loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
						long[] newCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes,
								salesIntervals.get(salesIndex)[1] - purchaseIntervals.get(purchaseIndex)[0] + loadDuration);
						long newMargin = salesIntervals.get(salesIndex)[2] - purchaseIntervals.get(purchaseIndex)[2] - (newCanalDetails[1] / loadVolumeMMBTU);
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

	private int[] trimCargoTimeWindowsWithRouteOptimisationForInvertedCase(IPortTimeWindowsRecord portTimeWindowsRecord, IVesselClass vesselClass, ILoadOption load, IDischargeOption discharge,
			List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		long[][] sortedCanalTimes = schedulingCanalDistanceProvider.getMinimumTravelTimes(load.getPort(), discharge.getPort(), vesselClass);
		int loadDuration = portTimeWindowsRecord.getSlotDuration(load);
		int minTime = Math.max(priceIntervalProviderHelper.getMinimumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration, loadDuration);
		int maxTime = priceIntervalProviderHelper.getMaximumPossibleTimeForCargoIntervals(loadPriceIntervals, dischargePriceIntervals) + loadDuration;
		List<Integer> canalsWeCanUse = schedulingCanalDistanceProvider.getFeasibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() == 1) {
			// no options
			Pair<Integer, Integer> highestPriceInterval = priceIntervalProviderHelper.getHighestPriceInterval(loadPriceIntervals);
			Pair<Integer, Integer> lowestPriceInterval = priceIntervalProviderHelper.getLowestPriceInterval(dischargePriceIntervals);
			return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(highestPriceInterval.getFirst(), highestPriceInterval.getSecond(), lowestPriceInterval.getFirst(),
					lowestPriceInterval.getSecond(), loadDuration, (int) sortedCanalTimes[0][0]);
		} else {
			// we could go via canal but should we?
			ArrayList<int[]> purchaseIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(loadPriceIntervals); // discharge prices!!
			ArrayList<int[]> salesIntervals = priceIntervalProviderHelper.getIntervalsBoundsAndPrices(dischargePriceIntervals); // load prices!!
			int bestPurchaseDetailsIdx = priceIntervalProviderHelper.getMaxIndexOfPriceIntervalList(purchaseIntervals);
			int bestSalesDetailsIdx = priceIntervalProviderHelper.getMinIndexOfPriceIntervalList(salesIntervals);
			if (priceIntervalProviderHelper.isFeasibleTravelTime(purchaseIntervals.get(bestPurchaseDetailsIdx), salesIntervals.get(bestSalesDetailsIdx), loadDuration, sortedCanalTimes[0][0])) {
				// we can choose best bucket and go direct if we want
				return priceIntervalProviderHelper.getCargoBoundsWithCanalTrimming(purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1],
						salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1], loadDuration, (int) sortedCanalTimes[0][0]);
			} else {
				// we are going to have to go via a canal. is it worth it?
				int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				int[] bestPurchaseDetails = purchaseIntervals.get(bestPurchaseDetailsIdx);
				int[] bestSalesDetails = salesIntervals.get(bestSalesDetailsIdx);
				long[] bestCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes, bestSalesDetails[1] - bestPurchaseDetails[0] + loadDuration);
				long bestMargin = purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - salesIntervals.get(bestSalesDetailsIdx)[2] - (bestCanalDetails[1] / loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex >= 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
						long[] newCanalDetails = schedulingCanalDistanceProvider.getBestCanalDetails(sortedCanalTimes,
								salesIntervals.get(salesIndex)[1] - purchaseIntervals.get(purchaseIndex)[0] + loadDuration);
						long newMargin = purchaseIntervals.get(purchaseIndex)[2] - salesIntervals.get(salesIndex)[2] - (newCanalDetails[1] / loadVolumeMMBTU);
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

	private void trimLoadAndDischargeWindowsWithRouteChoice(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge, List<int[]> loadIntervals,
			List<int[]> dischargeIntervals, boolean switched) {
		IResource resource = portTimeWindowRecord.getResource();
		assert resource != null;
		IVesselClass vesselClass = priceIntervalProviderHelper.getVesselClass(resource);
		assert vesselClass != null;
		int[] bounds;
		if (switched) {
			bounds = trimCargoTimeWindowsWithRouteOptimisationForInvertedCase(portTimeWindowRecord, vesselClass, load, discharge, loadIntervals, dischargeIntervals);
		} else {
			bounds = trimCargoTimeWindowsWithRouteOptimisation(portTimeWindowRecord, vesselClass, load, discharge, loadIntervals, dischargeIntervals);
		}
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, bounds[0], bounds[1]);
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, bounds[2], bounds[3]);
	}

	@NonNull
	private List<int[]> getLoadPriceIntervalsIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getStart(), loadTimeWindow.getEnd(),
				priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(load, portTimeWindowRecord));
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getStart(), dischargeTimeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowIndependentOfLoad(discharge, portTimeWindowRecord));
	}

	private void trimLoadWindowIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(getLoadPriceIntervalsIndependentOfDischarge(portTimeWindowRecord, load));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	@NonNull
	private List<int[]> getLoadPriceIntervalsBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		ITimeWindow loadTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(loadTimeWindow.getStart(), loadTimeWindow.getEnd(),
				priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord));
	}

	private void trimLoadWindowBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getLoadPriceIntervalsBasedOnDischarge(portTimeWindowRecord, load));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	private void trimDischargeWindowIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(getDischargePriceIntervalsIndependentOfLoad(portTimeWindowRecord, discharge));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	@NonNull
	private List<int[]> getDischargePriceIntervalsBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		ITimeWindow dischargeTimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		return priceIntervalProviderHelper.getFeasibleIntervalSubSet(dischargeTimeWindow.getStart(), dischargeTimeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord));
	}

	private void trimDischargeWindowBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getLowestPriceInterval(priceIntervalProviderHelper.getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord)));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	private void loadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		List<int[]> complexPricingIntervals = priceIntervalProviderHelper.getComplexPriceIntervals(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowRecord,
				dateFromLoad);
		Pair<Integer, Integer> bounds = priceIntervalProviderHelper.getHighestPriceInterval(complexPricingIntervals);
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		if (dateFromLoad) {
			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
		} else {
			priceIntervalProviderHelper.createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
		}
	}

}
