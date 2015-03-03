package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GeneratedCharterDaysFormatter extends NumberOfDPFormatter {

	private final boolean diffMode;
	private final boolean selectionOnly;

	public GeneratedCharterDaysFormatter(final boolean diffMode, final boolean selectionOnly) {
		super(1);
		this.diffMode = diffMode;
		this.selectionOnly = selectionOnly;

	}

	@Override
	public Double getDoubleValue(final Object object) {

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
			final int currentHours = getSequenceHours(selectedElements, currentSequence);
			if (diffMode) {
				int referenceHours = 0;
				if (row.getReferenceRow() != null) {
					final Row referenceRow = row.getReferenceRow();
					final Sequence referenceSequence = referenceRow.getSequence();
					referenceHours = getSequenceHours(selectedElements, referenceSequence);
				}
				return ((double) (currentHours - referenceHours)) / 24.0;
			} else {
				return currentHours / 24.0;
			}
		}
		return null;
	}

	protected int getSequenceHours(@Nullable final Collection<EObject> selectedElements, final Sequence sequence) {
		int time = 0;
		for (final Event evt : sequence.getEvents()) {
			if (selectedElements != null) {
				if (!selectedElements.contains(evt)) {
					continue;
				}
			}
			if (evt instanceof GeneratedCharterOut) {

				time += evt.getDuration();
			}

		}
		return time;
	}
}
