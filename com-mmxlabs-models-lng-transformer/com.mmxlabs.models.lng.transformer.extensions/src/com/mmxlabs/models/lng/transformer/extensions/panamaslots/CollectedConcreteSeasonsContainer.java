package com.mmxlabs.models.lng.transformer.extensions.panamaslots;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord;
import com.mmxlabs.models.lng.transformer.util.DateAndCurveHelper;

@NonNullByDefault
public class CollectedConcreteSeasonsContainer implements ISeasonalityChangePointTripleContainer {

	private final int minYearInclusive;
	private final int maxYearInclusive;
	private final List<List<DayMonthRollingSeason>> years;

	private CollectedConcreteSeasonsContainer(final int minYearInclusive, final int maxYearInclusive, final List<List<DayMonthRollingSeason>> years) {
		assert minYearInclusive <= maxYearInclusive;
		assert years.size() == maxYearInclusive - minYearInclusive + 1;
		this.minYearInclusive = minYearInclusive;
		this.maxYearInclusive = maxYearInclusive;
		this.years = years;
	}

	@Override
	public List<PanamaWaitingDaysTriple> getChangePointsFor(final int year, final DateAndCurveHelper dateAndCurveHelper) {
		return years.get(year - minYearInclusive).stream() //
				.map(season -> {
					final ZonedDateTime panamaSeasonStart = LocalDateTime.of(year, season.getMonth(), season.getDay(), 0, 0).atZone(ZoneId.of(PanamaTransformationConstants.PANAMA_TZ));
					final int changePoint = dateAndCurveHelper.convertTime(panamaSeasonStart);
					return new PanamaWaitingDaysTriple(changePoint, season.getNorthboundWaitingDays(), season.getSouthboundWaitingDays());
				}) //
				.toList();
	}

	@Override
	public boolean appliesToYear(int year) {
		return minYearInclusive <= year && year <= maxYearInclusive;
	}

	public static CollectedConcreteSeasonsContainer from(final List<PanamaSeasonalityRecord> sortedWithYearPsrs, final int nbWaitingDaysCarriedForward, final int sbWaitingDaysCarriedForward) {
		if (sortedWithYearPsrs.isEmpty()) {
			throw new IllegalStateException("Cannot build seasonality container without data");
		}

		for (PanamaSeasonalityRecord psr : sortedWithYearPsrs) {
			assert psr.isSetStartYear();
			assert psr.getStartDay() != 0;
		}

		final List<List<DayMonthRollingSeason>> years = new ArrayList<>();

		final int minYearInclusive = sortedWithYearPsrs.get(0).getStartYear();
		int maxYearInclusive = minYearInclusive;

		int previousNbWaitingDays = nbWaitingDaysCarriedForward;
		int previousSbWaitingDays = sbWaitingDaysCarriedForward;

		DayMonthRollingSeason previousSeason = new DayMonthRollingSeason(1, 1, previousNbWaitingDays, previousSbWaitingDays);
		int currentYear = minYearInclusive - 1;

		// Null object pattern - this variable should be reassigned on first for loop iteration
		List<DayMonthRollingSeason> currentYearSeasons = Collections.emptyList();
		for (final PanamaSeasonalityRecord currentPsr : sortedWithYearPsrs) {
			final int psrYear = currentPsr.getStartYear();
			if (currentYear != currentPsr.getStartYear()) {
				assert currentYear < psrYear;
				final DayMonthRollingSeason derivedFirstSeason = new DayMonthRollingSeason(1, 1, previousSeason.getNorthboundWaitingDays(), previousSeason.getSouthboundWaitingDays());
				{
					final List<DayMonthRollingSeason> intermediateYearSeason = Collections.singletonList(derivedFirstSeason);
					for (int intermediateYear = currentYear + 1; intermediateYear < psrYear; ++intermediateYear) {
						years.add(intermediateYearSeason);
					}
				}
				currentYear = psrYear;
				maxYearInclusive = psrYear;
				currentYearSeasons = new ArrayList<>();
				years.add(currentYearSeasons);
				if (currentPsr.getStartDay() != 1 || currentPsr.getStartMonth() != 1) {
					currentYearSeasons.add(derivedFirstSeason);
				}
			}
			assert currentYear == currentPsr.getStartYear();
			final DayMonthRollingSeason currentSeason = DayMonthRollingSeason.from(currentPsr);
			currentYearSeasons.add(currentSeason);
			previousSeason = currentSeason;
		}
		return new CollectedConcreteSeasonsContainer(minYearInclusive, maxYearInclusive, years);
	}

	@Override
	public @Nullable ISeasonalityChangePointTripleContainer buildRightSideRollingChangePoints() {
		return RollingSeasonalityContainer.buildRightSideContainer(years.get(years.size() - 1), maxYearInclusive);
	}
}
