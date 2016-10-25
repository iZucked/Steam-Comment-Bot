/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.lingo.reports.diff.utils.ChangeDescriptionUtil;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

/**
 * Generate a new formatter for the previous-vessel-assignment column
 * 
 * Used in pin/diff mode.
 **/
public class PreviousVesselFormatter extends BaseFormatter {
	@Override
	public String render(final Object obj) {
		if (obj instanceof Row) {
			final Row row = (Row) obj;
			return ChangeDescriptionUtil.getPreviousVessel(row);

		}
		return null;
	}
}
