/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.diffprocessors;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.lingo.reports.utils.ScheduleDiffUtils;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.schedule.Schedule;

public class StructuralDifferencesProcessor implements IDiffProcessor {

	private final ScheduleDiffUtils scheduleDiffUtils;

	public StructuralDifferencesProcessor(ScheduleDiffUtils scheduleDiffUtils) {
		this.scheduleDiffUtils = scheduleDiffUtils;
	}

	@Override
	public void processSchedule(final Schedule scheudle, boolean isPinned) {

	}

	@Override
	public void runDiffProcess(final Table table, final List<EObject> referenceElements, final List<EObject> uniqueElements, final Map<EObject, Set<EObject>> equivalancesMap,
			final Map<EObject, Row> elementToRowMap) {

		for (final EObject referenceElement : referenceElements) {
			final Collection<EObject> equivalents = equivalancesMap.get(referenceElement);
			if (equivalents == null || equivalents.isEmpty()) {
				// Unique, so display
				// for (final EObject referenceElement : referenceElements) {

				// elementToRowMap.get(referenceElement).setVisible(true);
				// }
				continue;
			}

			boolean showRows = false;
			for (final EObject equivalent : equivalents) {
				if (scheduleDiffUtils.isElementDifferent(referenceElement, equivalent)) {
					showRows = true;
					// scheduleDiffUtils.isElementDifferent(referenceElement, equivalent);
					break;
				}
			}
			if (showRows) {
				final Row referenceRow = elementToRowMap.get(referenceElement);
				if (referenceRow != null) {
					referenceRow.setVisible(true);
					final CycleGroup cycleGroup = CycleGroupUtils.createOrReturnCycleGroup(table, referenceRow);
					for (final EObject element : equivalents) {
						final Row elementRow = elementToRowMap.get(element);
						if (elementRow != null) {
							elementRow.setVisible(true);
							CycleGroupUtils.addToOrMergeCycleGroup(table, elementRow, cycleGroup);
						}
					}
				}
			}

		}
	}

}
