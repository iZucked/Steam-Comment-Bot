/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.ScenarioResult;

/**
 * Base class for things which collect stuff from the most recent schedule in a
 * scenario.
 * 
 * Implementors <em>must</em> override one of {@link #collectElements(Schedule)}
 * or {@link #collectElements(MMXRootObject, boolean)}, otherwise this will get
 * stuck in a mutually recursive stack overflow. Also, it wouldn't make sense
 * not to override one.
 * 
 * @author hinton
 * 
 */
public abstract class ScheduleElementCollector implements IScenarioInstanceElementCollector {

	@Override
	public Collection<EObject> collectElements(final ScenarioResult scenarioResult, final boolean pinned) {

		final LNGScenarioModel scenarioModel = scenarioResult.getTypedRoot(LNGScenarioModel.class);
		if (scenarioModel == null) {
			return Collections.emptySet();
		}
		final ScheduleModel scheduleModel = scenarioResult.getTypedResult(ScheduleModel.class);

		final Schedule lastScheduleFromScenario = scheduleModel == null ? null : scheduleModel.getSchedule();
		if (lastScheduleFromScenario != null) {
			return collectElements(scenarioResult, scenarioModel, lastScheduleFromScenario, pinned);
		} else {
			return Collections.emptySet();
		}
	}

	protected Collection<EObject> collectElements(final ScenarioResult scenarioResult, final LNGScenarioModel rootObject, final Schedule schedule) {
		return collectElements(scenarioResult, rootObject, schedule, false);
	}

	protected Collection<EObject> collectElements(final ScenarioResult scenarioResult, final LNGScenarioModel rootObject, final Schedule schedule, final boolean pinned) {
		return collectElements(scenarioResult, rootObject, schedule);
	}

	@Override
	public void beginCollecting(final boolean pinDiffMode) {

	}

	@Override
	public void endCollecting() {

	}
}