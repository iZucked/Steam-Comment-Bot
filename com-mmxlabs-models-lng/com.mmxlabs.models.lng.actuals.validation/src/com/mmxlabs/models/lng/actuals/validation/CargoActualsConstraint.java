/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoActualsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof CargoActuals) {
			final CargoActuals cargo = (CargoActuals) object;

			if (cargo.getCargoReference() == null || cargo.getCargoReference().isEmpty()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Actuals needs cargo reference"));
				status.addEObjectAndFeature(cargo, ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE);
				failures.add(status);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
