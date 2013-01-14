/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * @author Simon Goodall
 * @since 2.0
 * 
 */
public class CharterOutConstraint extends AbstractModelMultiConstraint {
	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof CharterOutEvent) {
			final CharterOutEvent charterOutEvent = (CharterOutEvent) target;

			if (charterOutEvent.getHireRate() == 0) {
				final String message = String.format("Charter out event %s has no hire cost set", charterOutEvent.getName());
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dcsd.addEObjectAndFeature(charterOutEvent, FleetPackage.eINSTANCE.getCharterOutEvent_HireRate());
				statuses.add(dcsd);
			}

		}
		return Activator.PLUGIN_ID;
	}
}
