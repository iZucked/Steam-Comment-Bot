/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.lng.analytics.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterOutOpportunityConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof CharterOutOpportunity) {
			final CharterOutOpportunity charterOutOpportunity = (CharterOutOpportunity) target;

			if (charterOutOpportunity.getDate() == null) {
				final String message = "No date specified.";
				DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(charterOutOpportunity, AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__DATE);
				failures.add(dsd);
			}
			if (charterOutOpportunity.getPort() == null) {
				final String message = "No port specified";
				DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(charterOutOpportunity, AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__PORT);
				failures.add(dsd);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
