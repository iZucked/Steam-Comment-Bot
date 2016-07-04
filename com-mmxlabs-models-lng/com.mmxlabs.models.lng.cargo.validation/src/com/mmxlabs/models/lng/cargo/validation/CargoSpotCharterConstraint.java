/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * @author Simon Goodall
 * 
 */
public class CargoSpotCharterConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
			if (vesselAssignmentType instanceof CharterInMarket) {
				final CharterInMarket charterInMarket = (CharterInMarket) vesselAssignmentType;
				if (cargo.getSpotIndex() >= charterInMarket.getSpotCharterCount()) {
					final String message = String.format("Cargo '%s'| Spot option is higher than the number of options available for charter in market (%s).", cargo.getLoadName(),
							charterInMarket.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(target, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
					statuses.add(dcsd);
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
