/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class NonZeroConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		final String id = ctx.getCurrentConstraintId();
		final String last = id.substring((getClass().getCanonicalName() + ".").length());
		for (final EAttribute attribute : target.eClass().getEAllAttributes()) {
			if (attribute.getName().equalsIgnoreCase(last)) {
				if (!attribute.isUnsettable() || target.eIsSet(attribute)) {
					final Object o = target.eGet(attribute);
					if (o instanceof Number) {
						final Number n = (Number)o;
						if (n.doubleValue() == 0) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());
							dcsd.addEObjectAndFeature(target, attribute);
							return dcsd;
						}
					}
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
