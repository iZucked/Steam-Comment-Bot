package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class SellOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<open>";
		}

		if (object instanceof Collection<?>) {
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
		} else if (object instanceof SellOpportunity) {

			final SellOpportunity sellOpportunity = (SellOpportunity) object;

			final LocalDate date = sellOpportunity.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				dateStr = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
			}
			String priceExpression = sellOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}

			if (priceExpression == null) {
				priceExpression = "<not set>";
			}
			String portName = "<not set>";
			final Port port = sellOpportunity.getPort();
			if (port != null) {
				final String n = port.getName();
				if (n != null) {
					portName = n;
				}
			}
			if (portName != null && dateStr != null && priceExpression != null) {
				return String.format("%s | %s | %s", portName, dateStr, priceExpression);
			}
			return String.format("Opp <not set>");
		} else if (object instanceof SellReference) {
			final SellReference sellReference = (SellReference) object;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final String str = windowStart.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID %s", "<not set>");
		} else if (object instanceof SellMarket) {
			final SellMarket sellMarket = (SellMarket) object;
			final SpotMarket market = sellMarket.getMarket();
			if (market != null) {
				return String.format("Market %s", market.getName());
			}
			return String.format("Market %s", "<not set>");
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
