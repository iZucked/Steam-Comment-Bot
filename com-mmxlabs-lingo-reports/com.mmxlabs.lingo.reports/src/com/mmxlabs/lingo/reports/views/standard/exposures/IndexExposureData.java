/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */
public class IndexExposureData {
	public final String indexName;
	public final @NonNull Map<YearMonth, Double> exposures;
	public final ScenarioResult scenarioResult;
	public final Schedule schedule;
	public final String currencyUnit;
	public final String volumeUnit;
	public final List<IndexExposureData> children;

	public IndexExposureData(ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final String name, //
			final NamedIndexContainer<?> index, //
			final @NonNull Map<YearMonth, Double> exposuresByMonth, //
			final @Nullable Map<Pair<DealType, String>, Map<YearMonth, Double>> dealExposuresByMonth, //
			final String currencyUnit, final String volumeUnit) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.indexName = name == null ? "" : name;
		this.exposures = exposuresByMonth;
		this.currencyUnit = currencyUnit;
		this.volumeUnit = volumeUnit;
		this.children = makeChildren(dealExposuresByMonth);
	}

	public IndexExposureData(ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final String name, //
			final NamedIndexContainer<?> index, //
			final @NonNull Map<YearMonth, Double> exposuresByMonth, //
			final @Nullable List<IndexExposureData> children, //
			final String currencyUnit, final String volumeUnit) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.indexName = name == null ? "" : name;
		this.exposures = exposuresByMonth;
		this.currencyUnit = currencyUnit;
		this.volumeUnit = volumeUnit;
		this.children = children;
	}

	private List<IndexExposureData> makeChildren(Map<Pair<DealType, String>, Map<YearMonth, Double>> dealExposuresByMonth) {
		if (dealExposuresByMonth != null) {
			List<IndexExposureData> children = new LinkedList<IndexExposureData>();
			for (Map.Entry<Pair<DealType, String>, Map<YearMonth, Double>> e : dealExposuresByMonth.entrySet()) {
				IndexExposureData child = new IndexExposureData(scenarioResult, schedule, e.getKey().getSecond(), null, e.getValue(), (List<IndexExposureData>) null, currencyUnit, volumeUnit);
				children.add(child);
			}
			return children;
		}
		return null;
	}

}
