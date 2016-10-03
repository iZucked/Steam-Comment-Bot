package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.util.Collection;

import com.mmxlabs.lingo.reports.views.formatters.BaseFormatter;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

public class BuyOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object instanceof Collection<?>) {
			Collection<?> collection = (Collection<?>) object;
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
			if (buyOpportunity.getPort() != null && buyOpportunity.getDate() != null && buyOpportunity.getPriceExpression() != null) {

				return String.format("%s on %04d-%02d - %s", buyOpportunity.getPort().getName(), buyOpportunity.getDate().getYear(), buyOpportunity.getDate().getMonthValue(),
						buyOpportunity.getPriceExpression());
			}
			return String.format("Opp <not set>");
		} else if (object instanceof BuyReference) {
			final BuyReference buyReference = (BuyReference) object;
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				LocalDate windowStart = slot.getWindowStart();
				return String.format("%s (%04d-%02d)", slot.getName(), windowStart.getYear(), windowStart.getMonthValue());
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
