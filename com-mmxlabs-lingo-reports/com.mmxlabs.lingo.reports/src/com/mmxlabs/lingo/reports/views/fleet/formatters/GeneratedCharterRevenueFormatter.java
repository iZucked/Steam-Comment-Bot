package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GeneratedCharterRevenueFormatter extends CostFormatter {

	private final boolean diffMode;
	private final boolean selectionOnly;

	public GeneratedCharterRevenueFormatter(final boolean includeUnits, final boolean diffMode, final boolean selectionOnly) {
		super(includeUnits);
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

			final Sequence currentSequence = row.getSequence();
			final int currentRevenue = getSequenceRevenue(selectedElements, currentSequence);
			if (diffMode) {
				int referenceRevenue = 0;
				if (row.getReferenceRow() != null) {
					final Row referenceRow = row.getReferenceRow();
					final Sequence referenceSequence = referenceRow.getSequence();
					referenceRevenue = getSequenceRevenue(selectedElements, referenceSequence);
				}
				return currentRevenue - referenceRevenue;
			} else {
				return currentRevenue;
			}
		}
		return null;
	}

	protected int getSequenceRevenue(@Nullable final Collection<EObject> selectedElements, final Sequence sequence) {
		int revenue = 0;
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
		return revenue;
	}
}
