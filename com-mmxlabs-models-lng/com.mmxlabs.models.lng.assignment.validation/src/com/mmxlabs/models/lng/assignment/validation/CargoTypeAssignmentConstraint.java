/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoTypeAssignmentConstraint extends AbstractModelConstraint {
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
			final ElementAssignment assignment = (ElementAssignment) object;

			final UUIDObject uuidObject = assignment.getAssignedObject();
			if (assignment.getAssignment() == null) {
				return ctx.createSuccessStatus();
			}
			final Set<Vessel> vessels = SetUtils.getObjects(assignment.getAssignment());

			Cargo cargo = null;
			if (uuidObject instanceof LoadSlot) {
				cargo = ((LoadSlot) uuidObject).getCargo();
			} else if (uuidObject instanceof DischargeSlot) {
				cargo = ((DischargeSlot) uuidObject).getCargo();
			} else if (uuidObject instanceof Cargo) {
				cargo = (Cargo) uuidObject;
			}
//			if (cargo != null) {
//				if (cargo.getCargoType() != CargoType.FLEET) {
//					if (!vessels.isEmpty()) {
//						
//						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("None fleet cargo " + cargo.getName()
//								+ " is assigned to a vessel " + vessels.toString()));
//						failure.addEObjectAndFeature(cargo, MMXCorePackage.eINSTANCE.getNamedObject_Name());
//
//						failures.add(failure);
//					}
//				}
//			}
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
