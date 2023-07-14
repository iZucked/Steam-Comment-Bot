package com.mmxlabs.lingo.reports.emissions.cii;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportJSONGenerator;
import com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportModelV1;
import com.mmxlabs.lingo.reports.emissions.cii.managers.CIIScenarioColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.managers.CIIVesselColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.managers.CIIYearColumnManager;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIIReportTransformer implements AbstractSimpleTabularReportTransformer<CIIGradesData> {

	private static final double FULL_YEAR_VALUE_INTERPOLATION = 1.0;
	private Year earliestYear = null;
	private Year latestYear = null;

	private @NonNull List<CIIGradesData> diffModeData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair, @NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult> pair) {
		return List.of();
	}

	@Override
	public @NonNull List<CIIGradesData> createData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
			@NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
		if (pinnedPair != null) {
			return diffModeData(pinnedPair, otherPairs.get(0));
		}

		findYearsRange(pinnedPair, otherPairs);

		final List<CIIGradesData> outputData = new LinkedList<>();

		for (final Pair<@NonNull Schedule, @NonNull ScenarioResult> scenarioPair : otherPairs) {
			final ScenarioResult scenarioResult = scenarioPair.getSecond();
			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioResult.getScenarioDataProvider());
			
			final List<VesselEmissionAccountingReportModelV1> vesselEventEmissionModels = VesselEmissionAccountingReportJSONGenerator.createReportData(scheduleModel, false, "-");

			final Map<Vessel, Map<Year, CumulativeCII>> vesselYearCIIs = new HashMap<>();
			for (final VesselEmissionAccountingReportModelV1 model : vesselEventEmissionModels) {
				final Vessel vessel = model.getVessel();
				vesselYearCIIs.computeIfAbsent(vessel, v -> new HashMap<>());
				final Map<Year, CumulativeCII> vesselCIIs = vesselYearCIIs.get(vessel);

				final LocalDate startDate = model.eventStart.toLocalDate();
				final LocalDate endDate = model.eventStart.toLocalDate();
				
				if (startDate.getYear() == endDate.getYear()) {
					accumulateVesselYearCII(model, vessel, vesselCIIs, startDate, FULL_YEAR_VALUE_INTERPOLATION);
					continue;
				}
				
				final LocalDate newYearMidPoint = LocalDate.of(startDate.getYear(), 12, 31);
				final double earlyEventLinearInterpolationCoefficient = Duration.between(startDate, newYearMidPoint).toDays() / (double) Duration.between(startDate, endDate).toDays();
				final double lateEventLinearInterpolationCoefficient = 1 - earlyEventLinearInterpolationCoefficient;

				accumulateVesselYearCII(model, vessel, vesselCIIs, startDate, earlyEventLinearInterpolationCoefficient);
				accumulateVesselYearCII(model, vessel, vesselCIIs, endDate, lateEventLinearInterpolationCoefficient);
			}

			vesselYearCIIs.forEach((vessel, accumulatedCIIs) -> {
				final Map<Year, String> vesselGradesMap = new HashMap<>();
				accumulatedCIIs.forEach((year, cumulativeCII) -> {
					final String letterGrade = UtilsCII.getLetterGrade(vessel, cumulativeCII.findCII());
					vesselGradesMap.put(year, letterGrade);
				});
				outputData.add(new CIIGradesData(vessel, vesselGradesMap, scenarioResult));
			});
		}

		return outputData;
	}

	private void accumulateVesselYearCII(final VesselEmissionAccountingReportModelV1 model, final Vessel vessel, final Map<Year, CumulativeCII> vesselCIIs, final LocalDate startDate,
			final double earlyEventLinearInterpolationCoefficient) {
		final Year startYear = Year.of(startDate.getYear());
		vesselCIIs.computeIfAbsent(startYear, y -> new CumulativeCII(vessel));
		vesselCIIs.get(startYear).accumulateEventModel(model, earlyEventLinearInterpolationCoefficient);
	}

	private void findYearsRange(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair, @NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
		earliestYear = null;
		latestYear = null;
		if (pinnedPair != null) {
			maybeSetYearsRangeFromScenario(pinnedPair);
		}
		for (final Pair<@NonNull Schedule, @NonNull ScenarioResult> pair : otherPairs) {
			maybeSetYearsRangeFromScenario(pair);
		}
		if (earliestYear == null || latestYear == null) {
			throw new IllegalStateException();
		}
	}

	private void maybeSetYearsRangeFromScenario(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> scenarioPair) {
		final ScenarioResult scenarioResult = scenarioPair.getSecond();
		final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioResult.getScenarioDataProvider());
		
		for (final Sequence sequence : scheduleModel.getSchedule().getSequences()) {
			for (final Event event : sequence.getEvents()) {
				final Year startDateYear = Year.from(event.getStart());
				if (earliestYear == null || earliestYear.isAfter(startDateYear)) {
					earliestYear = startDateYear;
				}
				final Year endDateYear = Year.from(event.getEnd());
				if (latestYear == null || latestYear.isBefore(endDateYear)) {
					latestYear = endDateYear;
				}
			}
		}
	}

	@Override
	public @NonNull List<@NonNull ColumnManager<CIIGradesData>> getColumnManagers(@NonNull ISelectedDataProvider selectedDataProvider) {
		final List<@NonNull ColumnManager<CIIGradesData>> result = new LinkedList<>();
		if (selectedDataProvider.getAllScenarioResults().size() > 1) {
			result.add(new CIIScenarioColumnManager());
		}
		result.add(new CIIVesselColumnManager());

		for (Year year = earliestYear; !year.isAfter(latestYear); year = year.plusYears(1)) {
			result.add(new CIIYearColumnManager(year));
		}
		return result;
	}
}
