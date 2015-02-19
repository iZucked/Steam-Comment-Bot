package com.mmxlabs.lingo.reports.views.schedule.formatters;

import com.mmxlabs.lingo.reports.diff.utils.ChangeDescriptionUtil;
import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;

public class CapacityViolationDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object obj) {

		if (obj instanceof Row) {
			final Row row = (Row) obj;
			return ChangeDescriptionUtil.getCapacityViolationDescription(row);
		}
		return "";
	}
}
