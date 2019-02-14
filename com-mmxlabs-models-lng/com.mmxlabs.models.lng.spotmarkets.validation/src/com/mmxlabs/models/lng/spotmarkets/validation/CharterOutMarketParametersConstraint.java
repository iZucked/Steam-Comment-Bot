/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.validation;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.spotmarkets.validation.internal.Activator;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * @author FM
 * 
 */
public class CharterOutMarketParametersConstraint extends AbstractModelMultiConstraint {
	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof CharterOutMarketParameters) {

			final CharterOutMarketParameters charterOutMarketParameters = (CharterOutMarketParameters) target;
			
			final LocalDate startDate = charterOutMarketParameters.getCharterOutStartDate();
			final LocalDate endDate = charterOutMarketParameters.getCharterOutEndDate();
			
			boolean sensible = false;
			
			if ((startDate == null) && (endDate == null)) {
				return Activator.PLUGIN_ID;
			}
			
			if (endDate != null) {
				sensible = startDate != null ? startDate.isBefore(endDate) : true;
			}

			if (!sensible) {
				final String message = "Charter out market start date is before the end date!";
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(charterOutMarketParameters, SpotMarketsPackage.eINSTANCE.getCharterOutMarketParameters_CharterOutEndDate());
				dcsd.addEObjectAndFeature(charterOutMarketParameters, SpotMarketsPackage.eINSTANCE.getCharterOutMarketParameters_CharterOutEndDate());
				statuses.add(dcsd);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
