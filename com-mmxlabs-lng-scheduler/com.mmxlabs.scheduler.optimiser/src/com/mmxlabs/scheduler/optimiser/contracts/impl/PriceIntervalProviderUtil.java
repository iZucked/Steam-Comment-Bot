package com.mmxlabs.scheduler.optimiser.contracts.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;

import com.google.inject.Inject;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.curves.ICurve;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.optimiser.common.components.impl.TimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.curves.IIntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer;
import com.mmxlabs.scheduler.optimiser.curves.IntegerIntervalCurve;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class PriceIntervalProviderUtil {

	@Inject
	private ITimeZoneToUtcOffsetProvider timeZoneToUtcOffsetProvider;

	@Inject
	private IPriceIntervalProducer priceIntervalProducer;

	private Set<PricingEventType> loadPricingEventTypeSet = new HashSet<>(Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_LOAD, PricingEventType.END_OF_LOAD_WINDOW,
			PricingEventType.START_OF_LOAD, PricingEventType.START_OF_LOAD_WINDOW, }));

	private Set<PricingEventType> dischargePricingEventTypeSet = new HashSet<>(Arrays.asList(new PricingEventType[] { PricingEventType.END_OF_DISCHARGE, PricingEventType.END_OF_DISCHARGE_WINDOW,
			PricingEventType.START_OF_DISCHARGE, PricingEventType.START_OF_DISCHARGE_WINDOW, }));

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
					if (!isLoadPricingEventTime((ILoadOption) slotRow.getFirst())) {
						throw new IllegalStateException("Complex cargoes must not have complex pricing event dates");
					}
				} else if (slotRow.getSecond().isAssignableFrom(IDischargeOption.class)) {
					if (!isDischargePricingEventTime((IDischargeOption) slotRow.getFirst())) {
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

			// if (a == startOfDischarge and b == startOfLoad) {
			// get both sets of price intervals
			// iterate them
			// }
			Pair<ILoadOption, IDischargeOption> slots = getLoadAndDischarge(portTimeWindowRecord, slotData);
			ILoadOption load = slots.getFirst();
			IDischargeOption discharge = slots.getSecond();
			if (load != null && discharge != null) {
				// TODO: what about starts, etc.?
				if (isLoadPricingEventTime(load) && isDischargePricingEventTime(discharge)) {
					// simplest case
					if (load.getLoadPriceCalculator() instanceof IPriceIntervalProvider) {
						trimLoadWindowIndependentOfDischarge(portTimeWindowRecord, load);
					}
					if (discharge.getDischargePriceCalculator() instanceof IPriceIntervalProvider) {
						trimDischargeWindowIndependentOfLoad(portTimeWindowRecord, discharge);
					}
				} else if (isDischargePricingEventTime(load) && isLoadPricingEventTime(discharge)) {
					// complex case (L -> D; D -> L)
					trimLoadWindowBasedOnDischarge(portTimeWindowRecord, load, discharge); // DON NOT COMMIT (check for IPriceIntervalProvider)
					trimDischargeWindowBasedOnLoad(portTimeWindowRecord, load, discharge);
				} else if (isDischargePricingEventTime(load) && isDischargePricingEventTime(discharge)) {
					// complex case (L -> D; D -> D)
					loadOrDischargeDeterminesBothPricingEvents(load, discharge, ((IPriceIntervalProvider) load.getLoadPriceCalculator()),
							((IPriceIntervalProvider) discharge.getDischargePriceCalculator()), portTimeWindowRecord, false);
				} else if (isLoadPricingEventTime(load) && isLoadPricingEventTime(discharge)) {
					// complex case (L -> L; D -> L)
					loadOrDischargeDeterminesBothPricingEvents(load, discharge, ((IPriceIntervalProvider) load.getLoadPriceCalculator()),
							((IPriceIntervalProvider) discharge.getDischargePriceCalculator()), portTimeWindowRecord, true);
				}
			}
		}
		return portTimeWindowRecord;
	}

	private void trimLoadWindowIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		// Pair<Integer, Integer> bounds = ((IPriceIntervalProvider) load.getLoadPriceCalculator()).getLowestPriceInterval(timeWindow.getStart(), timeWindow.getEnd(), load, null,
		// portTimeWindowRecord);
		Pair<Integer, Integer> bounds = getLowestPriceInterval(feasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceIntervalProducer.getLoadIntervalsIndependentOfDischarge(portTimeWindowRecord, load)));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		createAndSetTimeWindow(portTimeWindowRecord, load, start, end); // DON NOT COMMIT (do we need to do this?)
	}

	private void trimLoadWindowBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		// Pair<Integer, Integer> bounds = ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getHighestPriceInterval(timeWindow.getStart(), timeWindow.getEnd(), null, discharge,
		// portTimeWindowRecord);
		Pair<Integer, Integer> bounds = getHighestPriceInterval(feasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceIntervalProducer.getLoadIntervalsBasedOnDischarge(portTimeWindowRecord, load, discharge)));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
	}

	private List<int[]> feasibleIntervalSubSet(int start, int end, List<int[]> intervals) {
		List<int[]> list = new LinkedList<>();
		for (int i = 0; i < intervals.size(); i++) {
			if (list.size() == 0) {
				if (intervals.get(i)[0] == start) {
					list.add(new int[] { start, intervals.get(i)[1] });
				} else if (intervals.get(i)[0] > start) {
					list.add(new int[] { start, intervals.get(i - 1)[1] });
					list.add(intervals.get(i));
				}
			} else if (intervals.get(i)[0] < end) {
				list.add(intervals.get(i));
			}
		}
		list.add(new int[] {end, Integer.MIN_VALUE});
		return list;
	}

	private void trimDischargeWindowIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		// Pair<Integer, Integer> bounds = ((IPriceIntervalProvider) discharge.getDischargePriceCalculator()).getHighestPriceInterval(timeWindow.getStart(), timeWindow.getEnd(), null, discharge,
		// portTimeWindowRecord);
		Pair<Integer, Integer> bounds = getHighestPriceInterval(feasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceIntervalProducer.getDischargeWindowIndependentOfLoad(portTimeWindowRecord, discharge)));
		TimeWindow feasibleTimeWindow = new TimeWindow(bounds.getFirst(), bounds.getSecond(), timeWindow.getEndFlex());
		portTimeWindowRecord.setSlotFeasibleTimeWindow(discharge, feasibleTimeWindow);
	}

	private void trimDischargeWindowBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		// Pair<Integer, Integer> bounds = ((IPriceIntervalProvider) load.getLoadPriceCalculator()).getLowestPriceInterval(timeWindow.getStart(), timeWindow.getEnd(), load, null,
		// portTimeWindowRecord);
		Pair<Integer, Integer> bounds = getLowestPriceInterval(feasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceIntervalProducer.getDischargeWindowBasedOnLoad(portTimeWindowRecord, load, discharge)));
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
	}

	private void createAndSetTimeWindow(IPortTimeWindowsRecord portTimeWindowRecord, IPortSlot slot, int start, int end) {
		ITimeWindow timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(slot);
		TimeWindow feasibleTimeWindow = new TimeWindow(start, end, timeWindow.getEndFlex());
		portTimeWindowRecord.setSlotFeasibleTimeWindow(slot, feasibleTimeWindow);
	}

	private void loadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		//TODO: make feasible based on load or discharge
		// DO NOT COMMIT - clean up
		List<int[]> complexPricingIntervals = getComplexPriceIntervals(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowRecord, dateFromLoad);
		Pair<Integer, Integer> bounds = getHighestPriceInterval(complexPricingIntervals);
		int start = bounds.getFirst();
		int end = bounds.getSecond();
		//		int start = complexPricingIntervals.get(0)[0];
