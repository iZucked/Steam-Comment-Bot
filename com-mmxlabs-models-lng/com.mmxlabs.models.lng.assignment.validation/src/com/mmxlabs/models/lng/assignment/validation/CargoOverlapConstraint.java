/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoOverlapConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject object = ctx.getTarget();

		if (object instanceof ElementAssignment) {
			final ElementAssignment assignment = (ElementAssignment) object;

			final IExtraValidationContext extraValidationContext = Activator.getDefault().getExtraValidationContext();
			if (extraValidationContext == null) {
				return Activator.PLUGIN_ID;
			}
			final MMXRootObject rootObject = extraValidationContext.getRootObject();
			if (rootObject == null) {
				return Activator.PLUGIN_ID;
			}

			AssignmentModel assignmentModel = null;
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;
				final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
				if (portfolioModel != null) {
					assignmentModel = portfolioModel.getAssignmentModel();
				}
			}

			if (assignmentModel == null) {
				return Activator.PLUGIN_ID;
			}

			final UUIDObject assignedObject = assignment.getAssignedObject();
			final AVesselSet<Vessel> vesselAssignment = assignment.getAssignment();
			final int spotIndex = assignment.getSpotIndex();

			final Date startDate = getDate(assignedObject, true);
			final Date endDate = getDate(assignedObject, false);

			if (startDate == null || endDate == null) {
				return Activator.PLUGIN_ID;
			}

			for (final ElementAssignment elementAssignment : assignmentModel.getElementAssignments()) {
				if (extraValidationContext.getOriginal(assignment) == elementAssignment) {
					// Same element assignment!
					continue;
				}

				if (elementAssignment.getAssignment() != null && elementAssignment.getAssignment().equals(vesselAssignment) && elementAssignment.getSpotIndex() == spotIndex) {
					// Element assignment is on the same vessel

					final Date otherStartDate = getDate(elementAssignment.getAssignedObject(), true);
					final Date otherEndDate = getDate(elementAssignment.getAssignedObject(), false);

					boolean overlap = false;
					if (startDate.after(otherStartDate) && endDate.before(otherEndDate)) {
						overlap = true;
					} else if (startDate.before(otherStartDate) && endDate.after(otherEndDate)) {
						overlap = true;
					}
					if (overlap) {
						final String message = "Assignments on the same vessel completely overlap each other. Vessel assignment must be changed.";
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));

						failure.addEObjectAndFeature(elementAssignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());
						failure.addEObjectAndFeature(assignment, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment());

						statuses.add(failure);
					}
				}
			}

		}

		return Activator.PLUGIN_ID;
	}

	private Date getDate(final UUIDObject object, final boolean getStartDate) {
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			if (sortedSlots.isEmpty()) {
				return null;
			}
			if (getStartDate) {
				final Slot slot = sortedSlots.get(0);
				return slot.getWindowStartWithSlotOrPortTime();
			} else {
				final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
				return slot.getWindowEndWithSlotOrPortTime();
			}
		} else if (object instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) object;
			if (getStartDate) {
				return vesselEvent.getStartAfter();
			} else {
				vesselEvent.getStartBy();
			}
		}
		return null;
	}

}
