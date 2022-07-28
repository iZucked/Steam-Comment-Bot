/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.changeset;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResultImpl;

public abstract class AbstractSolutionSetTransformer<T extends AbstractSolutionSet> implements ISolutionSetTransformer<T> {

	protected ScenarioResultImpl make(final ScenarioInstance scenarioInstance, @Nullable final ScenarioModelRecord modelRecord, final @NonNull ScheduleModel scheduleModel) {
		if (modelRecord != null) {
			return new ScenarioResultImpl(modelRecord, scheduleModel);
		} else {
			return new ScenarioResultImpl(scenarioInstance, scheduleModel);
		}
	}
}