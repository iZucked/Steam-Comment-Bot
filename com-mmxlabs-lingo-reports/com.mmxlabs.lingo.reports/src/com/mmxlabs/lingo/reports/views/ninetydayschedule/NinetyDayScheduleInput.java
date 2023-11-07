/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.util.List;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.scenario.service.ScenarioResult;

public record NinetyDayScheduleInput(ScenarioResult pinned, List<ScenarioResult> other, ISelectedDataProvider selectedDataProvider) {

}
