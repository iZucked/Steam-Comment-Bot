/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class BuyOptionDescriptionFormatter extends BaseFormatter {

	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<open>";
		}

		if (object instanceof PartialCaseRow) {
			final PartialCaseRow partialCaseRow = (PartialCaseRow) object;
			final Collection<?> buys = partialCaseRow.getBuyOptions();
			return render(buys);

		} else if (object instanceof Collection<?>) {
			final Collection<?> collection = (Collection<?>) object;

			if (collection.isEmpty()) {
				return "<open>";
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
				return "<open>";
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
		} else if (object instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) object;
			final LocalDate date = buyOpportunity.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				dateStr = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
			}
			String priceExpression = buyOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}
			if (priceExpression == null) {
				priceExpression = "<not set>";
			}
			String portName = "<not set>";
			final Port port = buyOpportunity.getPort();
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
			if (portName != null && dateStr != null && priceExpression != null) {
				return String.format("%s (%s) %s", portName, dateStr, priceExpression);
			}
			return String.format("Opp <not set>");
		} else if (object instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) object;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final String str = windowStart.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID <not set>");
		} else if (object instanceof BuyMarket) {
			final BuyMarket buyMarket = (BuyMarket) object;
			final SpotMarket market = buyMarket.getMarket();
			if (market != null) {
				return String.format("Market %s", market.getName());
			}
			return String.format("Market <not set>");
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
