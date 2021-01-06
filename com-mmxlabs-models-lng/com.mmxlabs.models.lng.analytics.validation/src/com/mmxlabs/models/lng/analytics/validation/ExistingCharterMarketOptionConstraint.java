/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ExistingCharterMarketOptionConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof ExistingCharterMarketOption) {
			final ExistingCharterMarketOption option = (ExistingCharterMarketOption) target;

			if (option.getCharterInMarket() == null) {
				final String message = "No market specified";
				DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__CHARTER_IN_MARKET);
				failures.add(dsd);
			}
			if (option.getSpotIndex() < -1) {
				final String message = "Invalid spot index specified";
				DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(option, AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX);
				failures.add(dsd);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
