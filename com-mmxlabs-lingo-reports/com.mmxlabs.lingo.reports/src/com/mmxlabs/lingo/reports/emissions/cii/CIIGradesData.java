/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.emissions.cii;

import java.time.Year;
import java.util.Map;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.scenario.service.ScenarioResult;

public class CIIGradesData {
	private final Vessel vessel;
	private final Map<Year, String> grades;
	private final ScenarioResult scenarioResult;

	public CIIGradesData(final Vessel vessel, final Map<Year, String> grades, final ScenarioResult scenarioResult) {
		this.vessel = vessel;
		this.grades = grades;
		this.scenarioResult = scenarioResult;
	}

	public ScenarioResult getScenarioResult() {
		return scenarioResult;
	}

	public Vessel getVessel() {
		return vessel;
	}

	public Map<Year, String> getGrades() {
		return grades;
	}
}

