/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Schedule;

public class GCOCycleGroupingProcessor implements IDiffProcessor {

	@Override
	public void processSchedule(final Schedule schedule, final boolean isPinned) {
	}

	private void generateCycleDiffForElement(final Table table, final Map<EObject, Set<EObject>> equivalancesMap, final Map<EObject, Row> elementToRowMap, final EObject referenceElement) {

		final Row referenceRow = elementToRowMap.get(referenceElement);
		if (referenceRow == null) {
			return;
		}

		if (referenceRow.getTarget() instanceof GeneratedCharterOut) {
			final GeneratedCharterOut gco = (GeneratedCharterOut) referenceRow.getTarget();
			final Event previousEvent = gco.getPreviousEvent();
			if (previousEvent != null) {
				final Row previousEventRow = elementToRowMap.get(previousEvent);
				if (previousEventRow != null) {
//					final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
//					CycleGroupUtils.addToOrMergeCycleGroup(table, previousEventRow, cycleGroup);
					
					
//					if (showRows) {
//						final Row referenceRow = elementToRowMap.get(referenceElement);
//						if (referenceRow != null) {
//		                    referenceRow.setVisible(true);
//		                    final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
//		                    for (final EObject element : equivalents) {
//			                    final Row elementRow = elementToRowMap.get(element);
//			                    if (elementRow != null) {
//		                            elementRow.setVisible(true);
//		                            CycleGroupUtils.addToOrMergeCycleGroup(table, elementRow, cycleGroup);
//		                        }
//		                    }
//						}
//					}
					
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
