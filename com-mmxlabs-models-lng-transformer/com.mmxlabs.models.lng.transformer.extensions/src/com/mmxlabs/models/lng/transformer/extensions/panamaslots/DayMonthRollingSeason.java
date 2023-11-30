package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;

@NonNullByDefault
public class DayMonthRollingSeason extends Season {

	private int day;
	private int month;

	public DayMonthRollingSeason(final int day, final int month, final int northboundWaitingDays, final int southboundWaitingDays) {
		super(northboundWaitingDays, southboundWaitingDays);
		this.day = day;
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public boolean isAfter(final DayMonthRollingSeason other) {
		if (this.day == 0) {
			return false;
		}
		return other.day == 0 //
				|| this.month > other.month //
				|| (this.month == other.month && this.day > other.day);
	}

	public static DayMonthRollingSeason from(final PanamaSeasonalityRecord psr) {
		return new DayMonthRollingSeason(psr.getStartDay(), psr.getStartMonth(), psr.getNorthboundWaitingDays(), psr.getSouthboundWaitingDays());
	}
}
