/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.services.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;

import com.mmxlabs.lingo.reports.services.EDiffOption;
import com.mmxlabs.lingo.reports.services.ScenarioComparisonService;

public class FilterScheduleChartBySelectionHandler {
	@Inject
	private ScenarioComparisonService scenarioComparisonService;

	@Execute
	public void execute() {
		scenarioComparisonService.toggleDiffOption(EDiffOption.FILTER_SCHEDULE_CHART_BY_SELECTION);
	}
}
