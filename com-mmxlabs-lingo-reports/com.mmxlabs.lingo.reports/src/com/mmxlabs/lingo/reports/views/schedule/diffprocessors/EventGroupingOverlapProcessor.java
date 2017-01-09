/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.time.Hours;
import com.mmxlabs.common.time.TimeUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class EventGroupingOverlapProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {
	}

	private void generateCycleDiffForElement(final Table table, final Map<EObject, Set<EObject>> equivalancesMap, final Map<EObject, Row> elementToRowMap, final EObject referenceElement) {
		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}
		EventGrouping referenceGrouping = null;
		if (referenceElement instanceof SlotAllocation) {
			referenceGrouping = ((SlotAllocation) referenceElement).getCargoAllocation();
		} else if (referenceElement instanceof EventGrouping) {
			referenceGrouping = (EventGrouping) referenceElement;
		}
		if (referenceGrouping != null) {
			final EList<Event> events = referenceGrouping.getEvents();
			if (!events.isEmpty()) {
				final Event firstEvent = events.get(0);
				final Event lastEvent = events.get(events.size() - 1);

				final ZonedDateTime start = firstEvent.getStart();

				final ZonedDateTime end = lastEvent.getEnd();

				final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval = new NonNullPair<>(start, end);

				final Sequence referenceSequence = firstEvent.getSequence();

				if (referenceSequence.getSequenceType() == SequenceType.ROUND_TRIP) {
					return;
				}

				for (final EObject scenario : table.getScenarios()) {
					if (scenario instanceof LNGScenarioModel) {
						final LNGScenarioModel scenarioModel = (LNGScenarioModel) scenario;
						final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
						if (scheduleModel != null) {
							if (scheduleModel.getSchedule() != referenceRow.getSchedule()) {
								for (final Sequence sequence : scheduleModel.getSchedule().getSequences()) {
									if (sequence.getSequenceType() != SequenceType.ROUND_TRIP) {
										if (sequence.getName().equals(referenceSequence.getName())) {
											bindToOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

	private void bindToOverlaps(@NonNull final Sequence sequence, @NonNull final Row referenceRow, @NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval,
			@NonNull final Map<EObject, Row> elementToRowMap) {
		for (final Event event : sequence.getEvents()) {

			EventGrouping thisGrouping = null;
			if (event instanceof SlotVisit) {
				thisGrouping = ((SlotVisit) event).getSlotAllocation().getCargoAllocation();
			} else if (event instanceof EventGrouping) {
				thisGrouping = (EventGrouping) event;
			}
			if (thisGrouping != null) {
				final EList<Event> events = thisGrouping.getEvents();
				if (!events.isEmpty()) {
					final Event firstEvent = events.get(0);
					final Event lastEvent = events.get(events.size() - 1);

					final ZonedDateTime start = firstEvent.getStart();
					final ZonedDateTime end = lastEvent.getEnd();

					final NonNullPair<ZonedDateTime, ZonedDateTime> interval = new NonNullPair<>(start, end);

					bindEvent(referenceRow, referenceInterval, elementToRowMap, firstEvent, interval);
				}
			}
		}
	}

	private void bindEvent(@NonNull final Row referenceRow, @NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval, @NonNull final Map<EObject, Row> elementToRowMap,
			@NonNull final Event visit, @NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> interval) {

		if (TimeUtils.overlaps(referenceInterval, interval)) {
			final NonNullPair<ZonedDateTime, ZonedDateTime> intersection = TimeUtils.intersect(referenceInterval, interval);

			final int overlapHours = Hours.in(intersection);
			final int totalHours = Hours.in(interval);

			final double ratio = (double) overlapHours / ((double) totalHours);

			if (ratio > .7) {
				final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(referenceRow.getTable(), referenceRow);
				// CycleGroupUtils.setChangeType(group, ChangeType.DURATION);
				final Row r = elementToRowMap.get(visit);
				if (r != null) {
					CycleGroupUtils.addToOrMergeUserGroup(referenceRow.getTable(), r, group);
				}
			} else {

				final int totalHours2 = Hours.in(referenceInterval);

				final double ratio2 = (double) overlapHours / ((double) totalHours2);

				if (ratio2 > .7) {
					final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(referenceRow.getTable(), referenceRow);
					// CycleGroupUtils.setChangeType(group, ChangeType.DURATION);
					final Row r = elementToRowMap.get(visit);
					if (r != null) {
						CycleGroupUtils.addToOrMergeUserGroup(referenceRow.getTable(), r, group);
					}

				}

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
