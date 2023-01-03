/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.Locale;

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
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class ValueMatrixSellMarketDescriptionFormatter extends BaseFormatter {
	private static final String STR_NOT_SET = "<not set>";

	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<open>";
		}

		if (object instanceof final ViabilityRow row) {
			final SellOption sell = row.getSellOption();
			return render(sell);
		} else if (object instanceof final BreakEvenAnalysisRow row) {
			final SellOption sell = row.getSellOption();
			return render(sell);
		} else if (object instanceof final PartialCaseRow row) {
			final Collection<?> sells = row.getSellOptions();

			return render(sells);

		} else if (object instanceof final Collection<?> collection) {

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
		} else if (object instanceof final Object[] arr) {
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
		} else if (object instanceof final SellOpportunity sellOpportunity) {

			final String name = sellOpportunity.isSetName() ? sellOpportunity.getName() : null;
			if (name != null && !name.isBlank()) {
				return name.trim();
			}

			final LocalDate date = sellOpportunity.getDate();
			String dateStr = STR_NOT_SET;
			if (date != null) {
				final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
				dateStr = date.format(sdf);

				if (sellOpportunity.isSpecifyWindow()) {
					sellOpportunity.getWindowSize();
					sellOpportunity.getWindowSizeUnits();

					final int size = sellOpportunity.getWindowSize();
					final TimePeriod units = sellOpportunity.getWindowSizeUnits();
					String suffix;
					switch (units) {
					case DAYS:
						suffix = "d";
						break;
					case HOURS:
						suffix = "h";
						break;
					case MONTHS:
						suffix = "m";
						break;
					default:
						return "";
					}
					if (size > 0) {
						dateStr = String.format("%s +%d%s", dateStr, size, suffix);
					}
				}
			}
			String priceExpression = sellOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}

			if (priceExpression == null) {
				if (sellOpportunity.getContract() != null) {
					priceExpression = "";
				} else {
					priceExpression = STR_NOT_SET;
				}
			}
			String portName = STR_NOT_SET;
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
		} else if (object instanceof final SellReference sellReference) {
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
				final String str = windowStart.format(sdf);
				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID %s", STR_NOT_SET);
		} else if (object instanceof final SellMarket sellMarket) {
			final SpotMarket market = sellMarket.getMarket();
			if (market != null) {
				return String.format("%s", market.getName());
			}
			return String.format("%s", STR_NOT_SET);
		}

		if (object == null)

		{
			return "";
		} else {
			return object.toString();
		}
	}
}
