package com.mmxlabs.lingo.reports.emissions.cii;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.components.SimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.lingo.reports.views.standard.SimpleTabularReportView;
import com.mmxlabs.lingo.reports.views.standard.VolumeTrackingReportView.VolumeData;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIITabularReportView extends SimpleTabularReportView<CIITabularReportView.CIIGradesData> {


	public class CIIGradesData {

	}

	protected CIITabularReportView() {
		super("CII_Grades_ReportView");
	}

	@Override
	protected SimpleTabularReportContentProvider createContentProvider() {
		return new SimpleTabularReportContentProvider();
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<CIIGradesData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<CIIGradesData>() {
			
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
				return List.of();
			}
		};
	}

}
