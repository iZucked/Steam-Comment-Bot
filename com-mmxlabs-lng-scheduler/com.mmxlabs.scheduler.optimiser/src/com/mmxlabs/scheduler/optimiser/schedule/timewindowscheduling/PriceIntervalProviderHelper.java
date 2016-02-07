package com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling;

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

public class PriceIntervalProviderHelper {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	@Inject
	private IVesselProvider vesselProvider;
	
	private Set<PricingEventType> loadPricingEventTypeSet = new HashSet<>(Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_LOAD, PricingEventType.END_OF_LOAD_WINDOW,
			PricingEventType.START_OF_LOAD, PricingEventType.START_OF_LOAD_WINDOW, }));

	private Set<PricingEventType> dischargePricingEventTypeSet = new HashSet<>(Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_DISCHARGE, PricingEventType.END_OF_DISCHARGE_WINDOW,
			PricingEventType.START_OF_DISCHARGE, PricingEventType.START_OF_DISCHARGE_WINDOW, }));
	
	private PriceIntervalsComparator priceIntervalComparator = new PriceIntervalsComparator();
	
	
	/**
	 * Compares price intervals based on price
	 */
	private static final class PriceIntervalsComparator implements Comparator<int[]> {
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
	
	
	int[] getCargoBoundsWithCanalTrimming(int purchaseStart, int purchaseEnd, int salesStart, int salesEnd, int loadDuration, int minTimeCanal) {
		int minSalesStart = getMinDischargeGivenCanal(purchaseStart, salesStart, loadDuration, minTimeCanal);
		return new int[] {purchaseStart, purchaseStart, minSalesStart, Math.max(minSalesStart, salesEnd)};
	}

	public int getMinDischargeGivenCanal(int purchaseStart, int salesStart, int loadDuration, int minTimeCanal) {
		int minSalesStart = Math.max(purchaseStart + minTimeCanal + loadDuration, salesStart);
		return minSalesStart;
	}
	
	public int[] getIdealLoadAndDischargeTimesGivenCanal(int purchaseStart, int purchaseEnd, int salesStart, int salesEnd, int loadDuration, int canalMaxSpeed, int canalNBOSpeed) {
		int purchase = salesStart - canalMaxSpeed - loadDuration;
		int discharge = salesStart;
		if (purchase > purchaseEnd) {
			purchase = purchaseEnd;
			discharge = Math.min(Math.max(purchaseStart + canalNBOSpeed + loadDuration, salesStart), salesEnd);
		} else {
			int canalDifference = canalNBOSpeed - canalMaxSpeed;
			int remainder = purchase - purchaseStart - canalDifference;
			if (remainder >= 0) {
				purchase -= canalDifference;
			} else {
				purchase = purchaseStart;
				discharge = Math.min(salesStart - remainder, salesEnd); // note: remainder is -ve
			}
		}
		return new int[]{purchase, discharge};
	}

	boolean isFeasibleTravelTime(int[] purchase, int[] sales, int loadDuration, long time) {
		long minArrivalTime = purchase[0] + time + loadDuration;
		if (minArrivalTime <= sales[1]) {
			return true;
		} else {
			return false;
		}
	}

	public long[] getBestCanalDetails(int[] purchase, int[] sales, int loadDuration, long[][] sortedCanalTimes) {
		for (long[] canal : sortedCanalTimes) {
			if (isFeasibleTravelTime(purchase, sales, loadDuration, canal[0])) {
				return canal;
			}
		}
		return sortedCanalTimes[sortedCanalTimes.length - 1];
	}

	public long[] getBestCanalDetailsWithBoiloff(int[] purchase, int[] sales, int loadDuration, int salesPrice, long[][] sortedCanalTimes, long boiloffRateM3, int cv, int loadVolumeMMBTU) {
		long bestMargin = Long.MAX_VALUE;
		long bestBoiloffCostMMBTU = Long.MIN_VALUE;
		long[] bestCanal = null;
		for (long[] canal : sortedCanalTimes) {
			if (isFeasibleTravelTime(purchase, sales, loadDuration, canal[0])) {
				long boiloffMMBTU = Calculator.convertM3ToMMBTu(((getMinDischargeGivenCanal(purchase[0], sales[0], loadDuration, (int) canal[0])-purchase[0]-loadDuration)/24)*boiloffRateM3, cv);
				long boiloffCost = Calculator.costFromVolume(boiloffMMBTU, sales[2]);
				long boiloffCostMMBTU = OptimiserUnitConvertor.convertToInternalDailyCost(boiloffCost);
				long cost = canal[1] + boiloffCostMMBTU;
				if (cost < bestMargin) {
					bestMargin = cost;
					bestCanal = canal;
					bestBoiloffCostMMBTU = boiloffCostMMBTU;
				}
			}
		}
		return new long[] {bestCanal[0], bestCanal[2], bestCanal[1], bestBoiloffCostMMBTU}; // TODO: this ordering is stupid now!
	}

	int getMinIndexOfPriceIntervalList(List<int[]> list) {
		return getMinIndex(list, priceIntervalComparator);
	}
	
	private static <T> int getMinIndex(List<? extends T> coll, Comparator<? super T> comp) {
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
	
	int getMaxIndexOfPriceIntervalList(List<int[]> list) {
		return getMaxIndex(list, priceIntervalComparator);
	}

	private static <T> int getMaxIndex(List<? extends T> coll, Comparator<? super T> comp) {
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

	long[] getBestCanalDetails(long[][] times, int maxTime) {
		for (long[] canal : times) {
			if (maxTime >= canal[0]) {
				return canal;
			}
		}
		return times[times.length];
	}
	
	int getMinimumPossibleTimeForCargoIntervals(List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		return dischargePriceIntervals.get(0)[0] - loadPriceIntervals.get(loadPriceIntervals.size()-1)[0];
	}
	
	int getMaximumPossibleTimeForCargoIntervals(List<int[]> loadPriceIntervals, List<int[]> dischargePriceIntervals) {
		return dischargePriceIntervals.get(dischargePriceIntervals.size()-1)[0] - loadPriceIntervals.get(0)[0];
	}

	IVessel getVessel(IResource resource) {
		IVesselAvailability availability = vesselProvider.getVesselAvailability(resource);
		if (availability == null) {
			return null;
		}
		IVessel vessel = availability.getVessel();
		if (vessel == null) {
			return null;
		}
		return vessel;
	}

	IVesselClass getVesselClass(IResource resource) {
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
	List<int[]> getComplexPriceIntervals(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider, IPriceIntervalProvider dischargePriceIntervalProvider,
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

	@NonNull List<int[]> getFeasibleIntervalSubSet(int start, int end, List<int[]> intervals) {
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

	Pair<ILoadOption, IDischargeOption> getLoadAndDischarge(IPortTimeWindowsRecord portTimeWindowRecord, List<Triple<IPortSlot, Class<?>, PricingEventType>> slotData) {
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

	boolean isLoadPricingEventTime(ILoadOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
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

	boolean isLoadPricingEventTime(IDischargeOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pet = slot.getDischargePriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent() : slot.getDischargePriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isLoadPricingEventTime(pet);
	}

	boolean isDischargePricingEventTime(ILoadOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
		PricingEventType pet = slot.getLoadPriceCalculator().getCalculatorPricingEventType(slot, portTimeWindowsRecord) == null ? slot.getPricingEvent() : slot.getLoadPriceCalculator()
				.getCalculatorPricingEventType(slot, portTimeWindowsRecord);
		return isDischargePricingEventTime(pet);
	}


	boolean isDischargePricingEventTime(IDischargeOption slot, IPortTimeWindowsRecord portTimeWindowsRecord) {
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

	void createAndSetTimeWindow(IPortTimeWindowsRecord portTimeWindowRecord, IPortSlot slot, int start, int end) {
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

	boolean isPricingDateSpecified(IPortSlot portSlot, PricingEventType pricingEventType) {
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
	 * @return
	 */
	public List<int[]> getIntervalsBoundsAndPrices(List<int[]> intervals) {
		List<int[]> sortedIntervals = new LinkedList<>();
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
