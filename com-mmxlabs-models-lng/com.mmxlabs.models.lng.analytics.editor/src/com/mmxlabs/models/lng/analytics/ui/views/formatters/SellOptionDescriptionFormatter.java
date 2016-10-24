package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class SellOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<open>";
		}

		if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) object;

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
			Object[] objects = (Object[]) object;
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

			LocalDate date = sellOpportunity.getDate();
			String priceExpression = sellOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}
			if (sellOpportunity.getPort() != null && sellOpportunity.getDate() != null && sellOpportunity.getPriceExpression() != null) {

				String str = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));

				return String.format("%s | %s | %s", sellOpportunity.getPort().getName(), str, sellOpportunity.getPriceExpression());
			}
			return String.format("Opp <not set>");

		} else if (object instanceof SellReference) {
			final SellReference sellReference = (SellReference) object;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				LocalDate windowStart = slot.getWindowStart();
				String str = windowStart.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
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
