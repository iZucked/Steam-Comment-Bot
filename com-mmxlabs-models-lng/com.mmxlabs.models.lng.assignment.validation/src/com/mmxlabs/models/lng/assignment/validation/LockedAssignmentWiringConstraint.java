/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LockedAssignmentWiringConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement elementAssignment = (AssignableElement) object;
			if (elementAssignment instanceof Cargo) {
				Cargo cargo = (Cargo) elementAssignment;
				// Locked only applies to fleet cargoes
				if (cargo.getCargoType() == CargoType.FLEET) {
					if (cargo.isAllowRewiring()) {
						if (elementAssignment.isLocked()) {

							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getLoadName()
									+ " is locked to a vessel, but permits re-wiring."));

							failure.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_AllowRewiring());
							failure.addEObjectAndFeature(elementAssignment, CargoPackage.eINSTANCE.getAssignableElement_Locked());

							failures.add(failure);
						}
					}
					if (elementAssignment.isLocked() && elementAssignment.getVesselAssignmentType() == null) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getLoadName()
								+ " is locked to a vessel, but no vessel is assigned."));

						failure.addEObjectAndFeature(elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED);
						failure.addEObjectAndFeature(elementAssignment, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

						failures.add(failure);
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
