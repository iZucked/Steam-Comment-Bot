/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;

import scenario.cargo.Slot;

/**
 * A model constraint for checking that a slot's minimum and maximum volumes are
 * sensible (0 <= min <= max)
 * 
 * @author Tom Hinton
 * 
 */
public class SlotVolumeConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse
	 * .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		System.err.println("Validating " + object);
		if (object instanceof Slot) {
			final EMFEventType eventType = ctx.getEventType();
			if (eventType == EMFEventType.NULL) {
				// This is being triggered by a batch mode validation.
				final Slot slot = (Slot) object;
				//TODO return some placeholders for the error message
				if (slot.getMinQuantity() < 0) {
					return ctx.createFailureStatus();					
				}
				if (slot.getMaxQuantity() < 0) {
					return ctx.createFailureStatus();
				}
				if (slot.getMinQuantity() > slot.getMaxQuantity()) {
					return ctx.createFailureStatus();
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
