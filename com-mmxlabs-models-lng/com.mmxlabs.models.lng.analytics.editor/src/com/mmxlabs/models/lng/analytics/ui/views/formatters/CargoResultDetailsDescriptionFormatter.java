package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class CargoResultDetailsDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof ProfitAndLossResult) {
			ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) object;
			return String.format("%,.1f", profitAndLossResult.getValue());
		} else if (object instanceof BreakEvenResult) {
			BreakEvenResult breakEvenResult = (BreakEvenResult) object;
			return String.format("%,.1f", breakEvenResult.getCargoPNL());
		}

		if (object == null) {
			return "";
		} else {
			return "";
		}
	}
}
