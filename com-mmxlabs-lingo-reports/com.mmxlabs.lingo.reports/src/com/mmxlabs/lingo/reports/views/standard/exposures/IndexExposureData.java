/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */
public class IndexExposureData {
	public String dealSet;
	public final YearMonth date;
	public final int year;
	public final int quarter;
	public IndexExposureType type;
	public final String childName;
	public final @NonNull Map<String, Double> exposures;
	public boolean isChild = false;
	public final List<IndexExposureData> children;
	public final ScenarioResult scenarioResult;
	public final Schedule schedule;
	public String entity;
	public int fiscalYear;

	public IndexExposureData(ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final YearMonth date, final @NonNull Map<String, Double> exposuresByMonth, //
			final @Nullable Map<String, Map<String, Double>> dealExposuresByMonth) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.date = date;
		this.year = date.getYear();
		this.quarter = getQuarter(date.getMonthValue());
		this.exposures = exposuresByMonth;
		this.childName = "";
		this.children = makeChildren(dealExposuresByMonth, date);
		this.type = IndexExposureType.MONTHLY;
	}

	public IndexExposureData(ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final String dealSet) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.dealSet = dealSet;
		this.exposures = new HashMap<>();
		this.childName = "";
		this.children = new ArrayList<>();
		this.type = IndexExposureType.MONTHLY;

		this.date = YearMonth.from(LocalDate.now());
		this.year = 0;
		this.quarter = 0;
	}

	public IndexExposureData(IndexExposureData data) {
		this.scenarioResult = data.scenarioResult;
		this.schedule = data.schedule;
		this.date = data.date;
		this.year = data.year;
		this.quarter = getQuarter(date.getMonthValue());
		this.exposures = new HashMap<>();
		this.exposures.putAll(data.exposures);
		this.childName = "";
		this.children = new ArrayList<>();
	}

	public IndexExposureData(final ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final YearMonth date, //
			final @NonNull Map<String, Double> exposuresByMonth, //
			final @Nullable List<IndexExposureData> children) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.date = date;
		this.year = date.getYear();
		this.quarter = getQuarter(date.getMonthValue());
		this.exposures = exposuresByMonth;
		this.childName = "";
		this.children = children;
		this.type = IndexExposureType.MONTHLY;
	}

	public IndexExposureData(final ScenarioResult scenarioResult, //
			final Schedule schedule, //
			final YearMonth date, //
			final String childName, //
			final @NonNull Map<String, Double> exposuresByMonth, final String entity) {
		this.date = date;
		this.year = date.getYear();
		this.quarter = getQuarter(date.getMonthValue());
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.childName = childName;
		this.isChild = true;
		this.exposures = exposuresByMonth;
		this.children = null;
		this.type = IndexExposureType.MONTHLY;
		this.entity = entity;
	}

	public void setType(final IndexExposureType type) {
		this.type = type;
	}

	public void setDealSet(final String dealSet) {
		this.dealSet = dealSet;
	}

	public static IndexExposureData flatten(final List<IndexExposureData> data) {
		if (data.isEmpty()) {
			return null;
		}
		final IndexExposureData temp = data.get(0);
		for (IndexExposureData ied : data) {
			if (ied.equals(temp)) {
				continue;
			}
			ied.exposures.entrySet().forEach(e -> temp.exposures.merge(e.getKey(), e.getValue(), Double::sum));
			temp.children.addAll(ied.children);
		}
		return temp;
	}

	private List<IndexExposureData> makeChildren(final Map<String, Map<String, Double>> dealExposuresByMonth, final YearMonth date) {
		if (dealExposuresByMonth != null) {
			List<IndexExposureData> lchildren = new LinkedList<>();
			for (Map.Entry<String, Map<String, Double>> e : dealExposuresByMonth.entrySet()) {
				IndexExposureData child = new IndexExposureData(scenarioResult, schedule, date, e.getKey(), e.getValue(), null);
				lchildren.add(child);
			}
			return lchildren;
		}
		return null;
	}

	public enum IndexExposureType {
		MONTHLY(2), QUARTERLY(1), ANNUAL(0);
		private int value;

		private IndexExposureType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	private int getQuarter(final int month) {
		return (date.getMonthValue() - 1) / 3 + 1;
	}
}