//		int end = complexPricingIntervals.get(0)[1];
//		for (int i = 1; i < complexPricingIntervals.size(); i++) {
//			if (end == complexPricingIntervals.get(i)[0]) {
//				end = complexPricingIntervals.get(i)[1];
//			} else {
//				break;
//			}
//		}
		if (dateFromLoad) {
			createAndSetTimeWindow(portTimeWindowRecord, load, start, end);
		} else {
			createAndSetTimeWindow(portTimeWindowRecord, discharge, start, end);
		}
	}

	public List<int[]> getComplexPriceIntervals(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider, IPriceIntervalProvider dischargePriceIntervalProvider,
			IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		ITimeWindow timeWindow;
		if (dateFromLoad) {
			timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(load);
		} else {
			timeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(discharge);
		}

		List<int[]> intervals = feasibleIntervalSubSet(timeWindow.getStart(), timeWindow.getEnd(), priceIntervalProducer.getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(load, discharge, (IPriceIntervalProvider) load.getLoadPriceCalculator(), (IPriceIntervalProvider) discharge.getDischargePriceCalculator(), portTimeWindowRecord, dateFromLoad));
		return intervals;
	}

	public List<int[]> buildComplexPriceIntervals(int start, int end, ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider, IPriceIntervalProvider dischargePriceIntervalProvider,
			IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		int[][] intervals = getOverlappingWindows(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, start, end, portTimeWindowRecord); // DO NOT COMMIT - convert to UTC
		long bestPrice = Long.MIN_VALUE;
		List<int[]> bestIntervals = new LinkedList<>();
		for (int[] interval : intervals) {
			int purchasePrice = load.getLoadPriceCalculator().getEstimatedPurchasePrice(load, getShiftedDateForLoadBasedOnDischarge(load, discharge, portTimeWindowRecord, interval[0]));
			int salesPrice = discharge.getDischargePriceCalculator().getEstimatedSalesPrice(discharge, getShiftedDateForDischargeBasedOnLoad(load, discharge, portTimeWindowRecord, interval[0]));
			int difference = salesPrice - purchasePrice;
			bestIntervals.add(new int[]{interval[0], difference});
		}
		return bestIntervals;
	}
	
	private int[][] getOverlappingWindows(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider, IPriceIntervalProvider dischargePriceIntervalProvider,
			int start, int end, IPortTimeWindowsRecord portTimeWindowsRecord) {
		List<Integer> loadIntervals = loadPriceIntervalProvider.getPriceHourIntervals(load, start, end, portTimeWindowsRecord);
		List<Integer> dischargeIntervals = dischargePriceIntervalProvider.getPriceHourIntervals(discharge, start, end, portTimeWindowsRecord); //DON NOT COMMIT - problem with discharge time shifting!
		IntegerIntervalCurve integerIntervalCurve = new IntegerIntervalCurve();
		integerIntervalCurve.addAll(loadIntervals);
		integerIntervalCurve.addAll(dischargeIntervals);
		return integerIntervalCurve.getIntervalsAs2dArray(start, end);
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

	private boolean isLoadPricingEventTime(ILoadOption slot) {
		// throw new IllegalStateException("Not implemented full method");
		PricingEventType pet = slot.getLoadPriceCalculator().getCalculatorPricingEventType(l, d) == null ? slot.getPricingEvent() : slot.getLoadPriceCalculator().getCalculatorPricingEventType(l, d);
		if (loadPricingEventTypeSet.contains(pet)) {
			return true;
		}
		return false;
	}

	private boolean isLoadPricingEventTime(IDischargeOption slot) {
		// throw new IllegalStateException("Not implemented full method");

		if (loadPricingEventTypeSet.contains(slot.getPricingEvent())) {
			return true;
		}
		return false;
	}

	private boolean isDischargePricingEventTime(ILoadOption slot) {
		// throw new IllegalStateException("Not implemented full method");

		if (dischargePricingEventTypeSet.contains(slot.getPricingEvent())) {
			return true;
		}
		return false;
	}

	private boolean isDischargePricingEventTime(IDischargeOption slot) {
		// throw new IllegalStateException("Not implemented full method");

		if (dischargePricingEventTypeSet.contains(slot.getPricingEvent())) {
			return true; // DON NOT COMMIT
		}
		return false;
	}

	public List<int[]> getPriceIntervalsList(IIntegerIntervalCurve intervals, ICurve curve, int start, int end, int offsetInHours, ILoadOption slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		List<int[]> priceIntervals = new LinkedList<>();
		buildIntervalsList(intervals, curve, start, end, slot, getPriceEventFromSlotOrContract(slot), portTimeWindowRecord, priceIntervals); // DON NOT COMMIT (what about contract pricing event)
		return priceIntervals;
	}

	public List<int[]> getPriceIntervalsList(IIntegerIntervalCurve intervals, ICurve curve, int start, int end, int offsetInHours, IDischargeOption slot, IPortTimeWindowsRecord portTimeWindowRecord) {
		List<int[]> priceIntervals = new LinkedList<>();
		buildIntervalsList(intervals, curve, start, end, slot, getPriceEventFromSlotOrContract(slot), portTimeWindowRecord, priceIntervals); // DON NOT COMMIT (what about contract pricing event)
		return priceIntervals;
	}

	private void buildIntervalsList(IIntegerIntervalCurve intervals, ICurve curve, int start, int end, IPortSlot slot, PricingEventType pricingEventType, IPortTimeWindowsRecord portTimeWindowRecord,
			List<int[]> priceIntervals) {
		int[] hourIntervals = intervals.getIntervalRange(start, end);
		int transferDate = start;
		if (isStartOfWindow(pricingEventType)) {
			priceIntervals.add(new int[] { start, curve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(start, slot.getPort())) });
			priceIntervals.add(getEndInterval(end));
		} else if (isEndOfWindow(pricingEventType)) {
			priceIntervals.add(new int[] { end, curve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(end, slot.getPort())) });
			priceIntervals.add(getEndInterval(end));
		} else {
			priceIntervals.add(new int[] { start, curve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(start, slot.getPort())) });
			for (int h = 1; h < hourIntervals.length - 1; h++) {
				int date = hourIntervals[h];
				if (isStartOfEvent(pricingEventType)) {
					transferDate = date;
				} else if (isEndOfEvent(pricingEventType)) {
					transferDate = date + portTimeWindowRecord.getSlotDuration(slot);
				}
				if (timeZoneToUtcOffsetProvider.localTime(transferDate, slot.getPort()) < end) {
					priceIntervals.add(new int[] { timeZoneToUtcOffsetProvider.localTime(transferDate, slot.getPort()),
							curve.getValueAtPoint(timeZoneToUtcOffsetProvider.UTC(transferDate, slot.getPort())) });
				}
			}
			priceIntervals.add(getEndInterval(end));
		}
	}

	public List<Integer> buildDateChangeCurveAsIntegerList(int start, int end, IPortSlot slot, int[] intervals, IPortTimeWindowsRecord portTimeWindowsRecord, PricingEventType pricingEventType) {
		// DO NOT COMMIT - clean up and get rid of PriceEventType
		List<Integer> shifted = new LinkedList<>();
		if (isStartOfWindow(pricingEventType)) {
			shifted.add(start);
		} else if (isEndOfWindow(pricingEventType)) {
			shifted.add(end);
		} else {
			shifted.add(start);
			for (int h = 1; h < intervals.length - 1; h++) {
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
			transferDate = date - portTimeWindowsRecord.getSlotDuration(slot); // note: remove offset
		}
		return timeZoneToUtcOffsetProvider.localTime(transferDate, slot.getPort());
	}

	/**
	 * Shifts the date for a load object that depends on discharge date
	 * @param load
	 * @param discharge
	 * @param portTimeWindowsRecord
	 * @param date
	 * @return
	 */
	private int getShiftedDateForLoadBasedOnDischarge(ILoadOption load, IDischargeOption discharge, IPortTimeWindowsRecord portTimeWindowsRecord, int date) {
		int transferDate = date;
		if (isStartOfEvent(getPriceEventFromSlotOrContract(load))) {
			transferDate = date; // no change
		} else if (isEndOfEvent(getPriceEventFromSlotOrContract(load))) {
			transferDate = date + portTimeWindowsRecord.getSlotDuration(discharge); // note: added offset to DISCHARGE
		}
		return timeZoneToUtcOffsetProvider.UTC(transferDate, discharge.getPort());
	}

	/**
	 * Shifts the date for a discharge object that depends on load date
	 * @param load
	 * @param discharge
	 * @param portTimeWindowsRecord
	 * @param date
	 * @return
	 */
	private int getShiftedDateForDischargeBasedOnLoad(ILoadOption load, IDischargeOption discharge, IPortTimeWindowsRecord portTimeWindowsRecord, int date) {
		int transferDate = date;
		if (isStartOfEvent(getPriceEventFromSlotOrContract(discharge))) {
			transferDate = date; // no change
		} else if (isEndOfEvent(getPriceEventFromSlotOrContract(discharge))) {
			transferDate = date + portTimeWindowsRecord.getSlotDuration(load); // note: remove offset from LOAD
		}
		return timeZoneToUtcOffsetProvider.UTC(transferDate, discharge.getPort());
	}

	public int[] getIntervalRangePricingTime(int[] intervals, String tz, int additionalOffset) {
		int[] newIntervals = new int[intervals.length];
		for (int idx = 0; idx < newIntervals.length; idx++) {
			newIntervals[idx] = convertTimeToLocal(intervals[idx], tz, additionalOffset);
		}
		return newIntervals;
	}

	public int[] getIntervalRangeSchedulingTime(int[] intervals, String tz, int additionalOffset) {
		int[] newIntervals = new int[intervals.length];
		for (int idx = 0; idx < newIntervals.length; idx++) {
			newIntervals[idx] = convertTimeToUTC(intervals[idx], tz, additionalOffset);
		}
		return newIntervals;
	}

	private int[] getEndInterval(int end) {
		return new int[] { end, Integer.MIN_VALUE };
	}

	private int convertTimeToLocal(int utcTime, String tz, int additionalOffset) {
		return timeZoneToUtcOffsetProvider.localTime(utcTime, tz) + additionalOffset; // note: adding additionalOffset
	}

	private int convertTimeToUTC(int localTime, String tz, int additionalOffset) {
		return timeZoneToUtcOffsetProvider.UTC(localTime, tz) - additionalOffset; // note: subtracting additionalOffset
	}

	public static boolean isStartOfEvent(PricingEventType pet) {
		if (pet == PricingEventType.START_OF_LOAD || pet == PricingEventType.START_OF_DISCHARGE) {
			return true;
		}
		return false;
	}

	public static boolean isStartOfEvent(ILoadOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.START_OF_LOAD) {
			return true;
		}
		return false;
	}

	public static boolean isStartOfEvent(IDischargeOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.START_OF_DISCHARGE) {
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

	public static boolean isEndOfEvent(ILoadOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.END_OF_LOAD) {
			return true;
		}
		return false;
	}

	public static boolean isEndOfEvent(IDischargeOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.END_OF_DISCHARGE) {
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

	public static boolean isEndOfWindow(ILoadOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.END_OF_LOAD_WINDOW) {
			return true;
		}
		return false;
	}

	public static boolean isEndOfWindow(IDischargeOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.END_OF_DISCHARGE_WINDOW) {
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

	public static boolean isStartOfWindow(ILoadOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.START_OF_LOAD_WINDOW) {
			return true;
		}
		return false;
	}

	public static boolean isStartOfWindow(IDischargeOption slot) {
		if (getPriceEventFromSlotOrContract(slot) == PricingEventType.START_OF_DISCHARGE_WINDOW) {
			return true;
		}
		return false;
	}

	public static PricingEventType getPriceEventFromSlotOrContract(ILoadOption slot) {
		return slot.getLoadPriceCalculator().getCalculatorPricingEventType(l, d) == null ? slot.getPricingEvent() : slot.getLoadPriceCalculator().getCalculatorPricingEventType(l, d);
	}

	public static PricingEventType getPriceEventFromSlotOrContract(IDischargeOption slot) {
		return slot.getDischargePriceCalculator().getCalculatorPricingEventType(l, d) == null ? slot.getPricingEvent() : slot.getDischargePriceCalculator().getCalculatorPricingEventType(l, d);
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
	public Pair<Integer, Integer> getHighestPriceInterval(IPriceIntervalProvider priceIntervalProvider, int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption,
			IPortTimeWindowsRecord portTimeWindowRecord) {
		return getHighestPriceInterval(priceIntervalProvider.getPriceIntervals(startOfRange, endOfRange, loadOption, dischargeOption, portTimeWindowRecord));
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
	 * Provides the date range for the lowest price period in a given range
	 * 
	 * @return
	 */
	public Pair<Integer, Integer> getLowestPriceInterval(IPriceIntervalProvider priceIntervalProvider, int startOfRange, int endOfRange, ILoadOption loadOption, IDischargeOption dischargeOption,
			IPortTimeWindowsRecord portTimeWindowRecord) {
		return getLowestPriceInterval(priceIntervalProvider.getPriceIntervals(startOfRange, endOfRange, loadOption, dischargeOption, portTimeWindowRecord));
	}
	
	private ILoadOption getFirstLoadOption(List<IPortSlot> slots) {
		for (IPortSlot slot : slots) {
			if (slot instanceof ILoadOption) {
				return (ILoadOption) slot;
			}
		}
		return null;
	}
	
	private IDischargeOption getFirstDischargeOption(List<IPortSlot> slots) {
		for (IPortSlot slot : slots) {
			if (slot instanceof IDischargeOption) {
				return (IDischargeOption) slot;
			}
		}
		return null;
	}

	private boolean isPricingEventTypeLoad(PricingEventType pricingEventType) {
		if (pricingEventType == PricingEventType.START_OF_LOAD || pricingEventType == PricingEventType.END_OF_LOAD) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isPricingEventTypeDischarge(PricingEventType pricingEventType) {
		if (pricingEventType == PricingEventType.START_OF_DISCHARGE || pricingEventType == PricingEventType.END_OF_DISCHARGE) {
			return true;
		} else {
			return false;
		}
	}
}
