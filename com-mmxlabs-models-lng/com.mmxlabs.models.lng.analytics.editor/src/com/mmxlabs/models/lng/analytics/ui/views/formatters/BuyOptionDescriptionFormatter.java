package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class BuyOptionDescriptionFormatter extends BaseFormatter {

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
		} else if (object instanceof BuyOpportunity) {
			final BuyOpportunity buyOpportunity = (BuyOpportunity) object;
			final LocalDate date = buyOpportunity.getDate();
			String priceExpression = buyOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}
			if (buyOpportunity.getPort() != null && date != null && priceExpression != null) {
				final String str = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
				return String.format("%s | %s | %s", buyOpportunity.getPort().getName(), str, priceExpression);
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
