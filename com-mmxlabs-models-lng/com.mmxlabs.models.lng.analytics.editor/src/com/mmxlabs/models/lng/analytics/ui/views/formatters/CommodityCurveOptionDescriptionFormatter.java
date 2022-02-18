package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.CommodityCurveOverlay;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.VesselEventReference;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class CommodityCurveOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object == null) {
			return "";
		}

		if (object instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) object;
			final Collection<?> events = partialCaseRow.getCommodityCurveOptions();
			return render(events);
		} else if (object instanceof Collection<?>) {
			final Collection<?> collection = (Collection<?>) object;

			if (collection.isEmpty()) {
				return "";
			}

			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final Object o : collection) {

				if (!first) {
					sb.append("\n");
				}
				sb.append(render(o));
				first = false;
			}
			return sb.toString();
		} else if (object instanceof Object[]) {
			final Object[] objects = (Object[]) object;

			if (objects.length == 0) {
				return "";
			}
			final StringBuilder sb = new StringBuilder();
			boolean first = true;
			for (final Object o : objects) {
				if (!first) {
					sb.append("\n");
				}
				sb.append(render(o));
				first = false;
			}
			return sb.toString();
		} else if (object instanceof CommodityCurveOverlay commodityCurveOverlay) {
			final CommodityCurve referenceCurve = commodityCurveOverlay.getReferenceCurve();
			if (referenceCurve != null) {
				return referenceCurve.getName();
			}
			return String.format("ID <not set>");
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
