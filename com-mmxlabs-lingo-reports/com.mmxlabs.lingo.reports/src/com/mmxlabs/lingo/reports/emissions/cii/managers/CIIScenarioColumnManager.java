package com.mmxlabs.lingo.reports.emissions.cii.managers;

import com.mmxlabs.lingo.reports.components.ColumnManager;
import com.mmxlabs.lingo.reports.emissions.cii.CIITabularReportView.CIIGradesData;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIIScenarioColumnManager extends ColumnManager<CIIGradesData> {

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

	@Override
	public int compare(final CIIGradesData o1, final CIIGradesData o2) {
		final String s1 = getColumnText(o1);
		final String s2 = getColumnText(o2);
		if (s1 == null) {
			return -1;
		}
		if (s2 == null) {
			return 1;
		}
		return s1.compareTo(s2);
	}

	@Override
	public boolean isTree() {
		return false;
	}
}