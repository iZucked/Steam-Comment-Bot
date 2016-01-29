package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.optimiser.core.IResource;
import com.mmxlabs.optimiser.core.scenario.common.IMultiMatrixProvider;
import com.mmxlabs.optimiser.core.scenario.common.MatrixEntry;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.OptimiserUnitConvertor;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPort;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.IVessel;
import com.mmxlabs.scheduler.optimiser.components.IVesselAvailability;
import com.mmxlabs.scheduler.optimiser.components.IVesselClass;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.IRouteCostProvider;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.providers.IVesselProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class _PriceIntervalProviderHelper {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	@Inject
	private IVesselProvider vesselProvider;
	
	@Inject
	private IMultiMatrixProvider<IPort, Integer> distanceProvider;

	@Inject
	private IRouteCostProvider routeCostProvider;

	private Set<PricingEventType> loadPricingEventTypeSet = new HashSet<>(Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_LOAD, PricingEventType.END_OF_LOAD_WINDOW,
			PricingEventType.START_OF_LOAD, PricingEventType.START_OF_LOAD_WINDOW, }));

	private Set<PricingEventType> dischargePricingEventTypeSet = new HashSet<>(Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_DISCHARGE, PricingEventType.END_OF_DISCHARGE_WINDOW,
			PricingEventType.START_OF_DISCHARGE, PricingEventType.START_OF_DISCHARGE_WINDOW, }));
	
	private CanalComparator canalComparator = new CanalComparator();
	
	static final class CanalComparator implements Comparator<int[]> {
		@Override
		public int compare(int[] o1, int[] o2) {
			if (o1 == null || o2 == null) {
				return 0;
			} else {
				return Integer.compare(o1[2], o2[2]);
			}
		}
	}
	
	/**
	 * Trim time windows for a given set of slots
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
					if (!isLoadPricingEventTime((ILoadOption) slotRow.getFirst(), portTimeWindowRecord) && !isPricingDateSpecified(slotRow.getFirst(), getPriceEventFromSlotOrContract((ILoadOption) slotRow.getFirst(), portTimeWindowRecord))) { // DO NOT COMMIT - refactor
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					if (!isDischargePricingEventTime((IDischargeOption) slotRow.getFirst(), portTimeWindowRecord)  && !isPricingDateSpecified(slotRow.getFirst(), getPriceEventFromSlotOrContract((ILoadOption) slotRow.getFirst(), portTimeWindowRecord))) {
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				}
			}
			for (Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
				ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(slotRow.getFirst());
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
			Pair<ILoadOption, IDischargeOption> slots = getLoadAndDischarge(portTimeWindowRecord, slotData);
			ILoadOption load = slots.getFirst();
			IDischargeOption discharge = slots.getSecond();
			if (load != null && discharge != null) {
				if (
						(isLoadPricingEventTime(load, portTimeWindowRecord) || isPricingDateSpecified(load, getPriceEventFromSlotOrContract(load, portTimeWindowRecord)))
						&& (isDischargePricingEventTime(discharge, portTimeWindowRecord) || isPricingDateSpecified(discharge, getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))
					) {
					// simplest case
					if (load.getLoadPriceCalculator() instanceof IPriceIntervalProvider) {
						trimLoadWindowIndependentOfDischarge(portTimeWindowRecord, load);
					}
					if (discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
						trimDischargeWindowIndependentOfLoad(portTimeWindowRecord, discharge);
					}
				} else if (isDischargePricingEventTime(load, portTimeWindowRecord) && (isLoadPricingEventTime(discharge, portTimeWindowRecord))) {
					// complex case (L -> D; D -> L)
					trimLoadWindowBasedOnDischarge(portTimeWindowRecord, load, discharge); // DON NOT COMMIT (check for IPriceIntervalProvider)
					trimDischargeWindowBasedOnLoad(portTimeWindowRecord, load, discharge);
				}
				else if (isDischargePricingEventTime(load, portTimeWindowRecord) && (isDischargePricingEventTime(discharge, portTimeWindowRecord)
						|| isPricingDateSpecified(discharge, getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord)))) {
					// complex case (L -> D; D -> D)
					loadOrDischargeDeterminesBothPricingEvents(load, discharge, ((IPriceIntervalProvider) load.getLoadPriceCalculator()),
							((IPriceIntervalProvider) discharge.getDischargePriceCalculator()), portTimeWindowRecord, false);
				} else if ((isLoadPricingEventTime(load, portTimeWindowRecord) || isPricingDateSpecified(load, getPriceEventFromSlotOrContract(load, portTimeWindowRecord))) && isLoadPricingEventTime(discharge, portTimeWindowRecord)) {
					// complex case (L -> L; D -> L)
					loadOrDischargeDeterminesBothPricingEvents(load, discharge, ((IPriceIntervalProvider) load.getLoadPriceCalculator()),
							((IPriceIntervalProvider) discharge.getDischargePriceCalculator()), portTimeWindowRecord, true);
				}
			}
		}
		return portTimeWindowRecord;
	}

	/**
	 * Produce a list of the difference between purchase and sales price at hour points, when slots in a cargo are not price independent
	 * @param start
	 * @param end
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervalProvider
	 * @param dischargePriceIntervalProvider
	 * @param portTimeWindowRecord
	 * @return
	 */
	public List<int[]> buildComplexPriceIntervals(int start, int end, ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord) {
		int[][] intervals = getOverlappingWindows(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, start, end, portTimeWindowRecord);
		List<int[]> bestIntervals = new LinkedList<>();
		for (int[] interval : intervals) {
			int loadPricingTime = shiftTimeByTimezoneToUTC(interval[0], load, portTimeWindowRecord, getPriceEventFromSlotOrContract(load, portTimeWindowRecord));
			int purchasePrice = getPriceFromLoadOrDischargeCalculator(load, load, discharge, loadPricingTime);
			int dischargePricingTime = shiftTimeByTimezoneToUTC(interval[0], discharge, portTimeWindowRecord, getPriceEventFromSlotOrContract(discharge, portTimeWindowRecord));
			int salesPrice = getPriceFromLoadOrDischargeCalculator(discharge, load, discharge, dischargePricingTime);
			int difference = salesPrice - purchasePrice;
			bestIntervals.add(new int[] { interval[0], difference });
		}
		bestIntervals.add(new int[] {intervals[intervals.length-1][1], Integer.MIN_VALUE });
		return bestIntervals;
	}

	/**
	 * Produces a list of the time a purchase price changes for a load slot 
	 * @param slot
	 * @param intervals
	 * @param curve
	 * @param start
	 * @param end
	 * @param offsetInHours
	 * @param portTimeWindowRecord
	 * @return
	 */
	public List<int[]> getPriceIntervalsList(ILoadOption slot, IIntegerIntervalCurve intervals, ICurve curve, int start, int end, int offsetInHours, IPortTimeWindowsRecord portTimeWindowRecord) {
		List<int[]> priceIntervals = new LinkedList<>();
		buildIntervalsList(slot, intervals, curve, start, end, portTimeWindowRecord, priceIntervals);
		return priceIntervals;
	}

	/**
	 * Produces a list of the time a sales price changes for a discharge slot 
	 * @param slot
	 * @param intervals
	 * @param curve
	 * @param start
	 * @param end
	 * @param offsetInHours
	 * @param portTimeWindowRecord
	 * @return
	 */
	public List<int[]> getPriceIntervalsList(IDischargeOption slot, IIntegerIntervalCurve intervals, ICurve curve, int start, int end, int offsetInHours, IPortTimeWindowsRecord portTimeWindowRecord) {
		List<int[]> priceIntervals = new LinkedList<>();
		buildIntervalsList(slot, intervals, curve, start, end, portTimeWindowRecord, priceIntervals);
		return priceIntervals;
	}

	/**
	 * Provides a list of hours indicating a change in price for a particular slot
	 * @param start
	 * @param end
	 * @param slot
	 * @param intervals
	 * @param portTimeWindowsRecord
	 * @return
	 */
	public List<Integer> buildDateChangeCurveAsIntegerList(int start, int end, IPortSlot slot, int[] intervals, IPortTimeWindowsRecord portTimeWindowsRecord) {
		// TODO - get rid of PriceEventType (change to int[]?)
		List<Integer> shifted = new LinkedList<>();
		PricingEventType pricingEventType = getPriceEventTypeFromPortSlot(slot, portTimeWindowsRecord);
		if (isStartOfWindow(pricingEventType)) {
			shifted.add(start);
		} else if (isEndOfWindow(pricingEventType)) {
			shifted.add(start);
		} else if (isPricingDateSpecified(slot, pricingEventType)) {
			shifted.add(start);
		} else {
			shifted.add(start);
			for (int h = 1; h < intervals.length; h++) {
				IPortSlot pricingSlot = slot;
				if (slot instanceof ILoadOption && dischargePricingEventTypeSet.contains(pricingEventType)) {
					pricingSlot = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
				} else if (slot instanceof IDischargeOption && loadPricingEventTypeSet.contains(pricingEventType)) {
					pricingSlot = getFirstLoadOption(portTimeWindowsRecord.getSlots());
				}
				int date = getShiftedDate(pricingSlot, portTimeWindowsRecord, intervals[h], pricingEventType);
				if (date < end) {
					shifted.add(date);
				}
			}
		}
		shifted.add(end);
		return shifted;
	}
	
