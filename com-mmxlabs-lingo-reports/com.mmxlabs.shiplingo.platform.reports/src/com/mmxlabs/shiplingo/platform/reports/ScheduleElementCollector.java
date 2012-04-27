package com.mmxlabs.shiplingo.platform.reports;

import java.util.Collection;
import java.util.Collections;

import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

public abstract class ScheduleElementCollector implements IScenarioInstanceElementCollector {
	@Override
	public Collection<? extends Object> collectElements(final MMXRootObject rootObject) {
		Schedule lastScheduleFromScenario = getLastScheduleFromScenario(rootObject);
		if (lastScheduleFromScenario != null)
			return collectElements(lastScheduleFromScenario);
		else
			return Collections.emptySet();
	}

	protected abstract Collection<? extends Object> collectElements(Schedule schedule);

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
}