/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MarketIndexConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraValidationContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			if (target instanceof MarketIndex index) {
				// Only need these curves when auto-hedging is enabled.
				if (!index.isAutoHedgeEnabled()) {
					return;
				}

				if (index.getFlatCurve() == null) {
					final String message = String.format("Market index %s should have a flat curve!", index.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(index, PricingPackage.Literals.MARKET_INDEX__FLAT_CURVE);
					statuses.add(dcsd);
				} else {
					final CommodityCurve cc = index.getFlatCurve();
					checkCurve("Flat curve", ctx, statuses, index, cc);
				}
				if (index.getBidCurve() == null) {
					final String message = String.format("Market index %s should have a bid curve!", index.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(index, PricingPackage.Literals.MARKET_INDEX__BID_CURVE);
					statuses.add(dcsd);
				} else {
					final CommodityCurve cc = index.getBidCurve();
					checkCurve("Bid curve", ctx, statuses, index, cc);
				}
				if (index.getOfferCurve() == null) {
					final String message = String.format("Market index %s should have an offer curve!", index.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(index, PricingPackage.Literals.MARKET_INDEX__OFFER_CURVE);
					statuses.add(dcsd);
				} else {
					final CommodityCurve cc = index.getOfferCurve();
					checkCurve("Offer curve", ctx, statuses, index, cc);
				}
			}
		}
	}

	private void checkCurve(String curveType, IValidationContext ctx, List<IStatus> statuses, final MarketIndex index, final CommodityCurve cc) {
		if (cc.getMarketIndex() == null) {
			final String message = String.format("%s %s should have a market index set to %s!", curveType, cc.getName(), index.getName());
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dcsd.addEObjectAndFeature(cc, PricingPackage.Literals.COMMODITY_CURVE__MARKET_INDEX);
			statuses.add(dcsd);
		}
	}

}
