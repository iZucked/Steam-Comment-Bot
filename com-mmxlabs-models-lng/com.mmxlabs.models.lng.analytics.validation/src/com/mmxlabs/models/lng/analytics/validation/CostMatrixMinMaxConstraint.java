/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.analytics.UnitCostMatrix;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CostMatrixMinMaxConstraint extends AbstractModelConstraint {
	
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		if (target instanceof UnitCostMatrix) {
			final UnitCostMatrix matrix = (UnitCostMatrix) target;
			String currentConstraintId = ctx.getCurrentConstraintId();
			final String attributes = currentConstraintId.substring((getClass().getCanonicalName() + ".").length());
			final String[] parts = attributes.split("\\.");
			if (parts.length == 2) {
				EAttribute a1 = null, a2 = null;
				for (final EAttribute attribute : target.eClass().getEAllAttributes()) {
					
					if (attribute.getName().equalsIgnoreCase(parts[0])) {
						a1 = attribute;
					} else if (attribute.getName().equalsIgnoreCase(parts[1])) {
						a2 = attribute;
					}
				}
				if (a1!= null && a2 != null) {
					Object first = matrix.eGet(a1);
					Object second = matrix.eGet(a2);
					if (first instanceof Comparable && second instanceof Comparable) {
						if (((Comparable)first).compareTo((Comparable)second) > 0) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus());
							dcsd.addEObjectAndFeature(matrix, a1);
							dcsd.addEObjectAndFeature(matrix, a2);
							return dcsd;
						}
					}
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
