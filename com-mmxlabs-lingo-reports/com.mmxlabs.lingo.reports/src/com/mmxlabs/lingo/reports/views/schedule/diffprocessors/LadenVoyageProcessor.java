/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.DateTime;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LadenVoyageProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {
	}

	private void generateCycleDiffForElement(final Table table, final Map<EObject, Set<EObject>> equivalancesMap, final Map<EObject, Row> elementToRowMap, final EObject referenceElement) {

		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}
		if (!referenceRow.isReference()) {
			// return;
		}

		if (referenceRow.getTarget() instanceof CargoAllocation) {
			final CargoAllocation referenceCargoAllocation = (CargoAllocation) referenceRow.getTarget();

			for (final Event event : referenceCargoAllocation.getEvents()) {
				if (event instanceof Journey) {
					final Journey journey = (Journey) event;
					if (journey.isLaden()) {
						processEventForOverlaps(elementToRowMap, referenceRow, event.getSequence(), event);
					}
				}
				if (event instanceof Idle) {
					final Idle idle = (Idle) event;
					if (idle.isLaden()) {
						processEventForOverlaps(elementToRowMap, referenceRow, event.getSequence(), event);
					}
				}
			}
		} else if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
			final GeneratedCharterOut event = (GeneratedCharterOut) referenceRow.getTarget();
			processEventForOverlaps(elementToRowMap, referenceRow, event.getSequence(), event);
		} else if (referenceRow.getTarget() instanceof VesselEventVisit) {
			final VesselEventVisit event = (VesselEventVisit) referenceRow.getTarget();
			processEventForOverlaps(elementToRowMap, referenceRow, event.getSequence(), event);
		}
	}

	private void processEventForOverlaps(final Map<EObject, Row> elementToRowMap, final Row referenceRow, final Sequence referenceSequence, final Event event) {
		final ZonedDateTime start = new ZonedDateTime(event.getStart());
		final ZonedDateTime end = new ZonedDateTime(event.getEnd());

		final Interval referenceInterval = new Interval(start, end);

		for (final EObject scenario : referenceRow.getTable().getScenarios()) {
			if (scenario instanceof LNGScenarioModel) {
				final LNGPortfolioModel portfolioModel = ((LNGScenarioModel) scenario).getPortfolioModel();
				if (portfolioModel != null) {
					final ScheduleModel scheduleModel = portfolioModel.getScheduleModel();
					if (scheduleModel != null) {
						if (scheduleModel.getSchedule() != referenceRow.getSchedule()) {
							for (final Sequence sequence : scheduleModel.getSchedule().getSequences()) {
								if (sequence.getName().equals(referenceSequence.getName())) {
									bindToLadenOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
								}
							}

						}
					}
				}
			}
		}

	}

	private void bindToLadenOverlaps(final Sequence sequence, final Row referenceRow, final Interval referenceInterval, final Map<EObject, Row> elementToRowMap) {
		for (final Event event : sequence.getEvents()) {

			if (event instanceof Journey) {
				final Journey journey = (Journey) event;
				if (journey.isLaden()) {
					bindEvent(referenceRow, referenceInterval, elementToRowMap, event);
				}
			} else if (event instanceof Idle) {
				final Idle idle = (Idle) event;
				if (idle.isLaden()) {
					bindEvent(referenceRow, referenceInterval, elementToRowMap, event);
				}
			} else if (event instanceof GeneratedCharterOut) {
				bindEvent(referenceRow, referenceInterval, elementToRowMap, event);
			} else if (event instanceof VesselEventVisit) {
				bindEvent(referenceRow, referenceInterval, elementToRowMap, event);
			}

		}
	}

	private void bindEvent(final Row referenceRow, final Interval referenceInterval, final Map<EObject, Row> elementToRowMap, final Event event) {
		final ZonedDateTime start = new ZonedDateTime(event.getStart());
		final ZonedDateTime end = new ZonedDateTime(event.getEnd());

		final Interval interval = new Interval(start, end);

		if (referenceInterval.overlaps(interval)) {

			final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(referenceRow.getTable(), referenceRow);
			final Row r = elementToRowMap.get(event);
			if (r != null) {
				CycleGroupUtils.addToOrMergeUserGroup(referenceRow.getTable(), r, group);
			}
		}
	}

	@Override
	public void runDiffProcess(final Table table, final List<EObject> referenceElements, final List<EObject> uniqueElements, final Map<EObject, Set<EObject>> equivalancesMap,
			final Map<EObject, Row> elementToRowMap) {

		for (final EObject referenceElement : referenceElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, referenceElement);
		}
		for (final EObject element : uniqueElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, element);
		}
	}
}
