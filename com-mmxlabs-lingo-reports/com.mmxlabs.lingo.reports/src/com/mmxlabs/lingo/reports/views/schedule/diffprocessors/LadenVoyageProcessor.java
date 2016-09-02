/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.NonNullPair;
import com.mmxlabs.common.time.TimeUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class LadenVoyageProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(@NonNull final Schedule schedule, final boolean isPinned) {
	}

	private void generateCycleDiffForElement(@NonNull final Table table, @NonNull final Map<EObject, Set<EObject>> equivalancesMap, @NonNull final Map<EObject, Row> elementToRowMap,
			@NonNull final EObject referenceElement) {

		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}
		if (!referenceRow.isReference()) {
			// return;
		}

		if (referenceRow.getTarget() instanceof CargoAllocation) {
			final CargoAllocation referenceCargoAllocation = (CargoAllocation) referenceRow.getTarget();

			Sequence sequence = referenceCargoAllocation.getSequence();
			if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
				return;
			}
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

	private void processEventForOverlaps(@NonNull final Map<EObject, Row> elementToRowMap, @NonNull final Row referenceRow, @NonNull final Sequence referenceSequence, @NonNull final Event event) {
		final ZonedDateTime start = event.getStart();
		final ZonedDateTime end = event.getEnd();

		final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval = new NonNullPair<>(start, end);

		for (final EObject scenario : referenceRow.getTable().getScenarios()) {
			if (scenario instanceof LNGScenarioModel) {
				LNGScenarioModel scenarioModel = (LNGScenarioModel) scenario;
				final ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
				if (scheduleModel != null) {
					final Schedule schedule = scheduleModel.getSchedule();
					if (schedule != referenceRow.getSchedule()) {
						for (final Sequence sequence : schedule.getSequences()) {
							// Nominal cargoes never overlap (probably not really true, but right now we can overlap with anything..)
							if (sequence.getSequenceType() != SequenceType.ROUND_TRIP) {
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

	private void bindToLadenOverlaps(@NonNull final Sequence sequence, @NonNull final Row referenceRow, final @NonNull NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval,
			@NonNull final Map<EObject, Row> elementToRowMap) {
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

	private void bindEvent(@NonNull final Row referenceRow, @NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval, @NonNull final Map<EObject, Row> elementToRowMap,
			@NonNull final Event event) {
		final ZonedDateTime start = event.getStart();
		final ZonedDateTime end = event.getEnd();

		final NonNullPair<ZonedDateTime, ZonedDateTime> interval = new NonNullPair<>(start, end);

		if (TimeUtils.overlaps(referenceInterval, interval)) {

			final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(referenceRow.getTable(), referenceRow);
			final Row r = elementToRowMap.get(event);
			if (r != null) {
				CycleGroupUtils.addToOrMergeUserGroup(referenceRow.getTable(), r, group);
			}
		}
	}

	@Override
	public void runDiffProcess(@NonNull final Table table, @NonNull final List<EObject> referenceElements, @NonNull final List<EObject> uniqueElements,
			@NonNull final Map<EObject, Set<EObject>> equivalancesMap, @NonNull final Map<EObject, Row> elementToRowMap) {

		for (final EObject referenceElement : referenceElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, referenceElement);
		}
		for (final EObject element : uniqueElements) {
			generateCycleDiffForElement(table, equivalancesMap, elementToRowMap, element);
		}
	}
}
