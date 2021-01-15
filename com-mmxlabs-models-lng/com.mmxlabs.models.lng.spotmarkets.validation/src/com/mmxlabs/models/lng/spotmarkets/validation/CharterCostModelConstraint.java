/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterCostModelConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject object = ctx.getTarget();
		if (object instanceof CharterInMarket) {
			final CharterInMarket ccm = (CharterInMarket) object;
			if (ccm.getVessel() == null) {
				final String failureMessage = "A charter in market model needs to be associated with a vessel.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__VESSEL);
				statuses.add(dsd);
			}
			if (ccm.getCharterInRate() == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("A charter rate must be specified for market %s", ccm.getName())));
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE);
				statuses.add(dsd);
			} else {
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, ccm, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE, ccm.getCharterInRate(),
						PriceIndexType.CHARTER);
				if (!result.isOk()) {
					final String message = String.format("[Charter In Market|'%s']%s", statuses, result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__CHARTER_IN_RATE);
					statuses.add(dsd);
				}
			}
		}
		if (object instanceof CharterOutMarket) {
			final CharterOutMarket ccm = (CharterOutMarket) object;
			if (ccm.getVessels().isEmpty()) {
				final String failureMessage = "A charter out market needs to be associated with one or more vessels.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__VESSELS);
				statuses.add(dsd);
			}

			if (ccm.getCharterOutRate() == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("A charter rate must be specified for market %s", ccm.getName())));
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__CHARTER_OUT_RATE);
				statuses.add(dsd);
			} else {
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__CHARTER_OUT_RATE, ccm.getCharterOutRate(),
						PriceIndexType.CHARTER);
				if (!result.isOk()) {
					final String message = String.format("[Charter Out Market|'%s']%s", statuses, result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__CHARTER_OUT_RATE);
					statuses.add(dsd);
				}
			}
			if (ccm.getMinCharterOutDuration() == 0) {
				final String failureMessage = "A minimum charter out duration must be specified.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage), IStatus.ERROR);
				dsd.addEObjectAndFeature(ccm, SpotMarketsPackage.Literals.CHARTER_OUT_MARKET__MIN_CHARTER_OUT_DURATION);
				statuses.add(dsd);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
