package com.mmxlabs.models.lng.cargo.validation;

import java.util.Objects;

import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;

public class SeasonalityDateKey {
	final int day;
	final int month;
	final Integer year;

	SeasonalityDateKey(final int day, final int month, final Integer year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other instanceof SeasonalityDateKey otherKey) {
			return day == otherKey.day //
					&& month == otherKey.month //
					&& ((year == null && otherKey.year == null) //
							|| (year != null && otherKey.year != null && year.equals(otherKey.year)));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(day, month, year);
	}

	public static SeasonalityDateKey from(PanamaSeasonalityRecord psr) {
		return new SeasonalityDateKey(psr.getStartDay(), psr.getStartMonth(), psr.isSetStartYear() ? psr.getStartYear() : null);
	}
}
