/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

/**
 * A constraint which checks that cargos are not being carried by vessels
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
