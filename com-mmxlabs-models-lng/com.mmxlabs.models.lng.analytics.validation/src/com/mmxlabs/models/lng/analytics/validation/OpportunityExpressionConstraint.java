/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.BuyOpportunity;
import com.mmxlabs.models.lng.analytics.SellOpportunity;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class OpportunityExpressionConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof BuyOpportunity) {
			final BuyOpportunity slot = (BuyOpportunity) target;
			if (!"?".equals(slot.getPriceExpression())) {
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, AnalyticsPackage.eINSTANCE.getBuyOpportunity_PriceExpression(), slot.getPriceExpression(),
						PriceIndexType.COMMODITY);
				if (!result.isOk()) {
					final String message = String.format("%s", result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PRICE_EXPRESSION);
					failures.add(dsd);
				}
			}
			final Port port = slot.getPort();
			if (port == null) {
				final String message = String.format("Buy has no port");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
				failures.add(dsd);
			} else {
				if (slot.isDesPurchase()) {
					if (!port.getCapabilities().contains(PortCapability.DISCHARGE)) {
						final String message = String.format("Buy port should be a discharge port");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				} else {
					if (!port.getCapabilities().contains(PortCapability.LOAD)) {
						final String message = String.format("Buy port should be a load port");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				}
			}
			if (slot.getDate() == null) {
				final String message = String.format("Buy has no date");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);
				failures.add(dsd);
			} else {
				LocalDate date = slot.getDate();
				if (date.getYear() < 1900) {
					final String message = String.format("Buy has invalid year");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);
					failures.add(dsd);
				}
			}
			if (slot.getEntity() == null && slot.getContract() == null) {
				final String message = String.format("Buy has no entity");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__ENTITY);
				failures.add(dsd);
			}
		}
		if (target instanceof SellOpportunity) {
			final SellOpportunity slot = (SellOpportunity) target;
			if (!"?".equals(slot.getPriceExpression())) {

				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, AnalyticsPackage.eINSTANCE.getSellOpportunity_PriceExpression(), slot.getPriceExpression(),
						PriceIndexType.COMMODITY);
				if (!result.isOk()) {
					final String message = String.format("%s", result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION);
					failures.add(dsd);
				}
			}

			final Port port = slot.getPort();
			if (port == null) {
				final String message = String.format("Sell has no port");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT);
				failures.add(dsd);
			} else {
				if (slot.isFobSale()) {
					if (!port.getCapabilities().contains(PortCapability.LOAD)) {
						final String message = String.format("Sell port should be a load port");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				} else {
					if (!port.getCapabilities().contains(PortCapability.DISCHARGE)) {
						final String message = String.format("Sell port should be a discharge port");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				}
			}
			if (slot.getDate() == null) {
				final String message = String.format("Sell has no date");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);
				failures.add(dsd);
			} else {
				LocalDate date = slot.getDate();
				if (date.getYear() < 1900) {
					final String message = String.format("Sell has invalid year");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);
					failures.add(dsd);
				}
			}
			if (slot.getEntity() == null && slot.getContract() == null) {
				final String message = String.format("Sell has no entity");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__ENTITY);
				failures.add(dsd);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
