/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyMarket;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.BuyOption;
import com.mmxlabs.models.lng.analytics.BuyReference;
import com.mmxlabs.models.lng.analytics.PartialCaseRow;
import com.mmxlabs.models.lng.analytics.SellMarket;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.SellOption;
import com.mmxlabs.models.lng.analytics.SellReference;
import com.mmxlabs.models.lng.analytics.ui.views.sandbox.AnalyticsBuilder;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PartialCaseRowBuyToSellConstraint extends AbstractModelMultiConstraint {
	public static final String viewName = "Options";

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof PartialCaseRow partialCaseRow) {
			validateNonShipped(ctx, statuses, partialCaseRow, AnalyticsBuilder.isFOBPurchase(), AnalyticsBuilder.isFOBSale());
			validateNonShipped(ctx, statuses, partialCaseRow, AnalyticsBuilder.isDESPurchase(), AnalyticsBuilder.isDESSale());
		}
	}

	private void validateNonShipped(final IValidationContext ctx, final List<IStatus> statuses, final PartialCaseRow partialCaseRow, final Predicate<BuyOption> purchaseType,
			final Predicate<SellOption> saleType) {
		for (final BuyOption buyOption : partialCaseRow.getBuyOptions().stream().filter(purchaseType).collect(Collectors.toList())) {
			if (!(buyOption instanceof BuyMarket)) {
				for (final SellOption sellOption : partialCaseRow.getSellOptions().stream().filter(saleType).collect(Collectors.toList())) {
					if (!(sellOption instanceof SellMarket)) {
						final ZonedDateTime buyWindowStart = AnalyticsBuilder.getWindowStartDate(buyOption);
						final ZonedDateTime buyWindowEnd = AnalyticsBuilder.getWindowEndDate(buyOption);

						final ZonedDateTime sellWindowStart = AnalyticsBuilder.getWindowStartDate(sellOption);
						final ZonedDateTime sellWindowEnd = AnalyticsBuilder.getWindowEndDate(sellOption);

						if (buyWindowStart != null && buyWindowEnd != null && sellWindowStart != null && sellWindowEnd != null) {
							if (!(isDateWithinPeriod(sellWindowStart, buyWindowStart, buyWindowEnd) || isDateWithinPeriod(sellWindowEnd, buyWindowStart, buyWindowEnd))) {
								final DetailConstraintStatusDecorator deco = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
										String.format("%s - potential non-shipped cargo (%s - %s) has incompatible windows.", viewName, getOpportunityID(buyOption), getOpportunityID(sellOption))));
								deco.addEObjectAndFeature(partialCaseRow, AnalyticsPackage.Literals.OPTION_ANALYSIS_MODEL__PARTIAL_CASE);
								statuses.add(deco);
							}
						}
					}
				}
			}
		}
	}

	private boolean isDateWithinPeriod(final ZonedDateTime date, final ZonedDateTime start, final ZonedDateTime end) {
		return !date.isBefore(start) && !date.isAfter(end);
	}

	private String getOpportunityID(final Object object) {
		final DateTimeFormatter datePattern = DateTimeFormatter.ofPattern("dd/MM/yy");
		if (object instanceof BuyOpportunity buyOpportunity) {
			final LocalDate date = buyOpportunity.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				dateStr = date.format(datePattern);
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
		} else if (object instanceof BuyReference buyReference) {
			final LoadSlot slot = buyReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final String str = windowStart.format(datePattern);

				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID <not set>");
		} else if (object instanceof SellOpportunity sellOpportunity) {

			final LocalDate date = sellOpportunity.getDate();
			String dateStr = "<not set>";
			if (date != null) {
				dateStr = date.format(datePattern);
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
		} else if (object instanceof SellReference sellReference) {
			final DischargeSlot slot = sellReference.getSlot();
			if (slot != null) {
				final LocalDate windowStart = slot.getWindowStart();
				final String str = windowStart.format(datePattern);
				return String.format("%s (%s)", slot.getName(), str);
			}
			return String.format("ID %s", "<not set>");
		}
		return "<not set>";
	}
}
