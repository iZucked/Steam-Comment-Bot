/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class LockedAssignmentWiringConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (object instanceof ElementAssignment) {
			final ElementAssignment elementAssignment = (ElementAssignment) object;

			final UUIDObject assignedObject = elementAssignment.getAssignedObject();
			if (assignedObject instanceof Cargo) {
				Cargo cargo = (Cargo) assignedObject;
				if (cargo.isAllowRewiring()) {
					if (elementAssignment.isLocked()) {

						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getName()
								+ " is locked to a vessel, but permits re-wiring."));

						failure.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_AllowRewiring());
						failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Locked());

						failures.add(failure);
					}
				}
				if (elementAssignment.getAssignment() == null && elementAssignment.isLocked()) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(cargo.getName()
							+ " is locked to a vessel, but no vessel is assigned."));

					failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Locked());
					failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
					failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_AssignedObject());

					failures.add(failure);
				}
			}
		}

		if (failures.isEmpty()) {
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}
}
