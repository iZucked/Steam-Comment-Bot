/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.VesselEventReference;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class VesselEventOptionDescriptionFormatter extends BaseFormatter {

	@Override
	public String render(final Object object) {

		if (object == null) {
			return "";
		}

		if (object instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) object;
			final Collection<?> events = partialCaseRow.getVesselEventOptions();
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
		} else if (object instanceof VesselEventReference) {
			final VesselEventReference reference = (VesselEventReference) object;
			final VesselEvent event = reference.getEvent();
			if (event != null) {
				return event.getName();
			}
			return String.format("ID <not set>");
		} else if (object instanceof CharterOutOpportunity) {
			final CharterOutOpportunity opp = (CharterOutOpportunity) object;
			final LocalDate date = opp.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
				dateStr = date.format(sdf);
			}
			int hireCost = opp.getHireCost();
			String portName = "<not set>";
			final Port port = opp.getPort();
			if (port != null) {
				final String n = port.getName();
				if (n != null) {
					if (n.length() > 15) {
						portName = n.substring(0, 15) + "...";
					} else {
						portName = n;
					}
				}
			}
			if (portName != null && dateStr != null) {
				return String.format("Charter %s (%s) @ %d", portName, dateStr, hireCost);
			}
			return String.format("Opp <not set>");
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
