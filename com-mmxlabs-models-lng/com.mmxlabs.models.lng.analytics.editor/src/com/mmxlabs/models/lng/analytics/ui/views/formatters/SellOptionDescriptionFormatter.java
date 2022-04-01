/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.OpenSell;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class SellOptionDescriptionFormatter extends BaseFormatter {
	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<open>";
		}

		if (object instanceof ViabilityRow row) {
			final SellOption sell = row.getSellOption();
			return render(sell);
		} else if (object instanceof BreakEvenAnalysisRow row) {
			final SellOption sell = row.getSellOption();
			return render(sell);
		} else if (object instanceof PartialCaseRow row) {
			final Collection<?> sells = row.getSellOptions();

			return render(sells);

		} else if (object instanceof Collection<?> collection) {

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
		} else if (object instanceof Object[] arr) {
			final Object[] objects = arr;
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
		} else if (object instanceof OpenSell) {
			return "<open>";
		} else if (object instanceof SellOpportunity sellOpportunity) {

			final LocalDate date = sellOpportunity.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
				dateStr = date.format(sdf);
			}
			String priceExpression = sellOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}

			if (priceExpression == null) {
				if (sellOpportunity.getContract() != null) {
					priceExpression = "";
				} else {
					priceExpression = "<not set>";
				}
			}
			String portName = "<not set>";
			final Port port = sellOpportunity.getPort();
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
				// Use trim() as empty expression would leave a training space.
				return String.format("%s (%s) %s", portName, dateStr, priceExpression).trim();
			}
			return String.format("Opp <not set>");
		} else if (object instanceof SellReference sellReference) {
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
				final String str = windowStart.format(sdf);
				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID %s", "<not set>");
		} else if (object instanceof SellMarket sellMarket) {
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
