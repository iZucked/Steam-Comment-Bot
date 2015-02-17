package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;

public class PermutationGroupFormatter extends BaseFormatter {

	@Override
	public String render(final Object obj) {

		if (obj instanceof Row) {
			final Row row = (Row) obj;

			final CycleGroup group = row.getCycleGroup();
			if (group != null && group.isSetIndex()) {
				return Integer.toString(group.getIndex());

			}
		}
		return "";
	}

	@Override
	public Comparable<?> getComparable(final Object obj) {
		if (obj instanceof Row) {
			final Row row = (Row) obj;

			final CycleGroup group = row.getCycleGroup();
			if (group != null && group.isSetIndex()) {
				return group.getIndex();

			}
		}
		return Integer.MAX_VALUE;
	}

}
