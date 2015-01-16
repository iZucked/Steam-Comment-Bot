/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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

/**
 * Validation constraint to check the type of vessel assigned. FOB/DES cargoes are either unassigned or a vessel not part of the "scenario" data - that is vessels in the FleetModel but do not have a
 * VesselAvailability. Fleet cargoes can be only scenario vessels or vessel class assignments. Vessel Events can only use scenario vessels.
 * 
 */
public class ElementAssignmentConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			Cargo cargo = null;
			if (assignableElement instanceof Cargo) {
				cargo = (Cargo) assignableElement;
			}

			if (cargo != null) {

				if (cargo.getCargoType() == CargoType.FLEET) {
					if (assignableElement.getVesselAssignmentType() == null) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Fleet cargo " + cargo.getLoadName()
								+ " has no vessel assignment. This may cause problems evaluating scenario."), IStatus.WARNING);
						failure.addEObjectAndFeature(assignableElement, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);

						failures.add(failure);

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
