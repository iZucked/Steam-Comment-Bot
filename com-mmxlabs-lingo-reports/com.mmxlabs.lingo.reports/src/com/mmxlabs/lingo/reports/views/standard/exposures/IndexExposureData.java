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

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */
public class IndexExposureData {
	public final YearMonth date;
	public final String childName;
	public final @NonNull Map<String, Double> exposures;
	public boolean isChild = false;
	public final List<IndexExposureData> children;
	public final ScenarioResult scenarioResult;
	public final Schedule schedule;

	public IndexExposureData(ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final YearMonth date,
			final @NonNull Map<String, Double> exposuresByMonth, //
			final @Nullable Map<String, Map<String, Double>> dealExposuresByMonth) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.date = date;
		this.exposures = exposuresByMonth;
		this.childName = "";
		this.children = makeChildren(dealExposuresByMonth);
	}
	
	public IndexExposureData(final ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final YearMonth date, //
			final @NonNull Map<String, Double> exposuresByMonth, //
			final @Nullable List<IndexExposureData> children) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.date = date;
		this.exposures = exposuresByMonth;
		this.childName = "";
		this.children = children;
	}
	
	public IndexExposureData(final ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final String childName, //
			final @NonNull Map<String, Double> exposuresByMonth) {
		this.date = null;
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.childName = childName;
		this.isChild = true;
		this.exposures = exposuresByMonth;
		this.children = null;
	}
	
	public static IndexExposureData flatten(List<IndexExposureData> data) {
		if (data.isEmpty()){
			return null;
		}
		final IndexExposureData temp = data.get(0);
		for (IndexExposureData ied : data) {
			if (ied.equals(temp)) continue;
			temp.exposures.putAll(ied.exposures);
			temp.children.addAll(ied.children);
		}
		return temp;
	}
	
	private List<IndexExposureData> makeChildren(final Map<String, Map<String, Double>> dealExposuresByMonth) {
		if (dealExposuresByMonth != null) {
			List<IndexExposureData> children = new LinkedList<IndexExposureData>();
			for (Map.Entry<String, Map<String, Double>> e : dealExposuresByMonth.entrySet()) {
				IndexExposureData child = new IndexExposureData(scenarioResult, schedule, e.getKey(), e.getValue());
				children.add(child);
			}
			return children;
		}
		return null;
	}
}