//	private void trimCargoTimeWindowsWithRouteOptimisation(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge, List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
//		IResource resource = portTimeWindowRecord.getResource();
//		// DO NOT COMMIT (null check)
//		IVesselClass vesselClass = getVesselClass(resource);
//		Pair<Integer,Integer> times = getMinimumTravelTimes(load.getPort(), discharge.getPort(), vesselClass);
//		int maxTime = getMaxTime(loadPriceIntervals, dischargePriceIntervals);
//		int minTime = getMinTime(loadPriceIntervals, dischargePriceIntervals);
//		if (times.getSecond() == null || times.getSecond() >= times.getFirst() || minTime >= times.getFirst() || maxTime <= times.getFirst()) {
//			// Can't travel via canal anyway
//			chooseMinPurchase();
//			chooseMaxDischarge();
//		} else {
//			// we could go via canal but should we?
//			// case (1) L | L; D | D
//			ArrayList<int[]> purchaseIntervals = getSortedPriceIntervals(loadPriceIntervals);
//			ArrayList<int[]> salesIntervals = getSortedPriceIntervals(dischargePriceIntervals);
//			int leftIndex = 0;
//			int rightIndex = salesIntervals.size()-1;
//			int endOfBestDischargeBucket = salesIntervals.get(rightIndex)[1];
//			int startOfBestLoadBucket = purchaseIntervals.get(leftIndex)[1];
//			
//			if ((endOfBestDischargeBucket - startOfBestLoadBucket) >= times.getFirst()) {
//				// we can choose best bucket and go direct if we want
//			} else {
//				// we are going to have to go via suez. is it worth it?
//				int suezCost = (int) (routeCostProvider.getRouteCost("Suez", vesselClass, VesselState.Laden) / load.getMaxLoadVolumeMMBTU()); 
//				int bestMargin = salesIntervals.get(rightIndex)[3] - purchaseIntervals.get(rightIndex)[3] - suezCost;
//				int nextLeftToTry = purchaseBucketGetNextValidIndex(purchaseIntervals, leftIndex, endOfBestDischargeBucket, times.getFirst());
//				int nextRightToTry = salesBucketGetNextValidIndex(salesIntervals, rightIndex, startOfBestLoadBucket, times.getFirst());
//				boolean purchase = true;
//				while (nextRightToTry != -1 && nextLeftToTry != -1) {
//					
//				}
//			}
//			// case (2) L | D; D | L
//			// case (3) L | D; D | L
//		}
//		
//	}
	
	int[] trimCargoTimeWindowsWithRouteOptimisation(IPortTimeWindowsRecord portTimeWindowRecord, IVesselClass vesselClass, ILoadOption load, IDischargeOption discharge, List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		long[][] sortedCanalTimes = getMinimumTravelTimes(load.getPort(), discharge.getPort(), vesselClass);
		int minTime = getMinTime(loadPriceIntervals, dischargePriceIntervals);
		int maxTime = getMaxTime(loadPriceIntervals, dischargePriceIntervals);
		List<Integer> canalsWeCanUse = getPossibleRoutes(sortedCanalTimes, minTime, maxTime);
		if (canalsWeCanUse.size() <= 1) {
			// no options
			// DO NOT COMMIT (trim)
			return new int[] {getLowestPriceInterval(loadPriceIntervals).getFirst(), getLowestPriceInterval(loadPriceIntervals).getSecond(), getHighestPriceInterval(dischargePriceIntervals).getFirst(), getHighestPriceInterval(dischargePriceIntervals).getSecond()};
		} else {
			// we could go via canal but should we?
			// case (1) L | L; D | D
			ArrayList<int[]> purchaseIntervals = getIntervalsBoundsAndPrices(loadPriceIntervals);
			ArrayList<int[]> salesIntervals = getIntervalsBoundsAndPrices(dischargePriceIntervals);
			int bestPurchaseDetailsIdx = getMinIndex(purchaseIntervals, canalComparator);
			int bestSalesDetailsIdx = getMaxIndex(salesIntervals, canalComparator);
			if (isFeasibleTravelTime(purchaseIntervals.get(bestPurchaseDetailsIdx), salesIntervals.get(bestSalesDetailsIdx), sortedCanalTimes[0][0])) {
				// we can choose best bucket and go direct if we want
				return new int[] {getLowestPriceInterval(loadPriceIntervals).getFirst(), getLowestPriceInterval(loadPriceIntervals).getSecond(), getHighestPriceInterval(dischargePriceIntervals).getFirst(), getHighestPriceInterval(dischargePriceIntervals).getSecond()};
			} else {
				// we are going to have to go via a canal. is it worth it?
				int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				int[] bestPurchaseDetails = purchaseIntervals.get(bestPurchaseDetailsIdx);
				int[] bestSalesDetails = salesIntervals.get(bestSalesDetailsIdx);
				long[] bestCanalDetails = getBestCanalDetails(sortedCanalTimes, bestSalesDetails[1] - bestPurchaseDetails[0]);
				long bestMargin = salesIntervals.get(bestSalesDetailsIdx)[2] - purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - (bestCanalDetails[1]/loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex > 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
						long[] newCanalDetails = getBestCanalDetails(sortedCanalTimes, salesIntervals.get(salesIndex)[1] - purchaseIntervals.get(purchaseIndex)[0]);
						long newMargin = salesIntervals.get(salesIndex)[2] - purchaseIntervals.get(purchaseIndex)[2] - (newCanalDetails[1]/loadVolumeMMBTU);
						if (newMargin > bestMargin) {
							bestMargin = newMargin;
							bestPurchaseDetailsIdx = purchaseIndex;
							bestSalesDetailsIdx = salesIndex;
						}
					}
				}
				return new int[] {purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1], salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1]};
			}
		}
		
	}

	private boolean isFeasibleTravelTime(int[] purchase, int[] sales, long time) {
		return time >= sales[0] - purchase[1] && time <= sales[1] - purchase[0];
	}

	private List<Integer> getPossibleRoutes(long[][] sortedCanalTimes, int minTime, int maxTime) {
		List<Integer> canalsWeCanUse = new LinkedList<>();
		for (int i = 0; i < sortedCanalTimes.length; i++) {
			if (sortedCanalTimes[i][0] >= minTime && sortedCanalTimes[i][0] <= maxTime) {
				break;
			} else {
				canalsWeCanUse.add(i);
			}
		}
		return canalsWeCanUse;
	}

	int[] trimCargoTimeWindowsWithRouteOptimisation_LDDL(IPortTimeWindowsRecord portTimeWindowRecord, IVesselClass vesselClass, ILoadOption load, IDischargeOption discharge, List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		long[][] sortedCanalTimes = getMinimumTravelTimes(load.getPort(), discharge.getPort(), vesselClass);
		int maxTime = getMaxTime(loadPriceIntervals, dischargePriceIntervals);
		int minTime = getMinTime(loadPriceIntervals, dischargePriceIntervals);
		if (minTime >= sortedCanalTimes[0][0]) { // do not commit OR WE HAVE TO GO VIA A CANAL
			return new int[] {getLowestPriceInterval(loadPriceIntervals).getFirst(), getLowestPriceInterval(loadPriceIntervals).getSecond(), getHighestPriceInterval(dischargePriceIntervals).getFirst(), getHighestPriceInterval(dischargePriceIntervals).getSecond()};
		} else {
			// we could go via canal but should we?
			ArrayList<int[]> purchaseIntervals = getIntervalsBoundsAndPrices(loadPriceIntervals); // discharge prices!!
			ArrayList<int[]> salesIntervals = getIntervalsBoundsAndPrices(dischargePriceIntervals); // load prices!!
			int bestPurchaseDetailsIdx = getMaxIndex(purchaseIntervals, canalComparator); // discharge prices!!
			int bestSalesDetailsIdx = getMinIndex(salesIntervals, canalComparator); // load prices!!
			int bestInterval = purchaseIntervals.get(bestPurchaseDetailsIdx)[1] - salesIntervals.get(bestSalesDetailsIdx)[0];
			if (bestInterval >= sortedCanalTimes[0][0]) {
				// we can choose best bucket and go direct if we want
				return new int[] {getLowestPriceInterval(loadPriceIntervals).getFirst(), getLowestPriceInterval(loadPriceIntervals).getSecond(), getHighestPriceInterval(dischargePriceIntervals).getFirst(), getHighestPriceInterval(dischargePriceIntervals).getSecond()};
			} else {
				// we are going to have to go via a canal. is it worth it?
				int loadVolumeMMBTU = OptimiserUnitConvertor.convertToExternalVolume(load.getMaxLoadVolumeMMBTU());
				int[] bestPurchaseDetails = purchaseIntervals.get(bestPurchaseDetailsIdx);
				int[] bestSalesDetails = salesIntervals.get(bestSalesDetailsIdx);
				long[] bestCanalDetails = getBestCanalDetails(sortedCanalTimes, bestSalesDetails[1] - bestPurchaseDetails[0]);
				long canalMarginPerMMBTU = bestCanalDetails[1]/loadVolumeMMBTU;
				long normalMargin = purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - salesIntervals.get(bestSalesDetailsIdx)[2];
				long bestMargin = purchaseIntervals.get(bestPurchaseDetailsIdx)[2] - salesIntervals.get(bestSalesDetailsIdx)[2] - (bestCanalDetails[1]/loadVolumeMMBTU);
				for (int purchaseIndex = bestPurchaseDetailsIdx; purchaseIndex > 0; purchaseIndex--) {
					for (int salesIndex = bestSalesDetailsIdx; salesIndex < salesIntervals.size(); salesIndex++) {
						long[] newCanalDetails = getBestCanalDetails(sortedCanalTimes, salesIntervals.get(salesIndex)[1] - purchaseIntervals.get(purchaseIndex)[0]);
						long newMargin = purchaseIntervals.get(purchaseIndex)[2] - salesIntervals.get(salesIndex)[2] - (newCanalDetails[1]/loadVolumeMMBTU);
						if (newMargin > bestMargin) {
							bestMargin = newMargin;
							bestPurchaseDetailsIdx = purchaseIndex;
							bestSalesDetailsIdx = salesIndex;
						}
					}
				}
				return new int[] {purchaseIntervals.get(bestPurchaseDetailsIdx)[0], purchaseIntervals.get(bestPurchaseDetailsIdx)[1], salesIntervals.get(bestSalesDetailsIdx)[0], salesIntervals.get(bestSalesDetailsIdx)[1]};
			}
		}
	}

	private <T> int getMinIndex(List<? extends T> coll, Comparator<? super T> comp) {
		int i = 0;
		int bestIdx = 0;
		T bestObj = coll.get(0);
		for (T o : coll) {
			if (comp.compare(o, bestObj) == -1) {
				bestObj = o;
				bestIdx = i;
			}
			i++;
		}
		return bestIdx;
	}
	
	private <T> int getMaxIndex(List<? extends T> coll, Comparator<? super T> comp) {
		int i = 0;
		int bestIdx = 0;
		T bestObj = coll.get(0);
		for (T o : coll) {
			if (comp.compare(o, bestObj) == 1) {
				bestObj = o;
				bestIdx = i;
			}
			i++;
		}
		return bestIdx;
	}
	
	private long[] getBestCanalDetails(long[][] times, int maxTime) {
		for (long[] canal : times) {
			if (maxTime >= canal[0]) {
				return canal;
			}
		}
		return times[times.length];
	}
	
	private int purchaseBucketGetNextValidIndex(List<int[]> purchaseIntervals, int currentIndex, int salesBoundary, int minTime) {
		int index = purchaseIntervals.get(currentIndex)[0];
		while (currentIndex < purchaseIntervals.size()-1) {
			currentIndex++;
			if (purchaseIntervals.get(currentIndex)[0] > index) {
				continue;
			}
			if ((salesBoundary - purchaseIntervals.get(currentIndex)[1]) >= minTime) {
				return currentIndex;
			}
		}
		return -1;
	}
	
	private int salesBucketGetNextValidIndex(List<int[]> salesIntervals, int currentIndex, int purchaseBoundary, int minTime) {
		int index = salesIntervals.get(currentIndex)[0];
		while (currentIndex < salesIntervals.size()-1) {
			currentIndex++;
			if (salesIntervals.get(currentIndex)[0] > index) {
				continue;
			}
			if ((salesIntervals.get(currentIndex)[2] - purchaseBoundary) >= minTime) {
				return currentIndex;
			}
		}
		return -1;
	}

	private int getMinTime(List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		return dischargePriceIntervals.get(0)[0] - loadPriceIntervals.get(loadPriceIntervals.size()-1)[0];
	}
	
	private int getMaxTime(List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		return dischargePriceIntervals.get(dischargePriceIntervals.size()-1)[0] - loadPriceIntervals.get(0)[0];
	}

	@NonNull
	private long[][] getMinimumTravelTimes(IPort load, IPort discharge, IVesselClass vesselClass) {
		final List<MatrixEntry<IPort, Integer>> distances = new ArrayList<MatrixEntry<IPort, Integer>>(distanceProvider.getValues(load, discharge));
		Collections.sort(distances, new Comparator<MatrixEntry<IPort, Integer>>() {
			@Override
			public int compare(MatrixEntry<IPort, Integer> o1, MatrixEntry<IPort, Integer> o2) {
				if (routeCostProvider.getRouteCost(o1.getKey(), vesselClass, VesselState.Laden)==routeCostProvider.getRouteCost(o2.getKey(), vesselClass, VesselState.Laden)) {
					return Integer.compare(Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(),o1.getValue())+routeCostProvider.getRouteTransitTime(o1.getKey(), vesselClass), Calculator.getTimeFromSpeedDistance(vesselClass.getMaxSpeed(), o2.getValue())+routeCostProvider.getRouteTransitTime(o2.getKey(), vesselClass)); 
				} else {
					return Long.compare(routeCostProvider.getRouteCost(o1.getKey(), vesselClass, VesselState.Laden),routeCostProvider.getRouteCost(o2.getKey(), vesselClass, VesselState.Laden));
				}
			}
		});
		
		long[][] times = new long[distances.size()][2];
		int i = 0;
		for (final MatrixEntry<IPort, Integer> d : distances) {
			times[i][0] = d.getValue()/(vesselClass.getMaxSpeed()) + routeCostProvider.getRouteTransitTime(d.getKey(), vesselClass); // DO NOT COMMIT (Possible conversion issues)
			times[i][1] = OptimiserUnitConvertor.convertToInternalDailyCost(routeCostProvider.getRouteCost(d.getKey(), vesselClass, VesselState.Laden));
			i++;
		}
		return times;
	}
	
	private IVesselClass getVesselClass(IResource resource) {
		IVesselAvailability availability = vesselProvider.getVesselAvailability(resource);
		if (availability == null) {
			return null;
		}
		IVessel vessel = availability.getVessel();
		if (vessel == null) {
			return null;
		}
		IVesselClass vesselClass = vessel.getVesselClass();
		return vesselClass;
	}
	
	private void trimLoadAndDischargeWindowsWithRouteChoice(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge, List<int[]> loadIntervals, List<int[]> dischargeIntervals, boolean switched) {
		IResource resource = portTimeWindowRecord.getResource();
		assert resource != null;
		IVesselClass vesselClass = getVesselClass(resource);
		assert vesselClass != null;
		int[] bounds;
		if (switched) {
			bounds = trimCargoTimeWindowsWithRouteOptimisation_LDDL(portTimeWindowRecord, vesselClass, load, discharge, loadIntervals, dischargeIntervals);
		} else {
			bounds = trimCargoTimeWindowsWithRouteOptimisation(portTimeWindowRecord, vesselClass, load, discharge, loadIntervals, dischargeIntervals);
		}
		createAndSetTimeWindow(portTimeWindowRecord, load, bounds[0], bounds[1]);
		createAndSetTimeWindow(portTimeWindowRecord, discharge, bounds[2], bounds[3]);
	}
	
	private void trimLoadWindowIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		Pair<Integer, Integer> bounds = getLowestPriceInterval(getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(),
				priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(load, portTimeWindowRecord)));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	private void trimLoadWindowBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		List<int[]> priceCurve = priceIntervalProducer.getLoadIntervalsBasedOnDischarge(load, portTimeWindowRecord);
		Pair<Integer, Integer> bounds = getHighestPriceInterval(getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceCurve));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	private void trimDischargeWindowIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		Pair<Integer, Integer> bounds = getHighestPriceInterval(getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowIndependentOfLoad(discharge, portTimeWindowRecord)));
		TimeWindow feasibleTimeWindow = new TimeWindow(bounds.getFirst(), bounds.getSecond(), timeWindow.getEndFlex());
		portTimeWindowRecord.setSlotFeasibleTimeWindow(discharge, feasibleTimeWindow);
	}

	private void trimDischargeWindowBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		Pair<Integer, Integer> bounds = getLowestPriceInterval(getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(),
				priceIntervalProducer.getDischargeWindowBasedOnLoad(discharge, portTimeWindowRecord)));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	private void loadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		List<int[]> complexPricingIntervals = getComplexPriceIntervals(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowRecord, dateFromLoad);
		Pair<Integer, Integer> bounds = getHighestPriceInterval(complexPricingIntervals);
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		if (dateFromLoad) {
			createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
		} else {
			createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
		}
	}

	/**
	 * Produce a list of the difference between purchase and sales price at hour points, when slots in a cargo are not price independent
	 * @param load
	 * @param discharge
	 * @param loadPriceIntervalProvider
	 * @param dischargePriceIntervalProvider
	 * @param portTimeWindowRecord
	 * @param dateFromLoad
	 * @return
	 */
	private List<int[]> getComplexPriceIntervals(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider, IPriceIntervalProvider dischargePriceIntervalProvider,
			IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		ITimeWindow timeWindow;
		if (dateFromLoad) {
			timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		} else {
			timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		}
	
		List<int[]> intervals = getFeasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceIntervalProducer.getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(load, discharge,
				(IPriceIntervalProvider) load.getLoadPriceCalculator(), (IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, dateFromLoad));
		return intervals;
	}

	private List<int[]> getFeasibleIntervalSubSet(int start, int end, List<int[]> intervals) {
		List<int[]> list = new LinkedList<>();
		for (int i = 0; i < intervals.size(); i++) {
			if (list.size() == 0) {
				if (intervals.get(i)[0] == start) {
					list.add(new int[] { start, intervals.get(i)[1] });
				} else if (intervals.get(i)[0] > start) {
					list.add(new int[] { start, intervals.get(i - 1)[1] });
					if (intervals.get(i)[0] < end) {
						list.add(intervals.get(i));
					}
				}
			} else if (intervals.get(i)[0] < end) {
				list.add(intervals.get(i));
			}
		}
		list.add(new int[] { end, Integer.MIN_VALUE });
		return list;
	}

	int[][] getOverlappingWindows(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider, IPriceIntervalProvider dischargePriceIntervalProvider,
			int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord) {
		List<Integer> loadIntervals = loadPriceIntervalProvider.getPriceHourIntervals(load, start, end, portTimeWindowsRecord);
		List<Integer> dischargeIntervals = dischargePriceIntervalProvider.getPriceHourIntervals(discharge, start, end, portTimeWindowsRecord);
		IntegerIntervalCurve integerIntervalCurve = new IntegerIntervalCurve();
		integerIntervalCurve.addAll(loadIntervals);
		integerIntervalCurve.addAll(dischargeIntervals);
		return integerIntervalCurve.getIntervalsAs2dArray(start, end);
	}

	PricingEventType getPriceEventTypeFromPortSlot(IPortSlot slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pricingEventType = (slot instanceof ILoadOption) ? getPriceEventFromSlotOrContract((ILoadOption) slot, portTimeWindowsRecord) : getPriceEventFromSlotOrContract(
				(IDischargeOption) slot, portTimeWindowsRecord);
		return pricingEventType;
	}

	private Pair<ILoadOption, IDischargeOption> getLoadAndDischarge(IPortTimeWindowsRecord portTimeWindowRecord, List<Triple<IPortSlot, Class<?>, PricingEventType>> slotData) {
		ILoadOption load = null;
		IDischargeOption discharge = null;
		for (Triple<IPortSlot, Class<?>, PricingEventType> slotRow : slotData) {
			if (ILoadOption.class.isAssignableFrom(slotRow.getSecond())) {
				load = (ILoadOption) slotRow.getFirst();
			} else if (IDischargeOption.class.isAssignableFrom(slotRow.getSecond())) {
				discharge = (IDischargeOption) slotRow.getFirst();
			}
		}
		return new Pair<ILoadOption, IDischargeOption>(load, discharge);
	}

	private boolean isLoadPricingEventTime(ILoadOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pet = slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent() : slot.getLoadPriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isLoadPricingEventTime(pet);
	}

	private boolean isLoadPricingEventTime(PricingEventType pet) {
		if (loadPricingEventTypeSet.contains(pet)) {
			return true;
		}
		return false;
	}

	private boolean isLoadPricingEventTime(IDischargeOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pet = slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent() : slot.getDischargePriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isLoadPricingEventTime(pet);
	}

	private boolean isDischargePricingEventTime(ILoadOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pet = slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent() : slot.getLoadPriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isDischargePricingEventTime(pet);
	}


	private boolean isDischargePricingEventTime(IDischargeOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pet = slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent() : slot.getDischargePriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isDischargePricingEventTime(pet);
	}

	private boolean isDischargePricingEventTime(PricingEventType pet) {
		if (dischargePricingEventTypeSet.contains(pet)) {
			return true;
		}
		return false;
	}

	List<int[]> buildIntervalsList(IPortSlot slot, IIntegerIntervalCurve intervals, ICurve curve, int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord, List<int[]> priceIntervals) {
		ILoadOption loadOption = getFirstLoadOption(portTimeWindowsRecord.getSlots());
		IDischargeOption dischargeOption = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
		PricingEventType pricingEventType = getPriceEventTypeFromPortSlot(slot, portTimeWindowsRecord);
		int[] hourIntervals = intervals.getIntervalRange(start, end);
		int transferDate = start;
		if (isStartOfWindow(pricingEventType)) {
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, shiftTimeByTimezoneToUTC(start, slot, portTimeWindowsRecord, pricingEventType)) });
			priceIntervals.add(getEndInterval(end));
		} else if (isEndOfWindow(pricingEventType)) {
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, shiftTimeByTimezoneToUTC(end, slot, portTimeWindowsRecord, pricingEventType)) });
			priceIntervals.add(getEndInterval(end));
		} else if (isPricingDateSpecified(slot, pricingEventType)) {
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, timeZoneToUtcOffsetProvider.UTC(getDateFromSlotOrContract(slot, portTimeWindowsRecord), slot.getPort())) });
			priceIntervals.add(getEndInterval(end));
		} else {
			// first add start
			priceIntervals.add(new int[] { start, getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, shiftTimeByTimezoneToUTC(start + (isEndOfEvent(pricingEventType) ? getDuration(slot, portTimeWindowsRecord) : 0), slot, portTimeWindowsRecord, pricingEventType)) });
			for (int h = 1; h < hourIntervals.length - 1; h++) {
				int date = hourIntervals[h];
				if (isStartOfEvent(pricingEventType)) {
					transferDate = date;
				} else if (isEndOfEvent(pricingEventType)) {
					transferDate = date - getDuration(slot, portTimeWindowsRecord);
				}
				int windowDate = shiftTimeByTimezoneToLocalTime(transferDate, slot, portTimeWindowsRecord, pricingEventType);
				if (windowDate < end && windowDate > start) {
					priceIntervals.add(new int[] { windowDate,
							getPriceFromLoadOrDischargeCalculator(slot, loadOption, dischargeOption, date) });
				}
			}
			priceIntervals.add(getEndInterval(end));
		}
		return priceIntervals;
	}

	private void createAndSetTimeWindow(IPortTimeWindowsRecord portTimeWindowRecord, IPortSlot slot, int start, int end) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(slot);
		TimeWindow feasibleTimeWindow = new TimeWindow(start, end, timeWindow.getEndFlex());
		portTimeWindowRecord.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
	}

	private int shiftTimeByTimezoneToUTC(int time, IPortSlot slot, IPortTimeWindowsRecord portTimeWindowsRecord, PricingEventType pricingEventType) {
		IPortSlot slotToUse;
		if (slot instanceof ILoadOption) {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = slot;
			} else {
				slotToUse = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
			}
		} else {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = getFirstLoadOption(portTimeWindowsRecord.getSlots());
			} else {
				slotToUse = slot;
			}
		}
		int shifted = timeZoneToUtcOffsetProvider.UTC(time, slotToUse.getPort());
		return shifted;
	}
	
	private int shiftTimeByTimezoneToLocalTime(int time, IPortSlot slot, IPortTimeWindowsRecord portTimeWindowsRecord, PricingEventType pricingEventType) {
		IPortSlot slotToUse;
		if (slot instanceof ILoadOption) {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = slot;
			} else {
				slotToUse = getFirstDischargeOption(portTimeWindowsRecord.getSlots());
			}
		} else {
			if (isLoadPricingEventTime(pricingEventType)) {
				slotToUse = getFirstLoadOption(portTimeWindowsRecord.getSlots());
			} else {
				slotToUse = slot;
			}
		}
		return timeZoneToUtcOffsetProvider.localTime(time, slotToUse.getPort());
	}
	
	private int getDateFromSlotOrContract(IPortSlot slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		int date = IPortSlot.NO_PRICING_DATE;
		if (slot instanceof ILoadOption) {
			if (((ILoadOption) slot).getLoadPriceCalculator().getCalculatorPricingEventType((ILoadOption) slot, portTimeWindowsRecord) == PricingEventType.DATE_SPECIFIED) {
				date = ((ILoadOption) slot).getLoadPriceCalculator().getCalculatorPricingDate((ILoadOption) slot, portTimeWindowsRecord);
			} else {
				date = ((ILoadOption) slot).getPricingDate();
			}
		} else if (slot instanceof IDischargeOption) {
			if (((IDischargeOption) slot).getDischargePriceCalculator().getCalculatorPricingEventType((IDischargeOption) slot, portTimeWindowsRecord) == PricingEventType.DATE_SPECIFIED) {
				date = ((IDischargeOption) slot).getDischargePriceCalculator().getCalculatorPricingDate((IDischargeOption) slot, portTimeWindowsRecord);
			} else {
				date = ((IDischargeOption) slot).getPricingDate();
			}
		}
		return date;
	}

	private boolean isPricingDateSpecified(IPortSlot portSlot, PricingEventType pricingEventType) {
		if (portSlot instanceof ILoadOption) {
			return (pricingEventType == PricingEventType.DATE_SPECIFIED || ((ILoadOption) portSlot).getPricingDate() != IPortSlot.NO_PRICING_DATE);
		} else if (portSlot instanceof IDischargeOption){
			return (pricingEventType == PricingEventType.DATE_SPECIFIED || ((IDischargeOption) portSlot).getPricingDate() != IPortSlot.NO_PRICING_DATE);			
		}
		return false;
	}

	private int getPriceFromLoadOrDischargeCalculator(IPortSlot slot, ILoadOption loadOption, IDischargeOption dischargeOption, int timeInHours) {
		if (slot instanceof ILoadOption) {
			return ((ILoadOption) slot).getLoadPriceCalculator().getEstimatedPurchasePrice(loadOption, dischargeOption, timeInHours);
		} else if (slot instanceof IDischargeOption) {
			return ((IDischargeOption) slot).getDischargePriceCalculator().getEstimatedSalesPrice(loadOption, dischargeOption, timeInHours);
		} else {
			throw new IllegalStateException("A price can only be obtained from a load or discharge option");
		}
	}

	static List<Integer> getFixedStartEndIntervals(int start, int end) {
		List<Integer> priceIntervals = new LinkedList<Integer>();
		priceIntervals.add(start);
		priceIntervals.add(end);
		return priceIntervals;
	}

	private int getShiftedDate(IPortSlot slot, IPortTimeWindowsRecord portTimeWindowsRecord, int date, PricingEventType pricingEventType) {
		int transferDate = date;
		if (isStartOfEvent(pricingEventType)) {
			transferDate = date; // no change
		} else if (isEndOfEvent(pricingEventType)) {
			int duration = getDuration(slot, portTimeWindowsRecord);
			transferDate = date - duration; // note: remove offset
		}
		return timeZoneToUtcOffsetProvider.localTime(transferDate, slot.getPort());
	}

	private int getDuration(IPortSlot slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		int duration = 0;
		if (slot instanceof ILoadOption && isLoadPricingEventTime((ILoadOption) slot, portTimeWindowsRecord)) {
			duration = portTimeWindowsRecord.getSlotDuration(slot);
		} else if (slot instanceof ILoadOption && isDischargePricingEventTime((ILoadOption) slot, portTimeWindowsRecord)) {
			duration = portTimeWindowsRecord.getSlotDuration(getFirstDischargeOption(portTimeWindowsRecord.getSlots()));
		} else if (slot instanceof IDischargeOption && isDischargePricingEventTime((IDischargeOption) slot, portTimeWindowsRecord)) {
			duration = portTimeWindowsRecord.getSlotDuration(slot);
		} else if (slot instanceof IDischargeOption && isLoadPricingEventTime((IDischargeOption) slot, portTimeWindowsRecord)) {
			duration = portTimeWindowsRecord.getSlotDuration(getFirstLoadOption(portTimeWindowsRecord.getSlots()));				
		}
		return duration;
	}

	public static final int[] getEndInterval(int end) {
		return new int[] { end, Integer.MIN_VALUE };
	}

	public static boolean isStartOfEvent(PricingEventType pet) {
		if (pet == PricingEventType.START_OF_LOAD || pet == PricingEventType.START_OF_DISCHARGE) {
			return true;
		}
		return false;
	}

	public static boolean isEndOfEvent(PricingEventType pet) {
		if (pet == PricingEventType.END_OF_LOAD || pet == PricingEventType.END_OF_DISCHARGE) {
			return true;
		}
		return false;
	}

	public static boolean isEndOfWindow(PricingEventType pet) {
		if (pet == PricingEventType.END_OF_LOAD_WINDOW || pet == PricingEventType.END_OF_DISCHARGE_WINDOW) {
			return true;
		}
		return false;
	}

	public static boolean isStartOfWindow(PricingEventType pet) {
		if (pet == PricingEventType.START_OF_LOAD_WINDOW || pet == PricingEventType.START_OF_DISCHARGE_WINDOW) {
			return true;
		}
		return false;
	}

	public static PricingEventType getPriceEventFromSlotOrContract(ILoadOption slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		return slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowRecord) == null ? slot.getPricingEvent() : slot.getLoadPriceCalculator().getCalculatorPricingEventType(
				slot, portTimeWindowRecord);
	}

	public static PricingEventType getPriceEventFromSlotOrContract(IDischargeOption slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		return slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowRecord) == null ? slot.getPricingEvent() : slot.getDischargePriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowRecord);
	}

	/**
	 * Provides the date range for the highest price period in a given range
	 * 
	 * @return
	 */
	public Pair<Integer, Integer> getHighestPriceInterval(List<int[]> intervals) {
		int start = -1;
		int end = -1;
		int best = -Integer.MAX_VALUE;
		for (int i = 0; i < intervals.size(); i++) {
			int[] currInterval = intervals.get(i);
			int price = currInterval[1];
			if (price != Integer.MIN_VALUE && price > best) {
				start = currInterval[0];
				end = intervals.get(i + 1)[0];
				best = price;
			}
		}
		return new Pair<>(start, end);
	}

	/**
	 * Provides the date range for the highest price period in a given range
	 * 
	 * @return
	 */
	public Pair<Integer, Integer> getHighestPriceInterval(IPriceIntervalProvider priceIntervalProvider, int startOfRange, int endOfRange, IPortSlot slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		return getHighestPriceInterval(priceIntervalProvider.getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord));
	}

	/**
	 * Provides the date range for the lowest price period in a given range
	 * 
	 * @return
	 */
	public Pair<Integer, Integer> getLowestPriceInterval(List<int[]> intervals) {
		int start = -1;
		int end = -1;
		int best = Integer.MAX_VALUE;
		for (int i = 0; i < intervals.size(); i++) {
			int[] currInterval = intervals.get(i);
			int price = currInterval[1];
			if (price != Integer.MIN_VALUE && price < best) {
				start = currInterval[0];
				end = intervals.get(i + 1)[0];
				best = price;
			}
		}
		return new Pair<>(start, end);
	}

	/**
	 * Provides the date range and price for the lowest price period in a given range
	 * DO NOT COMMIT (get rid of arraylist)
	 * @return
	 */
	public ArrayList<int[]> getIntervalsBoundsAndPrices(List<int[]> intervals) {
		ArrayList<int[]> sortedIntervals = new ArrayList<>(intervals.size());
		for (int i = 0; i < intervals.size()-1; i++) {
			sortedIntervals.add(new int[] {intervals.get(i)[0], intervals.get(i + 1)[0], intervals.get(i)[1]});
		}
		return sortedIntervals;
	}

	/**
	 * Provides the date range for the lowest price period in a given range
	 * 
	 * @return
	 */
	public Pair<Integer, Integer> getLowestPriceInterval(IPriceIntervalProvider priceIntervalProvider, int startOfRange, int endOfRange, IPortSlot slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		return getLowestPriceInterval(priceIntervalProvider.getPriceIntervals(slot, startOfRange, endOfRange, portTimeWindowRecord));
	}

	public ILoadOption getFirstLoadOption(List<IPortSlot> slots) {
		for (IPortSlot slot : slots) {
			if (ILoadOption.class.isAssignableFrom(slot.getClass())) {
				return (ILoadOption) slot;
			}
		}
		return null;
	}

	public IDischargeOption getFirstDischargeOption(List<IPortSlot> slots) {
		for (IPortSlot slot : slots) {
			if (IDischargeOption.class.isAssignableFrom(slot.getClass())) {
				return (IDischargeOption) slot;
			}
		}
		return null;
	}

	public boolean isPricingEventTypeLoad(PricingEventType pricingEventType) {
		if (pricingEventType == PricingEventType.START_OF_LOAD || pricingEventType == PricingEventType.END_OF_LOAD) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPricingEventTypeDischarge(PricingEventType pricingEventType) {
		if (pricingEventType == PricingEventType.START_OF_DISCHARGE || pricingEventType == PricingEventType.END_OF_DISCHARGE) {
			return true;
		} else {
			return false;
		}
	}
}
