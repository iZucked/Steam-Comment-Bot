package com.mmxlabs.lingo.reports.emissions.cii.managers;

import com.mmxlabs.lingo.reports.emissions.cii.CIITabularReportView.CIIGradesData;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIIScenarioColumnManager extends AbstractCIIColumnManager {

	public CIIScenarioColumnManager() {
		super("Scenario");
	}

	@Override
	public String getColumnText(final CIIGradesData data) {
		final ScenarioResult scenarioResult = data.getScenarioResult();
		if (scenarioResult == null) {
			return null;
		}
		return scenarioResult.getModelRecord().getName();
	}
}