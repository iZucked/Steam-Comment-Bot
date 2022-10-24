/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ExistingCharterMarketOption;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ExistingCharterMarketOptionConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof final ExistingCharterMarketOption option) {

			final CharterInMarket charterInMarket = option.getCharterInMarket();
			if (charterInMarket == null) {
				final String message = "No market specified";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET);
				failures.add(dsd);
			}
			if (option.getSpotIndex() < -1) {
				final String message = "Invalid spot index specified";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
				failures.add(dsd);
			}

			if (charterInMarket != null) {
				if (option.getSpotIndex() == -1 && !charterInMarket.isNominal()) {
					final String message = "Nominal vessels not enabled for market";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
					failures.add(dsd);
				}
				if (charterInMarket.isEnabled() && option.getSpotIndex() >= 0 && option.getSpotIndex() >= charterInMarket.getSpotCharterCount()) {
					final String message = "Market option larger than available options";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
					failures.add(dsd);
				}
				if (!charterInMarket.isEnabled() && option.getSpotIndex() >= 0) {
					final String message = "Market option assigned to disable market";
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
					failures.add(dsd);
				}

			}
		}
	}
}
