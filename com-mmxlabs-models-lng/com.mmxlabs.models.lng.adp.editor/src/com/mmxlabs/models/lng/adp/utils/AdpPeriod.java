package com.mmxlabs.models.lng.adp.utils;

import java.time.Period;
import java.time.YearMonth;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class AdpPeriod {
	private final YearMonth inclusiveStart;
	private final YearMonth inclusiveEnd;
	private final int monthCount;

	public AdpPeriod(final YearMonth inclusiveStart, final YearMonth exclusiveEnd) {
		this.inclusiveStart = inclusiveStart;
		final Period p = Period.between(inclusiveStart.atDay(1), exclusiveEnd.atDay(1));
		this.monthCount = 12*p.getYears() + p.getMonths();
		this.inclusiveEnd = exclusiveEnd.minusMonths(1L);
	}

	public boolean equalsRange(final List<YearMonth> range) {
		if (this.monthCount == range.size()) {
			final Iterator<YearMonth> rangeIter = range.iterator();
			if (rangeIter.hasNext()) {
				YearMonth earliestMonth = rangeIter.next();
				YearMonth latestMonth = earliestMonth;
				while (rangeIter.hasNext()) {
					final YearMonth nextMonth = rangeIter.next();
					if (nextMonth.isBefore(earliestMonth)) {
						earliestMonth = nextMonth;
						if (earliestMonth.isBefore(inclusiveStart)) {
							return false;
						}
					} else if (nextMonth.isAfter(latestMonth)) {
						latestMonth = nextMonth;
						if (latestMonth.isAfter(inclusiveEnd)) {
							return false;
						}
					}
				}
				return this.inclusiveStart.equals(earliestMonth) && this.inclusiveEnd.equals(latestMonth);
			}
		}
		return false;
	}
}
