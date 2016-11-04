package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import com.mmxlabs.models.lng.analytics.OptionAnalysisModel;
import com.mmxlabs.models.lng.analytics.ui.views.OptionModellerView;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class OptionTreeViewerFormatter extends BaseFormatter {
	private OptionModellerView optionModellerView;
	
	public OptionTreeViewerFormatter(OptionModellerView optionModellerView) {
		this.optionModellerView = optionModellerView;
	}

	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<no model>";
		}

		if (object instanceof OptionAnalysisModel) {
			return "" + ((NamedObject) object).getName();
		}
		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
