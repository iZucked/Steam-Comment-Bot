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
import com.mmxlabs.models.lng.analytics.CharterOutOpportunity;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CharterOutOpportunityConstraint extends AbstractModelMultiConstraint {
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof final CharterOutOpportunity charterOutOpportunity) {

			if (!charterOutOpportunity.eIsSet(AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__HIRE_COST)) {
				final String message = "Hire cost must be non-zero";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(charterOutOpportunity, AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__HIRE_COST);
				failures.add(dsd);
			}
			if (charterOutOpportunity.getDate() == null) {
				final String message = "No date specified.";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(charterOutOpportunity, AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__DATE);
				failures.add(dsd);
			}
			if (charterOutOpportunity.getPort() == null) {
				final String message = "No port specified";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(charterOutOpportunity, AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__PORT);
				failures.add(dsd);
			}
			if (!charterOutOpportunity.eIsSet(AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__DURATION)) {
				final String message = "Duration must be non-zero";
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

				dsd.addEObjectAndFeature(charterOutOpportunity, AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__DURATION);
				failures.add(dsd);
			}
		}
	}
}
