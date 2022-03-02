/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.scenario.service.ScenarioResult;

public class ScheduledEventCollector extends ScheduleElementCollector {
	
	@Override
	protected Collection<EObject> collectElements(final ScenarioResult scenarioInstance, final LNGScenarioModel scenarioModel, final Schedule schedule) {
		final List<EObject> result = new ArrayList<>();
		for (final Sequence sequence : schedule.getSequences()) {
			if (filter()) {
				for (final Event e : sequence.getEvents()) {
					if (filter(e)) {
						result.add(e);
					}
				}
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