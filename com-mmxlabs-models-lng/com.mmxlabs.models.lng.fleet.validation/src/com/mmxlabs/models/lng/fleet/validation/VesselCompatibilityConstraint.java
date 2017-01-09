/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

/**
 * A constraint which checks that cargoes are not being carried by vessels
 * which cannot do the job.
 * @author Tom Hinton
 *
 */
public class VesselCompatibilityConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(IValidationContext ctx) {
		// TODO Auto-generated method stub
		return null;
	}

}
