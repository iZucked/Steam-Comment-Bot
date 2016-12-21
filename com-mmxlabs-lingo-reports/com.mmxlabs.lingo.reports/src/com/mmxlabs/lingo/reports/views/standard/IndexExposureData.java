/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.YearMonth;
import java.util.Map;

import com.mmxlabs.models.lng.pricing.NamedIndexContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 */
public class IndexExposureData {
	public final String indexName;
	public final Map<YearMonth, Double> exposures;
	public final ScenarioResult scenarioResult;
	public final Schedule schedule;

	public final String currencyUnit;
	public final String volumeUnit;

	public IndexExposureData(ScenarioResult scenarioResult, final Schedule schedule, final String name, final NamedIndexContainer<?> index, final Map<YearMonth, Double> exposuresByMonth,
			final String currencyUnit, final String volumeUnit) {
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.indexName = name;
		this.exposures = exposuresByMonth;
		this.currencyUnit = currencyUnit;
		this.volumeUnit = volumeUnit;
	}
}
