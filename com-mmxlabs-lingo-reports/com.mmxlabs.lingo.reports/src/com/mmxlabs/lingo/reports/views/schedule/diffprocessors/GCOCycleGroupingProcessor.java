/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.lingo.reports.views.schedule.model.ChangeType;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GCOCycleGroupingProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {
	}

	private void generateCycleDiffForElement(@NonNull final Table table, @NonNull final Map<EObject, Set<EObject>> equivalancesMap, @NonNull final Map<EObject, Row> elementToRowMap,
			@NonNull final EObject referenceElement) {
		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}

		if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
			final GeneratedCharterOut event = (GeneratedCharterOut) referenceRow.getTarget();

			final ZonedDateTime start = event.getStart();
			final ZonedDateTime end = event.getEnd();

			final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval = new NonNullPair<>(start, end);

			final Sequence referenceSequence = event.getSequence();

			for (EObject scenario : table.getScenarios()) {
				if (scenario instanceof LNGScenarioModel) {
					LNGScenarioModel scenarioModel = (LNGScenarioModel) scenario;
					ScheduleModel scheduleModel = scenarioModel.getScheduleModel();
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

	private void bindToLadenOverlaps(@NonNull final Sequence sequence, @NonNull final Row referenceRow, @NonNull final NonNullPair<ZonedDateTime, ZonedDateTime> referenceInterval,
			@NonNull final Map<EObject, Row> elementToRowMap) {
		for (final Event event : sequence.getEvents()) {
			if (event instanceof GeneratedCharterOut) {
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

			final CycleGroup group = CycleGroupUtils.createOrReturnCycleGroup(referenceRow.getTable(), referenceRow);
			CycleGroupUtils.setChangeType(group, ChangeType.CHARTERING);
			final Row r = elementToRowMap.get(event);
			if (r != null) {
				CycleGroupUtils.addToOrMergeCycleGroup(referenceRow.getTable(), r, group);
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
