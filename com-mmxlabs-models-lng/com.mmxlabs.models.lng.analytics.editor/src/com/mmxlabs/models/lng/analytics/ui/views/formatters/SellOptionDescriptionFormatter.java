package com.mmxlabs.models.lng.analytics.ui.views.formatters;

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
			if (sellOpportunity.getPort() != null && sellOpportunity.getDate() != null && sellOpportunity.getPriceExpression() != null) {
				return String.format("%s on %04d-%02d - %s", sellOpportunity.getPort().getName(), sellOpportunity.getDate().getYear(), sellOpportunity.getDate().getMonthValue(),
						sellOpportunity.getPriceExpression());
			}
			return String.format("Opp <not set>");

		} else if (object instanceof SellReference) {
			final SellReference sellReference = (SellReference) object;
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				return String.format("ID %s", slot.getName());
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
