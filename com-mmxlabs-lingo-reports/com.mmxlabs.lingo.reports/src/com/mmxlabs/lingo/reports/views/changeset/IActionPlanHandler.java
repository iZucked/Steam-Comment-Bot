/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import java.util.List;

import com.mmxlabs.scenario.service.ScenarioResult;

public interface IActionPlanHandler {

	void displayActionPlan(List<ScenarioResult> scenarios);
}
