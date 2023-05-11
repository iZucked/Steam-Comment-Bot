/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.incomestatement;

import java.time.YearMonth;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ScenarioResult;

public class IncomeStatementData {
	public final ScenarioResult scenarioResult;
	public final Object key;
	public final Map<YearMonth, Double> valuesByMonth; // Main value
	public final Map<YearMonth, Double> valuesByMonth2; // If not null, used to divide valuesByMonth to obtain average
	public IncomeStatementData parent;
	public final List<IncomeStatementData> children = new LinkedList<>();
	public Schedule schedule;

	public IncomeStatementData(@NonNull ScenarioResult scenarioResult, @NonNull Schedule schedule, final Object key, final Map<YearMonth, Double> valuesByMonth) {
		super();
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.key = key;
		this.valuesByMonth = valuesByMonth;
		this.valuesByMonth2 = null;
	}

	public IncomeStatementData(@NonNull ScenarioResult scenarioResult, @NonNull Schedule schedule, final Object key, final Map<YearMonth, Double> valuesByMonth,
			final Map<YearMonth, Double> valuesByMonth2) {
		super();
		this.scenarioResult = scenarioResult;
		this.schedule = schedule;
		this.key = key;
		this.valuesByMonth = valuesByMonth;
		this.valuesByMonth2 = valuesByMonth2;
	}
}
