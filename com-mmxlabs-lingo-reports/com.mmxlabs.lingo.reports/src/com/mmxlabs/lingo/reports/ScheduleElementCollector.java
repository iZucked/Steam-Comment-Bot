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
import com.mmxlabs.scenario.service.model.ScenarioInstance;

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
	public Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final LNGScenarioModel rootObject, final boolean pinned) {
		final Schedule lastScheduleFromScenario = getLastScheduleFromScenario(rootObject);
		if (lastScheduleFromScenario != null)
			return collectElements(scenarioInstance, lastScheduleFromScenario, pinned);
		else
			return Collections.emptySet();
	}

	protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule) {
		return collectElements(scenarioInstance, schedule, false);
	}

	protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, final Schedule schedule, final boolean pinned) {
		return collectElements(scenarioInstance, schedule);
	}

	/**
	 */
	public Schedule getLastScheduleFromScenario(final LNGScenarioModel scenario) {
		if (scenario == null) {
			return null;
		}

		final ScheduleModel scheduleModel = scenario.getScheduleModel();
		if (scheduleModel != null) {
			if (scheduleModel.getSchedule() != null) {
				return scheduleModel.getSchedule();
			}
		}
		return null;
	}

	@Override
	public void beginCollecting(final boolean pinDiffMode) {

	}

	@Override
	public void endCollecting() {

	}
}