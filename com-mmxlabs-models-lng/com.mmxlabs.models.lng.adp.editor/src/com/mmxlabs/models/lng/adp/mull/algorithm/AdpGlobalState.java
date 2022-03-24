package com.mmxlabs.models.lng.adp.mull.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;

@NonNullByDefault
public class AdpGlobalState {
	private final LocalDate startDate;
	private final LocalDateTime startDateTime;
	private final LocalDateTime endDateTimeExclusive;
	private final YearMonth adpStartYearMonth;
	private final YearMonth adpEndExclusiveYearMonth;

	private final LocalDate dayBeforeAdpStart;
	private final LocalDateTime dateTimeBeforeAdpStart;

	private final CharterInMarket nominalMarket;

	public AdpGlobalState(final YearMonth startMonth, final YearMonth endMonthExclusive, final CharterInMarket nominalMarket) {
		if (!startMonth.isBefore(endMonthExclusive)) {
			throw new IllegalStateException("ADP start must be before end");
		}
		this.startDate = startMonth.atDay(1);
		this.startDateTime = this.startDate.atStartOfDay();
		this.endDateTimeExclusive = endMonthExclusive.atDay(1).atStartOfDay();
		this.dayBeforeAdpStart = this.startDate.minusDays(1L);
		this.dateTimeBeforeAdpStart = this.dayBeforeAdpStart.atStartOfDay();
		this.nominalMarket = nominalMarket;
		this.adpStartYearMonth = startMonth;
		this.adpEndExclusiveYearMonth = endMonthExclusive;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	public LocalDateTime getEndDateTimeExclusive() {
		return endDateTimeExclusive;
	}

	public LocalDate getDayBeforeAdpStart() {
		return dayBeforeAdpStart;
	}

	public LocalDateTime getDateTimeBeforeAdpStart() {
		return dateTimeBeforeAdpStart;
	}

	public CharterInMarket getNominalMarket() {
		return nominalMarket;
	}

	public YearMonth getAdpStartYearMonth() {
		return adpStartYearMonth;
	}

	public YearMonth getAdpEndExclusiveYearMonth() {
		return adpEndExclusiveYearMonth;
	}
}
