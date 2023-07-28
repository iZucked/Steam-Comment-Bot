/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.cii;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.List;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

public class CIIGradeFinder {

	private CIIGradeFinder() {
	}

	private static final double FULL_YEAR_VALUE_INTERPOLATION = 1.0;

	public static String findCIIGradeForScheduleVesselYear(final ScheduleModel scheduleModel, final Vessel vessel, final Year year) {

		final List<CIIAccumulatableEventModel> vesselEventEmissionModels = ModelUtilsCII.createCIIDataForVessel(scheduleModel, vessel);
		final CumulativeCII cumulativeCII = new CumulativeCII(vessel);

		for (final CIIAccumulatableEventModel model : vesselEventEmissionModels) {
			final LocalDate startDate = model.getCIIStartDate();
			final LocalDate endDate = model.getCIIEndDate();

			boolean startDateHasRelevantYear = startDate.getYear() == year.getValue();
			boolean endDateHasReleveantYear = endDate.getYear() == year.getValue();
			
			if (startDateHasRelevantYear || endDateHasReleveantYear) {
				
				if (startDate.getYear() == endDate.getYear()) {
					cumulativeCII.accumulateEventModel(model, FULL_YEAR_VALUE_INTERPOLATION);
					continue;
				}
				final LocalDate newYearMidPoint = LocalDate.of(startDate.getYear(), 12, 31);
				final double earlyEventLinearInterpolationCoefficient = Period.between(startDate, newYearMidPoint).getDays() / (double) Period.between(startDate, endDate).getDays();
				final double lateEventLinearInterpolationCoefficient = 1 - earlyEventLinearInterpolationCoefficient;
				
				if (startDateHasRelevantYear) {
					cumulativeCII.accumulateEventModel(model, earlyEventLinearInterpolationCoefficient);
				}
				if (endDateHasReleveantYear) {
					cumulativeCII.accumulateEventModel(model, lateEventLinearInterpolationCoefficient);
				}
			}
		}
		return UtilsCII.getLetterGrade(vessel, cumulativeCII.findCII(), year);
	}
}
