/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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
import com.mmxlabs.models.lng.analytics.VolumeMode;
import com.mmxlabs.models.lng.cargo.validation.SlotVolumeConstraint;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.parseutils.IndexConversion;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class OpportunityExpressionConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof BuyOpportunity slot) {
			if (slot.getPriceExpression() != null && !slot.getPriceExpression().contains("?") && !slot.getPriceExpression().equals("")) {
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, AnalyticsPackage.eINSTANCE.getBuyOpportunity_PriceExpression(), slot.getPriceExpression(),
						PriceIndexType.COMMODITY);
				if (!result.isOk()) {
					final String message = String.format("%s", result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PRICE_EXPRESSION);
					failures.add(dsd);
				}
			}
			if (slot.getPriceExpression() != null && slot.getPriceExpression().contains("?") && !slot.getPriceExpression().equals("?")) {
				PricingModel pricingModel = getPricingModel(extraContext);
				if (pricingModel != null) {
					final boolean expressionValidForIndexConversion = IndexConversion.isExpressionValidForIndexConversion(pricingModel, slot.getPriceExpression());
					if (!expressionValidForIndexConversion) {
						final String message = String.format("%s", "Breakeven expression must be in form ?, ?%INDEX+CONSTANT or COEFFICIENT%INDEX+?.");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CONTRACT);
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PRICE_EXPRESSION);
						failures.add(dsd);
					}
				}
			}
			if (slot.getContract() == null && (slot.getPriceExpression() == null || (slot.getPriceExpression() != null && slot.getPriceExpression().equals("")))) {
				final String message = String.format("%s", "Either a contract or a price expression must be set.");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CONTRACT);
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PRICE_EXPRESSION);
				failures.add(dsd);
			}
			if (slot.getContract() != null && (slot.getPriceExpression() != null && !slot.getPriceExpression().equals(""))) {
				final String message = String.format("%s", "Either a contract or a price expression must be set but not both.");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CONTRACT);
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PRICE_EXPRESSION);
				failures.add(dsd);
			}
			if (slot.isDesPurchase()) {
				if (slot.getCv() == 0.0) {
					final String message = "Buy needs a non-zero CV";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__CV);
					failures.add(dsd);
				}
			}
			final Port port = slot.getPort();
			if (port == null) {
				final String message = "Buy has no port";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
				failures.add(dsd);
			} else {
				if (slot.isDesPurchase()) {
					if (!port.getCapabilities().contains(PortCapability.DISCHARGE)) {
						final String message = "Buy port should be a discharge port";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				} else {
					if (!port.getCapabilities().contains(PortCapability.LOAD)) {
						final String message = "Buy port should be a load port";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				}
			}
			if (slot.getDate() == null) {
				final String message = "Buy has no date";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);
				failures.add(dsd);
			} else {
				LocalDate date = slot.getDate();
				if (date.getYear() < 1900) {
					final String message = "Buy has invalid year";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__DATE);
					failures.add(dsd);
				}
			}
			if (slot.getEntity() == null && slot.getContract() == null) {
				final String message = "Buy has no entity";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__ENTITY);
				failures.add(dsd);
			}

			if (slot.getVolumeMode() == VolumeMode.RANGE) {
				if (slot.getMaxVolume() < slot.getMinVolume()) {
					final String message = "Buy max volume is less than min volume";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME);
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MAX_VOLUME);
					failures.add(dsd);
				}
				if (slot.getVolumeUnits() == VolumeUnits.M3) {
					if (slot.getMinVolume() > SlotVolumeConstraint.SENSIBLE_M3) {
						final String message = "Buy min volume is not sensible, note units are in M3";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME);
						failures.add(dsd);
					}
					if (slot.getMaxVolume() > SlotVolumeConstraint.SENSIBLE_M3) {
						final String message = "Buy max volume is not sensible, note units are in M3";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MAX_VOLUME);
						failures.add(dsd);
					}
				}
			}

			if (slot.getVolumeMode() == VolumeMode.FIXED) {
				if (slot.getMinVolume() > SlotVolumeConstraint.SENSIBLE_M3 && slot.getVolumeUnits() == VolumeUnits.M3) {
					final String message = "Buy volume is not sensible, note units are in M3";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME);
					failures.add(dsd);
				}
			}

		}
		if (target instanceof SellOpportunity) {
			final SellOpportunity slot = (SellOpportunity) target;
			if (slot.getPriceExpression() != null && !slot.getPriceExpression().contains("?") && !slot.getPriceExpression().equals("")) {
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, slot, AnalyticsPackage.eINSTANCE.getSellOpportunity_PriceExpression(), slot.getPriceExpression(),
						PriceIndexType.COMMODITY);
				if (!result.isOk()) {
					final String message = String.format("%s", result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION);
					failures.add(dsd);
				}
			}
			if (slot.getPriceExpression() != null && slot.getPriceExpression().contains("?") && !slot.getPriceExpression().equals("?")) {
				PricingModel pricingModel = getPricingModel(extraContext);
				if (pricingModel != null) {
					final boolean expressionValidForIndexConversion = IndexConversion.isExpressionValidForIndexConversion(pricingModel, slot.getPriceExpression());
					if (!expressionValidForIndexConversion) {
						final String message = String.format("%s", "Breakeven expression must be in form ?, ?%INDEX+CONSTANT or COEFFICIENT%INDEX+?.");
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__CONTRACT);
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION);
						failures.add(dsd);
					}
				}
			}
			if (slot.getContract() == null && (slot.getPriceExpression() == null || (slot.getPriceExpression() != null && slot.getPriceExpression().equals("")))) {
				final String message = String.format("%s", "Either a contract or a price expression must be set.");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__CONTRACT);
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION);
				failures.add(dsd);
			}
			if (slot.getContract() != null && ((slot.getPriceExpression() != null && !slot.getPriceExpression().equals("")))) {
				final String message = String.format("%s", "Either a contract or a price expression must be set but not both.");
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__CONTRACT);
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PRICE_EXPRESSION);
				failures.add(dsd);

			}
			final Port port = slot.getPort();
			if (port == null) {
				final String message = "Sell has no port";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT);
				failures.add(dsd);
			} else {
				if (slot.isFobSale()) {
					if (!port.getCapabilities().contains(PortCapability.LOAD)) {
						final String message = "Sell port should be a load port";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				} else {
					if (!port.getCapabilities().contains(PortCapability.DISCHARGE)) {
						final String message = "Sell port should be a discharge port";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT);
						failures.add(dsd);
					}
				}
			}
			if (slot.getDate() == null) {
				final String message = "Sell has no date";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);
				failures.add(dsd);
			} else {
				LocalDate date = slot.getDate();
				if (date.getYear() < 1900) {
					final String message = "Sell has invalid year";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__DATE);
					failures.add(dsd);
				}
			}
			if (slot.getEntity() == null && slot.getContract() == null) {
				final String message = "Sell has no entity";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__ENTITY);
				failures.add(dsd);
			}

			if (slot.getVolumeMode() == VolumeMode.RANGE) {
				if (slot.getMaxVolume() < slot.getMinVolume()) {
					final String message = "Sell max volume is less than min volume";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME);
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__MAX_VOLUME);
					failures.add(dsd);
				}
				if (slot.getVolumeUnits() == VolumeUnits.M3) {
					if (slot.getMinVolume() > SlotVolumeConstraint.SENSIBLE_M3) {
						final String message = "Sell min volume is not sensible, note units are in M3";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME);
						failures.add(dsd);
					}
					if (slot.getMaxVolume() > SlotVolumeConstraint.SENSIBLE_M3) {
						final String message = "Sell max volume is not sensible, note units are in M3";
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__MAX_VOLUME);
						failures.add(dsd);
					}
				}
			}
			if (slot.getVolumeMode() == VolumeMode.FIXED) {
				if (slot.getMinVolume() > SlotVolumeConstraint.SENSIBLE_M3 && slot.getVolumeUnits() == VolumeUnits.M3) {
					final String message = "Sell volume is not sensible, note units are in M3";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(slot, AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME);
					failures.add(dsd);
				}
			}
		}
	}

	private static PricingModel getPricingModel(final IExtraValidationContext extraContext) {
		if (extraContext.getRootObject() instanceof LNGScenarioModel model) {
			return model.getReferenceModel().getPricingModel();
		} else {
			return null;
		}
	}
}
