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
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ElementAssignmentConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		final IExtraValidationContext evc = Activator.getDefault().getExtraValidationContext();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (object instanceof ElementAssignment) {
			final ElementAssignment elementAssignment = (ElementAssignment) object;

			final UUIDObject assignedObject = elementAssignment.getAssignedObject();

			if (assignedObject == null) {
				return ctx.createSuccessStatus();
			}

			if (assignedObject instanceof Cargo) {
				return ctx.createSuccessStatus();
			}

			if (assignedObject instanceof VesselEvent) {
				return ctx.createSuccessStatus();
			}

			{
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Element Assignment has unexpected assigned object of type %s. Expected Cargo or VesselEvent object type.", assignedObject.eClass().getName())));
				failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_AssignedObject());

				failures.add(failure);
			}

			final AVesselSet<Vessel> vessel = elementAssignment.getAssignment();

			boolean found = false;

			if (vessel instanceof Vessel) {
				final LNGScenarioModel root = (LNGScenarioModel) evc.getRootObject();

				final ScenarioFleetModel fleet = root.getPortfolioModel().getScenarioFleetModel();
				for (final VesselAvailability availability : fleet.getVesselAvailabilities()) {
					if (availability.getVessel().equals(vessel)) {
						found = true;
						break;
					}
				}

				if (!found) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
							"Element Assignment assigns an element for an unavailable vessel %s.", vessel.getName())));
					failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.Literals.ELEMENT_ASSIGNMENT__ASSIGNMENT);

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
