/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessTransformer {

	public static class RowData {
		/**
		 */
		public RowData(final String scenario, final String component, final Long raw, final Long deltaFitness) {
			super();
			this.scenario = scenario;
			this.component = component;
			this.raw = raw;
			this.deltaFitness = deltaFitness;
		}

		public final String scenario;
		public final String component;
		public final Long raw;
		public final Long deltaFitness;
	}

	@NonNull
	public List<RowData> transform(final ScenarioResult scenarioResult, @NonNull final Schedule schedule, @Nullable final List<RowData> pinnedData) {
		final List<RowData> rowDataList = new LinkedList<>();

		for (final Fitness f : schedule.getFitnesses()) {
			final long raw = f.getFitnessValue();
			final Long deltaFitness = pinnedData == null ? null : getDelta(f.getName(), raw, pinnedData);
			rowDataList.add(createRow(scenarioResult, f.getName(), raw, deltaFitness));
		}
		return rowDataList;
	}

	private RowData createRow(final ScenarioResult scenarioResult, final String f, final Long raw, final Long deltaFitness) {
		final ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
		final String name = modelRecord == null ? "" : modelRecord.getName();
		return new RowData(name, f, raw, deltaFitness);
	}

	private Long getDelta(@NonNull final String component, final long raw, @Nullable final List<RowData> pinnedData) {
		if (pinnedData == null) {
			return null;
		}
		for (final RowData data : pinnedData) {
			if (component.equals(data.component)) {
				return data.raw - raw;
			}
		}
		return null;
	}
}
