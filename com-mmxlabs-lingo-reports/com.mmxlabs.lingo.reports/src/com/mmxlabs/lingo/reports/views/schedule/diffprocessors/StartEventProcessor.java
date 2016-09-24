/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.StartEvent;

public class StartEventProcessor implements IDiffProcessor {

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

		if (referenceRow.getTarget() instanceof StartEvent) {
			final StartEvent referenceStartEvent = (StartEvent) referenceRow.getTarget();

			final Sequence sequence = referenceStartEvent.getSequence();
			if (sequence.getSequenceType() == SequenceType.ROUND_TRIP) {
				return;
			}
			for (final Event event : referenceStartEvent.getEvents()) {

				if (event.getNextEvent() instanceof GeneratedCharterOut) {
					final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(referenceRow.getTable(), referenceRow);
					final Row r = elementToRowMap.get(event.getNextEvent());
					if (r != null) {
						CycleGroupUtils.addToOrMergeUserGroup(referenceRow.getTable(), r, group);
					}
				}
			}

			final Set<EObject> set = equivalancesMap.get(referenceStartEvent);
			if (set != null) {
				for (final EObject equiv : set) {
					if (equiv instanceof StartEvent) {
						final StartEvent equivStartEvent = (StartEvent) equiv;
						final Row row = elementToRowMap.get(equivStartEvent);
						for (final Event event : equivStartEvent.getEvents()) {

							if (event.getNextEvent() instanceof GeneratedCharterOut) {
								final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(row.getTable(), row);
								final Row r = elementToRowMap.get(event.getNextEvent());
								if (r != null) {
									CycleGroupUtils.addToOrMergeUserGroup(row.getTable(), r, group);
								}
							}
						}
					}
				}
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
