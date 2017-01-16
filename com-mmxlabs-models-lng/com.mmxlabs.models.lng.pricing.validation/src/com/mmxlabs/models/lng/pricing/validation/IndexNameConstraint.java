/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class IndexNameConstraint extends AbstractModelMultiConstraint {
	private final Pattern pattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

	@Override
	protected String validate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraValidationContext, @NonNull List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof CommodityIndex) {
			final CommodityIndex index = (CommodityIndex) target;

			// Only check commodity indicies
			if (extraValidationContext != null) {
				final EReference containmentReference = extraValidationContext.getContainment(index);
				if (containmentReference != PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()) {
					return Activator.PLUGIN_ID;
				}
			}

			if ((index.getName() == null) || !pattern.matcher(index.getName()).matches()) {
				final String message = "Index '" + index.getName() + "': Name can only contain letters, numbers and underscores for use with pricing expressions.";
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(index, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				statuses.add(dcsd);
			}
		}

		return Activator.PLUGIN_ID;
	}

}
