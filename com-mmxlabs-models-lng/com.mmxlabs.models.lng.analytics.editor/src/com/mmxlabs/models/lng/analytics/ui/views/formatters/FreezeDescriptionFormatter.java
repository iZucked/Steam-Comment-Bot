/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.BaseCaseRow;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class FreezeDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {
 
		if (object instanceof BaseCaseRow) {
			final BaseCaseRow row = (BaseCaseRow) object;
			if (row.isFreeze()) {
				return "Y";
			}
		}
		return "";	
	}
}
