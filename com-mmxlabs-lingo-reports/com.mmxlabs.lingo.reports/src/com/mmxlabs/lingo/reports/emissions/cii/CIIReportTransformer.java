package com.mmxlabs.lingo.reports.emissions.cii;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.emissions.VesselEmissionAccountingReportJSONGenerator;
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
import com.mmxlabs.models.lng.schedule.cii.CIIAccumulatableEventModel;
import com.mmxlabs.models.lng.schedule.cii.CumulativeCII;
import com.mmxlabs.models.lng.schedule.cii.UtilsCII;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIIReportTransformer implements AbstractSimpleTabularReportTransformer<CIIGradesData> {

	private static final double FULL_YEAR_VALUE_INTERPOLATION = 1.0;
	private Year earliestYear = null;
	private Year latestYear = null;
	private final Image pinImage;

	public CIIReportTransformer(final Image pinImage) {
		this.pinImage = pinImage;
	}

	@Override
	public @NonNull List<CIIGradesData> createData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
			@NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
		
		
		final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairsWithMaybePinnedOneAsWell = new ArrayList<>(otherPairs);
		if (pinnedPair != null) {
			otherPairsWithMaybePinnedOneAsWell.add(pinnedPair);
		}
		
		if (otherPairsWithMaybePinnedOneAsWell.isEmpty()) {
			return new ArrayList<>();
		}

		findYearsRange(pinnedPair, otherPairs);

		final List<CIIGradesData> outputData = new LinkedList<>();

		for (final Pair<@NonNull Schedule, @NonNull ScenarioResult> scenarioPair : otherPairsWithMaybePinnedOneAsWell) {
			final ScenarioResult scenarioResult = scenarioPair.getSecond();
			final ScheduleModel scheduleModel = ScenarioModelUtil.getScheduleModel(scenarioResult.getScenarioDataProvider());
			
			final List<? extends CIIAccumulatableEventModel> vesselEventEmissionModels = VesselEmissionAccountingReportJSONGenerator.createReportData(scheduleModel, false, "-");

			final Map<Vessel, Map<Year, CumulativeCII>> vesselYearCIIs = new HashMap<>();
			for (final CIIAccumulatableEventModel model : vesselEventEmissionModels) {
				final Vessel vessel = model.getCIIVessel();
				vesselYearCIIs.computeIfAbsent(vessel, v -> new HashMap<>());
				final Map<Year, CumulativeCII> vesselCIIs = vesselYearCIIs.get(vessel);

				final LocalDate startDate = model.getCIIStartDate();
				final LocalDate endDate = model.getCIIEndDate();
				
				if (startDate.getYear() == endDate.getYear()) {
					accumulateVesselYearCII(model, vessel, vesselCIIs, startDate, FULL_YEAR_VALUE_INTERPOLATION);
					continue;
				}
				
				final LocalDate newYearMidPoint = LocalDate.of(startDate.getYear(), Month.DECEMBER.getValue(), Month.DECEMBER.maxLength());
				final double earlyEventLinearInterpolationCoefficient = Period.between(startDate, newYearMidPoint).getDays() / (double) Period.between(startDate, endDate).getDays();
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

	private void accumulateVesselYearCII(final CIIAccumulatableEventModel model, final Vessel vessel, final Map<Year, CumulativeCII> vesselCIIs, final LocalDate startDate,
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
			result.add(new CIIScenarioColumnManager(selectedDataProvider, pinImage));
		}
		result.add(new CIIVesselColumnManager());

		if (earliestYear != null) {
			for (Year year = earliestYear; !year.isAfter(latestYear); year = year.plusYears(1)) {
				result.add(new CIIYearColumnManager(year));
			}
		}
		return result;
	}
}
