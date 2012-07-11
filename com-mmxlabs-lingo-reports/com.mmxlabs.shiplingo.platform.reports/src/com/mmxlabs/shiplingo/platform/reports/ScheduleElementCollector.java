/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Base class for things which collect stuff from the most recent schedule in a scenario.
 * 
 * Implementors <em>must</em> override one of {@link #collectElements(Schedule)} or {@link #collectElements(MMXRootObject, boolean)},
 * otherwise this will get stuck in a mutually recursive stack overflow. Also, it wouldn't make sense not to override one.
 * 
 * @author hinton
 *
 */
public abstract class ScheduleElementCollector implements IScenarioInstanceElementCollector {
	@Override
	public Collection<? extends Object> collectElements(final MMXRootObject rootObject, final boolean pinned) {
		Schedule lastScheduleFromScenario = getLastScheduleFromScenario(rootObject);
		if (lastScheduleFromScenario != null)
			return collectElements(lastScheduleFromScenario, pinned);
		else
			return Collections.emptySet();
	}

	protected Collection<? extends Object> collectElements(Schedule schedule) {
		return collectElements(schedule, false);
	}
	
	protected Collection<? extends Object> collectElements(Schedule schedule, boolean pinned) {
		return collectElements(schedule);
	}
	
	public Schedule getLastScheduleFromScenario(final MMXRootObject scenario) {
		if (scenario == null) {
			return null;
		}

		final ScheduleModel scheduleModel = scenario.getSubModel(ScheduleModel.class);
		if (scheduleModel != null) {
			if (scheduleModel.getOptimisedSchedule() == null) {
				return scheduleModel.getInitialSchedule();
			} else {
				return scheduleModel.getOptimisedSchedule();
			}
		}
		return null;
	}
	
	public void beginCollecting() {
		
	}
	
	public void endCollecting() {
		
	}
}