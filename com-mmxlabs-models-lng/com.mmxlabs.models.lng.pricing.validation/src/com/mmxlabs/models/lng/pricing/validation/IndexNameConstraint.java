/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class IndexNameConstraint extends AbstractModelConstraint {
	private final Pattern pattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof CommodityIndex) {
			final CommodityIndex index = (CommodityIndex) target;

			// Only check commodity indicies
			final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
			if (extraValidationContext != null) {
				final EReference containmentReference = extraValidationContext.getContainment(index);
				if (containmentReference != PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()) {
					return ctx.createSuccessStatus();
				}
			}

			if ((index.getName() == null) || !pattern.matcher(index.getName()).matches()) {
				final String message = "Index '" + index.getName() + "': Name can only contain letters, numbers and underscores for use with pricing expressions.";
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(index, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				return dcsd;
			}
		}

		return ctx.createSuccessStatus();
	}

}
