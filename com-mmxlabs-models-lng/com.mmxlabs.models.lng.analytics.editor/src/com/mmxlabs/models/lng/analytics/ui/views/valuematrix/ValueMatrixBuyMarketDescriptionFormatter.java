/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.ui.views.valuematrix;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import com.mmxlabs.models.lng.analytics.BreakEvenAnalysisRow;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.MTMRow;
import com.mmxlabs.models.lng.analytics.OpenBuy;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.ViabilityRow;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.tabular.BaseFormatter;

public class ValueMatrixBuyMarketDescriptionFormatter extends BaseFormatter {

	@Override
	public String render(final Object object) {

		if (object == null) {
			return "<open>";
		}

		if (object instanceof final MTMRow row) {
			final BuyOption buy = row.getBuyOption();
			if (buy instanceof final BuyReference ref) {
				final LoadSlot slot = ref.getSlot();
				return slot.getName();
			}
			return render(buy);
		} else if (object instanceof final ViabilityRow row) {
			final BuyOption buy = row.getBuyOption();
			return render(buy);
		} else if (object instanceof final BreakEvenAnalysisRow row) {
			final BuyOption buy = row.getBuyOption();
			return render(buy);
		} else if (object instanceof final PartialCaseRow row) {
			final Collection<?> buys = row.getBuyOptions();
			return render(buys);

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
		} else if (object instanceof OpenBuy) {
			return "<open>";
		} else if (object instanceof final BuyOpportunity buyOpportunity) {
			final String name = buyOpportunity.isSetName() ? buyOpportunity.getName() : null;
			if (name != null && !name.isBlank()) {
				return name.trim();
			}

			final LocalDate date = buyOpportunity.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				final DateTimeFormatter sdf = DateTimeFormatsProvider.INSTANCE.createDateStringDisplayFormatter();
				dateStr = date.format(sdf);

				if (buyOpportunity.isSpecifyWindow()) {
					buyOpportunity.getWindowSize();
					buyOpportunity.getWindowSizeUnits();

					final int size = buyOpportunity.getWindowSize();
					final TimePeriod units = buyOpportunity.getWindowSizeUnits();
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
			String priceExpression = buyOpportunity.getPriceExpression();
			if (priceExpression != null && priceExpression.length() > 5) {
				priceExpression = priceExpression.substring(0, 4) + "...";
			}

			if (priceExpression == null) {
				if (buyOpportunity.getContract() != null) {
					priceExpression = "";
				} else {
					priceExpression = "<not set>";
				}
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
				// Use trim() as empty expression would leave a training space.
				return String.format("%s (%s) %s", portName, dateStr, priceExpression).trim();
			}
			return String.format("Opp <not set>");
		} else if (object instanceof final BuyReference buyReference) {
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final DateTimeFormatter sdf = DateTimeFormatter.ofPattern(DateTimeFormatsProvider.INSTANCE.getDateStringDisplay());
				final String str = windowStart.format(sdf);

				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID <not set>");
		} else if (object instanceof final BuyMarket buyMarket) {
			final SpotMarket market = buyMarket.getMarket();
			if (market != null) {
				return String.format("%s", market.getName());
			}
			return String.format("<not set>");
		}

		if (object == null) {
			return "";
		} else {
			return object.toString();
		}
	}
}
