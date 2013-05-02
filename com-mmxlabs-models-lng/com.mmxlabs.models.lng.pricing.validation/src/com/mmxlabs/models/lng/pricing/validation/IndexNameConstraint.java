/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class IndexNameConstraint extends AbstractModelConstraint {
	private final Pattern pattern = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof Index<?>) {
			final Index<?> index = (Index<?>) target;

			if ((index.getName() == null) || !pattern.matcher(index.getName()).matches()) {
				final String message = "Index '" + index.getName() + "': Name can only contain letters, numbers and underscores.";
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(index, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
				return dcsd;
			}
		}

		return ctx.createSuccessStatus();
	}

}
