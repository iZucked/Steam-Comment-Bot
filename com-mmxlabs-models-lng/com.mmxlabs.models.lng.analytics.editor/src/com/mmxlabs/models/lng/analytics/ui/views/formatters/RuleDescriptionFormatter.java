package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.OptionRule;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class RuleDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof OptionRule) {
			OptionRule optionRule = (OptionRule) object;
			return optionRule.getName();
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
