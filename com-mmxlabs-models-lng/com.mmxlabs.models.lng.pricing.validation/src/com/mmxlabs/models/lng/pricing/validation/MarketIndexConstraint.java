/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class MarketIndexConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraValidationContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_GENERATED_PAPER_DEALS)) {
			if (target instanceof MarketIndex) {
				final MarketIndex index = (MarketIndex) target;
				if (index.getBidCurve() == null) {
					final String message = String.format("Market index %s should have a bid curve!", index.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(index, PricingPackage.Literals.MARKET_INDEX__BID_CURVE);
					statuses.add(dcsd);
				}
				if (index.getOfferCurve() == null) {
					final String message = String.format("Market index %s should have an offer curve!", index.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(index, PricingPackage.Literals.MARKET_INDEX__OFFER_CURVE);
					statuses.add(dcsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
