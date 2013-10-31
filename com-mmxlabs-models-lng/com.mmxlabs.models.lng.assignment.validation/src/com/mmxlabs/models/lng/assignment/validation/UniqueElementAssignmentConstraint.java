/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class UniqueElementAssignmentConstraint extends AbstractModelMultiConstraint {

	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignmentModel) {
			final AssignmentModel assignmentModel = (AssignmentModel) object;

			final Map<UUIDObject, ElementAssignment> seenObjects = new LinkedHashMap<UUIDObject, ElementAssignment>();
			for (final ElementAssignment elementAssignment : assignmentModel.getElementAssignments()) {

				final UUIDObject assignedObject = elementAssignment.getAssignedObject();
				if (assignedObject == null) {
					continue;
				}
				if (seenObjects.containsKey(assignedObject)) {
					final String message;
					if (assignedObject instanceof Cargo) {
						message = String.format("Element Assignment already exists for Cargo %s.", ((Cargo) assignedObject).getName());
					} else if (assignedObject instanceof VesselEvent) {
						message = String.format("Element Assignment already exists for Vessel Event %s.", ((VesselEvent) assignedObject).getName());
					} else {
						message = String.format("Element Assignment already exists for UUID object %s.", assignedObject.getUuid());
					}
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_AssignedObject());

					failures.add(failure);
				}

				seenObjects.put(elementAssignment.getAssignedObject(), elementAssignment);
			}
		}
		return Activator.PLUGIN_ID;
	}
}
