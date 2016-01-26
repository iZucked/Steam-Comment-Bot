/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.ArrayList;
import java.util.Collection;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

public class ScheduledEventCollector extends ScheduleElementCollector {
	@Override
	protected Collection<? extends Object> collectElements(final ScenarioInstance scenarioInstance, Schedule schedule) {
		final ArrayList<Object> result = new ArrayList<Object>();
		for (final Sequence sequence : schedule.getSequences()) {
			if (filter()) {
				for (final Event e : sequence.getEvents()) if (filter(e)) result.add(e);
			} else {
				result.addAll(sequence.getEvents());
			}
		}
		return result;
	}
	protected boolean filter(final Event event) {
		return true;
	}
	protected boolean filter() {
		return false;
	}
}