/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.lingo.reports.views.schedule.model.UserGroup;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;

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
						final DateTime start = new DateTime(journey.getStart());
						final DateTime end = new DateTime(journey.getEnd());

						final Interval referenceInterval = new Interval(start, end);

						final Sequence referenceSequence = referenceCargoAllocation.getSequence();

						if (referenceRow.isReference()) {
							for (final Row referringRow : referenceRow.getReferringRows()) {
								for (final Sequence sequence : referringRow.getSchedule().getSequences()) {
									if (sequence.getName().equals(referenceSequence.getName())) {
										bindToLadenOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
									}
								}
							}
						} else {
							final Row referringRow = referenceRow.getReferenceRow();
							for (final Sequence sequence : referringRow.getSchedule().getSequences()) {
								if (sequence.getName().equals(referenceSequence.getName())) {
									bindToLadenOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
								}
							}
						}
					}
				}
			}

		} else if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
			final GeneratedCharterOut event = (GeneratedCharterOut) referenceRow.getTarget();

			final DateTime start = new DateTime(event.getStart());
			final DateTime end = new DateTime(event.getEnd());

			final Interval referenceInterval = new Interval(start, end);

			final Sequence referenceSequence = event.getSequence();

			if (referenceRow.isReference()) {
				for (final Row referringRow : referenceRow.getReferringRows()) {
					for (final Sequence sequence : referringRow.getSchedule().getSequences()) {
						if (sequence.getName().equals(referenceSequence.getName())) {
							bindToLadenOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
						}
					}
				}
			} else {
				final Row referringRow = referenceRow.getReferenceRow();
				if (referringRow != null) {
					for (final Sequence sequence : referringRow.getSchedule().getSequences()) {
						if (sequence.getName().equals(referenceSequence.getName())) {
							bindToLadenOverlaps(sequence, referenceRow, referenceInterval, elementToRowMap);
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
			} else if (event instanceof GeneratedCharterOut) {
				bindEvent(referenceRow, referenceInterval, elementToRowMap, event);
			}

		}
	}

	private void bindEvent(final Row referenceRow, final Interval referenceInterval, final Map<EObject, Row> elementToRowMap, final Event event) {
		final DateTime start = new DateTime(event.getStart());
		final DateTime end = new DateTime(event.getEnd());

		final Interval interval = new Interval(start, end);
//		if (referenceInterval.overlaps(interval)) {
//			final CycleGroup group = CycleGroupUtils.createOrReturnCycleGroup(referenceRow.getTable(), referenceRow);
//			final Row r = elementToRowMap.get(event);
//			if (r != null) {
//				CycleGroupUtils.addToOrMergeCycleGroup(referenceRow.getTable(), r, group);
//			}
//		}

		// final UserGroup group = CycleGroupUtils.createOrReturnUserGroup(referenceRow.getTable(), referenceRow.getCycleGroup());
		// final Row r = elementToRowMap.get(event);
		// if (r != null) {
		// CycleGroupUtils.addToOrMergeUserGroup(referenceRow.getTable(), r.getCycleGroup(), group);
		// }

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
