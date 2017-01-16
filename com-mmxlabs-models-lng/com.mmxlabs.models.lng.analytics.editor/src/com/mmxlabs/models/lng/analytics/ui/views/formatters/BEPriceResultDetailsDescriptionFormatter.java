/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class BEPriceResultDetailsDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof BreakEvenResult) {
			final BreakEvenResult breakEvenResult = (BreakEvenResult) object;
			return String.format("%s", breakEvenResult.getPriceString());
		}

		if (object == null) {
			return "";
		} else {
			return "";
		}
	}
}
