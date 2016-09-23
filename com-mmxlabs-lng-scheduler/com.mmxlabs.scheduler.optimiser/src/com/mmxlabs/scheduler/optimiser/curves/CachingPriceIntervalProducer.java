/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.curves;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.google.inject.Inject;
import com.mmxlabs.common.Equality;
import com.mmxlabs.optimiser.common.components.ITimeWindow;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.IPortSlot;
import com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider;
import com.mmxlabs.scheduler.optimiser.schedule.timewindowscheduling.PriceIntervalProviderHelper;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

/**
 * A caching version of the {@link IPriceIntervalProducer}
 * 
 * @author achurchill
 *
 */
public class CachingPriceIntervalProducer implements IPriceIntervalProducer {

	@Inject
	private PriceIntervalProviderHelper priceIntervalProviderUtil;

	private final class CacheKey {

		protected final int startTime;
		protected final int endTime;
		protected final IPortSlot[] determiningSlots;
		protected final IPortSlot[] allSlots;

		public CacheKey(final int start, final int end, final IPortSlot[] determiningSlots, final IPortSlot[] allSlots) {
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
		// FIXME: This is never set to anything
		public List<int[]> result = null;

		public Entry(final int startTime, final int endTime, final IPortSlot[] determiningSlots, final IPortSlot[] allSlots) {
			this.startTime = startTime;
			this.endTime = endTime;
			this.determiningSlots = determiningSlots;
			this.allSlots = allSlots;
			this.cacheKey = new CacheKey(startTime, endTime, determiningSlots, allSlots);
		}
	}

	private IPriceIntervalProducer delegate;
	private HashMap<CacheKey, List<int[]>> cache = new HashMap<>(800);

	public CachingPriceIntervalProducer(final IPriceIntervalProducer delegate) {
		this.delegate = delegate;
	}

	@Override
	public @NonNull List<int @NonNull []> getLoadIntervalsIndependentOfDischarge(final ILoadOption portSlot, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final int inclusiveWindowStart = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(inclusiveWindowStart, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());

		final Entry entry = getCacheEntry(inclusiveWindowStart, end, new IPortSlot[] { portSlot }, portTimeWindowRecord);
		if (entry.result == null) {
			@NonNull
			final List<int @NonNull []> intervals = delegate.getLoadIntervalsIndependentOfDischarge(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public @NonNull List<int @NonNull []> getLoadIntervalsBasedOnDischarge(final ILoadOption portSlot, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final int inclusiveWindowStart = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(inclusiveWindowStart, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());

		final Entry entry = getCacheEntry(inclusiveWindowStart, end, new IPortSlot[] { portSlot }, portTimeWindowRecord);
		if (entry.result == null) {
			final List<int[]> intervals = delegate.getLoadIntervalsBasedOnDischarge(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int @NonNull []> getDischargeWindowIndependentOfLoad(final IDischargeOption portSlot, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final int inclusiveWindowStart = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(inclusiveWindowStart, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());

		final Entry entry = getCacheEntry(inclusiveWindowStart, end, new IPortSlot[] { portSlot }, portTimeWindowRecord);
		if (entry.result == null) {
			final List<int[]> intervals = delegate.getDischargeWindowIndependentOfLoad(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int @NonNull []> getDischargeWindowBasedOnLoad(final IDischargeOption portSlot, final IPortTimeWindowsRecord portTimeWindowRecord) {
		final int inclusiveWindowStart = portSlot.getTimeWindow().getInclusiveStart();
		final ITimeWindow feasibletimeWindow = portTimeWindowRecord.getSlotFeasibleTimeWindow(portSlot);
		final int end = findBestEnd(inclusiveWindowStart, feasibletimeWindow.getInclusiveStart(), portSlot.getTimeWindow().getExclusiveEnd(), feasibletimeWindow.getExclusiveEnd());

		final Entry entry = getCacheEntry(inclusiveWindowStart, end, new IPortSlot[] { portSlot }, portTimeWindowRecord);
		if (entry.result == null) {
			final List<int[]> intervals = delegate.getDischargeWindowBasedOnLoad(portSlot, portTimeWindowRecord);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	@Override
	public List<int @NonNull []> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(final ILoadOption load, final IDischargeOption discharge,
			final IPriceIntervalProvider loadPriceIntervalProvider, final IPriceIntervalProvider dischargePriceIntervalProvider, final IPortTimeWindowsRecord portTimeWindowsRecord,
			final boolean dateFromLoad) {
		final int inclusiveWindowStart = load.getTimeWindow().getInclusiveStart();
		final ITimeWindow loadFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(load);
		final ITimeWindow dischargeFeasibletimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(discharge);
		final int end = findBestEnd(inclusiveWindowStart, loadFeasibletimeWindow.getInclusiveStart(), discharge.getTimeWindow().getExclusiveEnd(), dischargeFeasibletimeWindow.getExclusiveEnd());

		final Entry entry = getCacheEntry(inclusiveWindowStart, end, new IPortSlot[] { load, discharge }, portTimeWindowsRecord);
		if (entry.result == null) {
			final List<int[]> intervals = delegate.getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider,
					portTimeWindowsRecord, dateFromLoad);
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

	private Entry getCacheEntry(final int start, final int end, final IPortSlot[] determiningSlots, final IPortTimeWindowsRecord portTimeWindowsRecord) {
		return new Entry(start, end, determiningSlots, portTimeWindowsRecord.getSlots().toArray(new IPortSlot[portTimeWindowsRecord.getSlots().size()]));
	}

	/**
	 * Return the max of the window end and the feasbile end (if late). Add 1 hour if start == end
	 * 
	 * @param windowStart
	 * @param feasibleStart
	 * @param windowEnd
	 * @param feasibleEnd
	 * @return
	 */
	private int findBestEnd(final int inclusiveWindowStart, final int inclusiveFeasibleStart, final int exclusiveWindowEnd, final int exclusiveFeasibleEnd) {
		int maxEnd = Math.max(exclusiveWindowEnd, exclusiveFeasibleEnd);
		if (inclusiveWindowStart == maxEnd || inclusiveFeasibleStart == maxEnd) {
			assert false;
//			maxEnd += 1;
		}
		// Why +1 1 again?
		return maxEnd + 1;
	}

}