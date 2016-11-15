package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.BreakEvenResult;
import com.mmxlabs.models.lng.analytics.ProfitAndLossResult;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class ResultDetailsDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof BreakEvenResult) {
			final BreakEvenResult breakEvenResult = (BreakEvenResult) object;
			return String.format("BE Price is %s", breakEvenResult.getPriceString());
		} else if (object instanceof ProfitAndLossResult) {
			ProfitAndLossResult profitAndLossResult = (ProfitAndLossResult) object;
			return String.format("Cargo P&L is %,.1f", profitAndLossResult.getValue());
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
