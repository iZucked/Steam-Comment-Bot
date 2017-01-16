/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class PermutationDescriptionFormatter extends BaseFormatter {

	@Override
	public String render(final Object obj) {

		if (obj instanceof Row) {
			final Row row = (Row) obj;

			final CycleGroup group = row.getCycleGroup();
			if (group != null) {
				return group.getDescription();

			}
		}
		return "";
	}

	@Override
	public Comparable<?> getComparable(final Object obj) {
		if (obj instanceof Row) {
			final Row row = (Row) obj;

			final CycleGroup group = row.getCycleGroup();
			if (group != null) {
				return group.getDescription() == null ? "" : group.getDescription();
			}
		}
		return "";
	}

}
