/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.Equality;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.components.PricingEventType;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * A caching version of the {@link IPriceIntervalProducer}
 * @author achurchill
 *
 */
public class CachingPriceIntervalProducer implements IPriceIntervalProducer {

	@Inject
	PriceIntervalProviderHelper priceIntervalProviderUtil;

	private final class CacheKey {

		protected final int startTime;
		protected final int endTime;
		protected final IPortSlot[] determiningSlots;
		protected final IPortSlot[] allSlots;
		public CacheKey(int start, int end, IPortSlot[] determiningSlots, IPortSlot[] allSlots) {
			this.startTime = start;
			this.endTime = end;
			this.determiningSlots = determiningSlots;
			this.allSlots = allSlots;
		}

		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + startTime;
			result = (prime * result) + endTime;
			result = (prime * result) + Arrays.hashCode(determiningSlots);
			result = (prime * result) + Arrays.hashCode(allSlots);

			return result;
		}

		/**
		 */
		@Override
		public final boolean equals(final Object obj) {
			if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;
				return this.startTime == other.startTime && this.endTime == other.endTime && Equality.shallowEquals(this.determiningSlots, other.determiningSlots)
						&& Equality.shallowEquals(this.allSlots, other.allSlots);
			}
			return false;
		}

	}

	private final class Entry {
		protected final int startTime;
		protected final int endTime;
		protected final IPortSlot[] determiningSlots;
		protected final IPortSlot[] allSlots;
		public final CacheKey cacheKey;
		public List<int[]> result = null;
		
		public Entry(int startTime, int endTime, IPortSlot[] determiningSlots, IPortSlot[] allSlots) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.determiningSlots = determiningSlots;
			this.allSlots = allSlots;
			this.cacheKey = new CacheKey(startTime, endTime, determiningSlots, allSlots);
		}
	}

	IPriceIntervalProducer delegate;
	HashMap<CacheKey, List<int[]>> cache = new HashMap<>(800);

	public CachingPriceIntervalProducer(IPriceIntervalProducer delegate) {
		this.delegate = delegate;
	}

	@Override
	public List<int[]> getLoadIntervalsIndependentOfDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		Entry entry = getCacheEntry(start, end, new IPortSlot[] {portSlot}, portTimeWindowRecord);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getLoadIntervalsIndependentOfDischarge(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int[]> getLoadIntervalsBasedOnDischarge(ILoadOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());
		
		Entry entry = getCacheEntry(start, end, new IPortSlot[] {portSlot}, portTimeWindowRecord);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getLoadIntervalsBasedOnDischarge(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int[]> getDischargeWindowIndependentOfLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		Entry entry = getCacheEntry(start, end, new IPortSlot[] {portSlot}, portTimeWindowRecord);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getDischargeWindowIndependentOfLoad(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int[]> getDischargeWindowBasedOnLoad(IDischargeOption portSlot, IPortTimeWindowsRecord portTimeWindowRecord) {
		int start = portSlot.getTimeWindow().getStart();
		ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		int end = findBestEnd(start, feasibletimeWindow.getStart(), portSlot.getTimeWindow().getEnd(), feasibletimeWindow.getEnd());

		Entry entry = getCacheEntry(start, end, new IPortSlot[] {portSlot}, portTimeWindowRecord);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getDischargeWindowBasedOnLoad(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowsRecord, boolean dateFromLoad) {
		int start = load.getTimeWindow().getStart();
		ITimeWindow loadFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		ITimeWindow dischargeFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		int end = findBestEnd(start, loadFeasibletimeWindow.getStart(), discharge.getTimeWindow().getEnd(), dischargeFeasibletimeWindow.getEnd());
		
		Entry entry = getCacheEntry(start, end, new IPortSlot[] {load, discharge}, portTimeWindowsRecord);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowsRecord, dateFromLoad);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public void reset() {
		cache.clear();
	}

	@Override
	public void dispose() {
		cache.clear();
	}

	private Entry getCacheEntry(int start, int end, IPortSlot[] determiningSlots, IPortTimeWindowsRecord portTimeWindowsRecord) {
		return new Entry(start, end, determiningSlots,
				portTimeWindowsRecord.getSlots().toArray(new IPortSlot[portTimeWindowsRecord.getSlots().size()]));
	}
	
	/**
	 * Return the max of the window end and the feasbile end (if late). Add 1 hour if start == end
	 * @param windowStart
	 * @param feasibleStart
	 * @param windowEnd
	 * @param feasibleEnd
	 * @return
	 */
	private int findBestEnd(int windowStart, int feasibleStart, int windowEnd, int feasibleEnd) {
		int maxEnd = Math.max(windowEnd, feasibleEnd);
		if (windowStart == maxEnd || feasibleStart == maxEnd) {
			maxEnd += 1;
		}
		return maxEnd + 1;
	}

}