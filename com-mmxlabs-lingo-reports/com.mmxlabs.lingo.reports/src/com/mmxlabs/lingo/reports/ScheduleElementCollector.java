/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Base class for things which collect stuff from the most recent schedule in a scenario.
 * 
 * Implementors <em>must</em> override one of {@link #collectElements(Schedule)} or {@link #collectElements(MMXRootObject, boolean)}, otherwise this will get stuck in a mutually recursive stack
 * overflow. Also, it wouldn't make sense not to override one.
 * 
 * @author hinton
 * 
 */
public abstract class ScheduleElementCollector implements IScenarioInstanceElementCollector {
	@Override
	public Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, boolean pinned) {

		LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		ScheduleModel scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);

		final Schedule lastScheduleFromScenario = scheduleModel == null ? null : scheduleModel.getSchedule();
		if (lastScheduleFromScenario != null)
			return collectElements(scenarioResult, scenarioModel, lastScheduleFromScenario, pinned);
		else
			return Collections.emptySet();
	}

	protected Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, final LNGScenarioModel rootObject, final Schedule schedule) {
		return collectElements(scenarioResult, rootObject, schedule, false);
	}

	protected Collection<? extends Object> collectElements(final ScenarioResult scenarioResult, final LNGScenarioModel rootObject, final Schedule schedule, final boolean pinned) {
		return collectElements(scenarioResult, rootObject, schedule);
	}

	@Override
	public void beginCollecting(final boolean pinDiffMode) {

	}

	@Override
	public void endCollecting() {

	}
}