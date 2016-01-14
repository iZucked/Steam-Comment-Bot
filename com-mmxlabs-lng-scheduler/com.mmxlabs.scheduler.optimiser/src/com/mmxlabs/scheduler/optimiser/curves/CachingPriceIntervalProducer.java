package com.mmxlabs.scheduler.optimiser.curves;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.mmxlabs.common.Equality;
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
import com.mmxlabs.scheduler.optimiser.contracts.impl.PriceIntervalProviderUtil;
import com.mmxlabs.scheduler.optimiser.providers.ITimeZoneToUtcOffsetProvider;
import com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord;

public class CachingPriceIntervalProducer implements IPriceIntervalProducer {

	private final class CacheKey {

		protected final int loadStart;
		protected final int loadEnd;
		protected final int dischargeStart;
		protected final int dischargeEnd;
		protected final ILoadOption loadOption;
		protected final IDischargeOption dischargeOption;
		protected final IPricingEventHelper dischargeOption;
		protected final IPricingEventHelper dischargeOption;
		public CacheKey(int loadStart, int loadEnd, int dischargeStart, int dischargeEnd, ILoadOption loadOption, IDischargeOption dischargeOption) {
			this.loadStart = loadStart;
			this.loadEnd = loadEnd;
			this.dischargeStart = dischargeStart;
			this.dischargeEnd = dischargeEnd;
			this.loadOption = loadOption;
			this.dischargeOption = dischargeOption;
		}

		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + loadStart;
			result = (prime * result) + loadEnd;
			result = (prime * result) + dischargeStart;
			result = (prime * result) + dischargeEnd;
			result = (prime * result) + (loadOption == null ? 0 : loadOption.hashCode());
			result = (prime * result) + (dischargeOption == null ? 0 : dischargeOption.hashCode());

			return result;
		}

		/**
		 * This equals method almost certainly doesn't fulfil the normal equality contract; however it should be fast, and because this class is final and private it ought not end up getting used
		 * wrongly.
		 */
		@Override
		public final boolean equals(final Object obj) {
			if (obj instanceof CacheKey) {
				final CacheKey other = (CacheKey) obj;
				return this.loadStart == other.loadStart && this.loadEnd == other.loadEnd && this.dischargeStart == other.dischargeStart && this.dischargeEnd == other.dischargeEnd
						&& this.loadOption == other.loadOption && this.dischargeOption == other.dischargeOption;
			}
			return false;
		}

	}

	private final class Entry {
		public final int loadStart;
		public final int loadEnd;
		public final int dischargeStart;
		public final int dischargeEnd;
		public final ILoadOption loadOption;
		public final IDischargeOption dischargeOption;
		public final CacheKey cacheKey;
		public List<int[]> result = null;
		
		public Entry(IPortTimeWindowsRecord portTimeWindowsRecord, ILoadOption loadOption, IDischargeOption dischargeOption) {
			if (loadOption != null) {
				ITimeWindow loadFeasibleTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(loadOption);
				this.loadStart = loadOption.getTimeWindow().getStart();
				this.loadEnd = Math.max(loadOption.getTimeWindow().getEnd(), loadFeasibleTimeWindow.getEnd());
				this.loadOption = loadOption;
			} else {
				this.loadStart = -1;
				this.loadEnd = -1;
				this.loadOption = null;
			}
			if (dischargeOption != null) {
				ITimeWindow dischargeFeasibleTimeWindow = portTimeWindowsRecord.getSlotFeasibleTimeWindow(dischargeOption);
				this.dischargeStart = dischargeOption.getTimeWindow().getStart();
				this.dischargeEnd = Math.max(dischargeOption.getTimeWindow().getEnd(), dischargeFeasibleTimeWindow.getEnd());
				this.dischargeOption = dischargeOption;
			} else {
				this.dischargeStart = -1;
				this.dischargeEnd = -1;
				this.dischargeOption = null;
			}
			cacheKey = new CacheKey(loadStart, loadEnd, dischargeStart, dischargeEnd, loadOption, dischargeOption);
		}
	}

	IPriceIntervalProducer delegate;
	HashMap<CacheKey, List<int[]>> cache = new HashMap<>(800);

	public CachingPriceIntervalProducer(IPriceIntervalProducer delegate) {
		this.delegate = delegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getLoadIntervalsIndependentOfDischarge(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord,
	 * com.mmxlabs.scheduler.optimiser.components.ILoadOption)
	 */
	@Override
	public List<int[]> getLoadIntervalsIndependentOfDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load) {
		Entry entry = getCacheEntry(portTimeWindowRecord, load, null);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getLoadIntervalsIndependentOfDischarge(portTimeWindowRecord, load);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getLoadIntervalsBasedOnDischarge(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord,
	 * com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getLoadIntervalsBasedOnDischarge(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		Entry entry = getCacheEntry(portTimeWindowRecord, load, discharge);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getLoadIntervalsBasedOnDischarge(portTimeWindowRecord, load, discharge);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getDischargeWindowIndependentOfLoad(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord,
	 * com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getDischargeWindowIndependentOfLoad(IPortTimeWindowsRecord portTimeWindowRecord, IDischargeOption discharge) {
		Entry entry = getCacheEntry(portTimeWindowRecord, null, discharge);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getDischargeWindowIndependentOfLoad(portTimeWindowRecord, discharge);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getDischargeWindowBasedOnLoad(com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord,
	 * com.mmxlabs.scheduler.optimiser.components.ILoadOption, com.mmxlabs.scheduler.optimiser.components.IDischargeOption)
	 */
	@Override
	public List<int[]> getDischargeWindowBasedOnLoad(IPortTimeWindowsRecord portTimeWindowRecord, ILoadOption load, IDischargeOption discharge) {
		Entry entry = getCacheEntry(portTimeWindowRecord, load, discharge);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getDischargeWindowBasedOnLoad(portTimeWindowRecord, load, discharge);
			cache.put(entry.cacheKey, intervals);
			return intervals;
		} else {
			return entry.result;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.scheduler.optimiser.curves.IPriceIntervalProducer#getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(com.mmxlabs.scheduler.optimiser.components.ILoadOption,
	 * com.mmxlabs.scheduler.optimiser.components.IDischargeOption, com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider, com.mmxlabs.scheduler.optimiser.contracts.IPriceIntervalProvider,
	 * com.mmxlabs.scheduler.optimiser.voyage.IPortTimeWindowsRecord, boolean)
	 */
	@Override
	public List<int[]> getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(ILoadOption load, IDischargeOption discharge, IPriceIntervalProvider loadPriceIntervalProvider,
			IPriceIntervalProvider dischargePriceIntervalProvider, IPortTimeWindowsRecord portTimeWindowRecord, boolean dateFromLoad) {
		Entry entry = getCacheEntry(portTimeWindowRecord, load, discharge);
		if (entry.result == null) {
			List<int[]> intervals = delegate.getIntervalsWhenLoadOrDischargeDeterminesBothPricingEvents(load, discharge, loadPriceIntervalProvider, dischargePriceIntervalProvider, portTimeWindowRecord, dateFromLoad);
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

	private Entry getCacheEntry(IPortTimeWindowsRecord portTimeWindowsRecord, ILoadOption load, IDischargeOption discharge) {
		return new Entry(portTimeWindowsRecord, load, discharge);
	}

}