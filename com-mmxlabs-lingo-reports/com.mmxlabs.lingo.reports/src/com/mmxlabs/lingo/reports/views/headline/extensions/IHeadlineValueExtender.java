/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.headline.extensions;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.headline.HeadlineReportView.ColumnDefinition;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

public interface IHeadlineValueExtender {

	default long getExtraValue(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult, @NonNull ColumnDefinition valueType) {
		return 0L;
	}
}
