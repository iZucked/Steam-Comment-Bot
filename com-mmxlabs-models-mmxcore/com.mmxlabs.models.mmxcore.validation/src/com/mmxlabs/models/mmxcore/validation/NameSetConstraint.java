/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.validation;


import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * A constraint to check that id / name fields are all set (BugzID:288)
 * 
 * @author hinton
 * 
 */

//DUPLICATE FOPR FLEET AND CARGO

public class NameSetConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof NamedObject) {
			return validate(target, MMXCorePackage.eINSTANCE.getNamedObject_Name(), ctx);
		}

		return ctx.createSuccessStatus();
	}
	
	/**
	 * Check whether the given attribute on the target object is null, empty, or whitespace. If it is any of these, error out
	 * @param target
	 * @param attribute
	 * @param ctx
	 * @return validation status; success if attribute is a valid id/name
	 */
	private IStatus validate(final EObject target, final EAttribute attribute, final IValidationContext ctx) {
		final String name = (String) target.eGet(attribute);
		
		if (name == null || name.trim().isEmpty()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(target
							.eClass().getName(), attribute.getName()));
			dsd.addEObjectAndFeature(target, attribute);
			return dsd;
		}
		
		return ctx.createSuccessStatus();
	}
}
