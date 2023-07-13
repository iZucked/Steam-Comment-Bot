package com.mmxlabs.lingo.reports.emissions.cii;

import java.time.Year;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.CIITabularReportView.CIIGradesData;
import com.mmxlabs.lingo.reports.emissions.cii.managers.CIIVesselColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.managers.CIIYearColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.managers.CIIScenarioColumnManager;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.VolumeTrackingReportView.VolumeData;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

public class CIIReportTransformer implements AbstractSimpleTabularReportTransformer<CIIGradesData> {
	
	private @NonNull List<CIIGradesData> diffModeData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair, @NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult> pair) {
		return List.of();
	}

	@Override
	public @NonNull List<CIIGradesData> createData(@Nullable Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
			@NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
		if (pinnedPair != null) {
			return diffModeData(pinnedPair, otherPairs.get(0));
		}
		
		
		
		return List.of();
	}

	@Override
	public @NonNull List<@NonNull ColumnManager<CIIGradesData>> getColumnManagers(@NonNull ISelectedDataProvider selectedDataProvider) {
		final List<@NonNull ColumnManager<CIIGradesData>> result = new LinkedList<>();
		if (selectedDataProvider.getAllScenarioResults().size() > 1) {
			result.add(new CIIScenarioColumnManager());
		}
		result.add(new CIIVesselColumnManager());
		
		final Year earliestYear = Year.of(2020);
		final Year latestYear = Year.of(2029);
		
		for (Year year = earliestYear; !year.isAfter(latestYear); year = year.plusYears(1)) {
			result.add(new CIIYearColumnManager(year));
		}
		return result;
	}
};
