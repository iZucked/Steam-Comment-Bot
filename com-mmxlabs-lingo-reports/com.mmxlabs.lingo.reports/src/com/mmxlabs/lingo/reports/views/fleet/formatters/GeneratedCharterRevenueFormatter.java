/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GeneratedCharterRevenueFormatter extends IntegerFormatter {

	private final boolean diffMode;
	private final boolean selectionOnly;

	public GeneratedCharterRevenueFormatter(final boolean diffMode, final boolean selectionOnly) {
		this.diffMode = diffMode;
		this.selectionOnly = selectionOnly;

	}

	@Override
	public Integer getIntValue(final Object object) {

		if (object instanceof Row) {
			final Row row = (Row) object;
			if (diffMode && row.isReference()) {
				return null;
			}
			Collection<EObject> selectedElements = null;
			if (selectionOnly) {
				selectedElements = row.getTable().getSelectedElements();
			}

			final int currentRevenue = getSequenceRevenue(selectedElements, row.getLinkedSequences());
			if (diffMode) {
				int referenceRevenue = 0;
				if (row.getReferenceRow() != null) {
					final Row referenceRow = row.getReferenceRow();
					referenceRevenue = getSequenceRevenue(selectedElements, referenceRow.getLinkedSequences());
				}
				return currentRevenue - referenceRevenue;
			} else {
				return currentRevenue;
			}
		}
		return null;
	}

	protected int getSequenceRevenue(@Nullable final Collection<EObject> selectedElements, final List<Sequence> sequences) {
		int revenue = 0;
		for (Sequence sequence : sequences) {
			for (final Event evt : sequence.getEvents()) {
				if (selectedElements != null) {
					if (!selectedElements.contains(evt)) {
						continue;
					}
				}
				if (evt instanceof GeneratedCharterOut) {
					revenue += ((GeneratedCharterOut) evt).getRevenue();
				}

			}
		}
		return revenue;
	}
}
