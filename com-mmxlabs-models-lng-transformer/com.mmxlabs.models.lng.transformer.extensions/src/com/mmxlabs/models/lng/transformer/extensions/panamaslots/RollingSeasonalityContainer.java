package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;

@NonNullByDefault
public abstract class RollingSeasonalityContainer implements ISeasonalityChangePointTripleContainer {

	protected final List<DayMonthRollingSeason> seasons;

	private RollingSeasonalityContainer(final List<DayMonthRollingSeason> seasons) {
		this.seasons = seasons;
	}

	private List<DayMonthRollingSeason> filterInvalidSeasons(int year) {
		if (Year.isLeap(year)) {
			return seasons;
		}
		final LinkedList<DayMonthRollingSeason> filteredSeasons = new LinkedList<>();
		final Iterator<DayMonthRollingSeason> iterSeasons = seasons.iterator();
		DayMonthRollingSeason previousSeason = iterSeasons.next();
		filteredSeasons.add(previousSeason);

		final DayMonthRollingSeason cutoffSeasonPoint = new DayMonthRollingSeason(1, 3, 0, 0);
		while (iterSeasons.hasNext()) {
			DayMonthRollingSeason currentSeason = iterSeasons.next();
			if (currentSeason.isAfter(cutoffSeasonPoint)) {
				filteredSeasons.add(currentSeason);
				break;
			}
			if (currentSeason.getDay() == 29 && currentSeason.getMonth() == 2) {
				currentSeason = new DayMonthRollingSeason(1, 3, currentSeason.getNorthboundWaitingDays(), currentSeason.getSouthboundWaitingDays());
			} else if (currentSeason.getDay() == 1 && currentSeason.getMonth() == 3 && previousSeason.getDay() == 1 && previousSeason.getMonth() == 3) {
				filteredSeasons.removeLast();
				filteredSeasons.add(currentSeason);
				break;
			}
			filteredSeasons.add(currentSeason);
			previousSeason = currentSeason;
		}
		iterSeasons.forEachRemaining(filteredSeasons::add);
		return filteredSeasons;
	}

	@Override
	public List<@NonNull PanamaWaitingDaysTriple> getChangePointsFor(final int year, final DateAndCurveHelper dateAndCurveHelper) {
		return filterInvalidSeasons(year).stream() //
				.map(season -> {
					final ZonedDateTime panamaSeasonStart = LocalDateTime.of(year, season.getMonth(), season.getDay(), 0, 0).atZone(ZoneId.of(PanamaTransformationConstants.PANAMA_TZ));
					final int changePoint = dateAndCurveHelper.convertTime(panamaSeasonStart);
					return new PanamaWaitingDaysTriple(changePoint, season.getNorthboundWaitingDays(), season.getSouthboundWaitingDays());
				}) //
				.toList();
	}

	protected static List<DayMonthRollingSeason> buildSeasonsFrom(final List<PanamaSeasonalityRecord> sortedWithoutYearPsrs) {
		if (sortedWithoutYearPsrs.isEmpty()) {
			throw new IllegalStateException("Cannot build seasonality container without data");
		}

		// Sanity checks
		for (PanamaSeasonalityRecord psr : sortedWithoutYearPsrs) {
			assert psr.getStartYear() == 0;
		}

		final Iterator<PanamaSeasonalityRecord> psrIter = sortedWithoutYearPsrs.iterator();
		final List<DayMonthRollingSeason> dayMonthSeasons = new LinkedList<>();
		DayMonthRollingSeason previousSeason;
		{
			final PanamaSeasonalityRecord waitingDaysDefiningPsr;
			{
				final PanamaSeasonalityRecord firstPsr = sortedWithoutYearPsrs.get(0);
				if (firstPsr.getStartDay() == 0 || (firstPsr.getStartDay() == 1 && firstPsr.getStartMonth() == 1)) {
					waitingDaysDefiningPsr = psrIter.next();
				} else {
					waitingDaysDefiningPsr = sortedWithoutYearPsrs.get(sortedWithoutYearPsrs.size() - 1);
				}
			}
			final DayMonthRollingSeason season = new DayMonthRollingSeason(1, 1, waitingDaysDefiningPsr.getNorthboundWaitingDays(), waitingDaysDefiningPsr.getSouthboundWaitingDays());
			dayMonthSeasons.add(season);
			previousSeason = season;
		}
		while (psrIter.hasNext()) {
			final PanamaSeasonalityRecord currentPsr = psrIter.next();
			if (currentPsr.getStartDay() == 0) {
				throw new IllegalStateException("Seasonality entry should have non-zero start day");
			}
			final DayMonthRollingSeason season = new DayMonthRollingSeason(currentPsr.getStartDay(), currentPsr.getStartMonth(), currentPsr.getNorthboundWaitingDays(),
					currentPsr.getSouthboundWaitingDays());
			if (!season.isAfter(previousSeason)) {
				throw new IllegalStateException("Seasons should ordered and not repeated");
			}
			dayMonthSeasons.add(season);
			previousSeason = season;
		}
		return dayMonthSeasons;
	}

	public static RollingSeasonalityContainer buildLeftSideContainer(final int yearAfterExclusive, final List<PanamaSeasonalityRecord> sortedWithoutYearPsrs) {
		return new RollingSeasonalityContainer(buildSeasonsFrom(sortedWithoutYearPsrs)) {
			@Override
			public boolean appliesToYear(int year) {
				return year < yearAfterExclusive;
			}

			@Override
			public @Nullable ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints() {
				return buildRightSideContainer(seasons, yearAfterExclusive - 1);
			}
		};
	}

	public static RollingSeasonalityContainer buildLeftSideContainer(final int yearAfterExclusive, final int northboundWaitingDays, final int southboundWaitingDays) {
		return new RollingSeasonalityContainer(Collections.singletonList(new DayMonthRollingSeason(1, 1, northboundWaitingDays, southboundWaitingDays))) {
			@Override
			public boolean appliesToYear(int year) {
				return year < yearAfterExclusive;
			}

			@Override
			public @Nullable ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints() {
				return buildRightSideContainer(seasons, yearAfterExclusive - 1);
			}
		};
	}

	public static RollingSeasonalityContainer buildRightSideContainer(final List<DayMonthRollingSeason> seasons, final int yearBeforeExclusive) {
		return new RollingSeasonalityContainer(seasons) {
			@Override
			public boolean appliesToYear(int year) {
				return yearBeforeExclusive < year;
			}

			@Override
			public @Nullable ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints() {
				return null;
			}
		};
	}

	public static RollingSeasonalityContainer buildGlobalContainer(final List<PanamaSeasonalityRecord> sortedWithoutYearPsrs) {
		return new RollingSeasonalityContainer(buildSeasonsFrom(sortedWithoutYearPsrs)) {
			@Override
			public boolean appliesToYear(int year) {
				return true;
			}

			@Override
			public @Nullable ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints() {
				return null;
			}
		};
	}

	public static RollingSeasonalityContainer buildGlobalContainer(final int northboundWaitingDays, final int southboundWaitingDays) {
		return new RollingSeasonalityContainer(Collections.singletonList(new DayMonthRollingSeason(1, 1, northboundWaitingDays, southboundWaitingDays))) {

			@Override
			public boolean appliesToYear(int year) {
				return true;
			}

			@Override
			public @Nullable ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints() {
				return null;
			}

		};
	}
}