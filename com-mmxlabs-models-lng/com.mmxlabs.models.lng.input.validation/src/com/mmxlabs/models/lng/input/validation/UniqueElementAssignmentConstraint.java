/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.validation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class UniqueElementAssignmentConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (object instanceof InputModel) {
			final InputModel inputModel = (InputModel) object;

			final Map<UUIDObject, ElementAssignment> seenObjects = new LinkedHashMap<UUIDObject, ElementAssignment>();
			for (final ElementAssignment assignment : inputModel.getElementAssignments()) {

				final UUIDObject assignedObject = assignment.getAssignedObject();
				if (assignedObject == null) {
					continue;
				}

				if (seenObjects.containsKey(assignedObject)) {

					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
							"Element Assignment already exists for UUID object %s. ", assignedObject.getUuid())));
					failure.addEObjectAndFeature(assignment, InputPackage.eINSTANCE.getElementAssignment_AssignedObject());

					failures.add(failure);
				}

				seenObjects.put(assignment.getAssignedObject(), assignment);
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
