/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.assignment.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A constraint to problem EMF level support for the TimeSortConstraintChecker to avoid getting scenarios which do not optimise when there is certain kinds of lateness.
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleLatenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof AssignmentModel) {
			final AssignmentModel inputModel = (AssignmentModel) target;

			final MMXRootObject rootObject = Activator.getDefault().getExtraValidationContext().getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
				final FleetModel fleetModel = scenarioModel.getFleetModel();
				final ScenarioFleetModel scenarioFleetModel = scenarioModel.getPortfolioModel().getScenarioFleetModel();

				final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(inputModel, fleetModel, scenarioFleetModel);

				final Set<UUIDObject> problemObjects = new HashSet<UUIDObject>();

				// Check sequencing for each grouping
				for (final CollectedAssignment collectedAssignment : collectAssignments) {
					UUIDObject prevAssignment = null;
					for (final UUIDObject assignment : collectedAssignment.getAssignedObjects()) {
						final Date left = getEndDate(prevAssignment);
						final Date right = getStartDate(assignment);

						if (left != null && right != null) {
							if (left.after(right)) {
								// Uh oh, likely to be an error
								final int severity = (problemObjects.contains(prevAssignment) || problemObjects.contains(assignment)) ? IStatus.ERROR : IStatus.WARNING;

								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
										"%s and %s overlap causing too much lateness. Change dates or vessel.", getID(prevAssignment), getID(assignment))), severity);
								addEndDateFeature(failure, prevAssignment);
								addStartDateFeature(failure, assignment);

								statuses.add(failure);

								problemObjects.add(prevAssignment);
								problemObjects.add(assignment);
							}
						}

						prevAssignment = assignment;
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}

	private void addStartDateFeature(final DetailConstraintStatusDecorator failure, final UUIDObject uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final Slot slot = cargo.getSortedSlots().get(0);
			failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			failure.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_StartBy());
		}
	}

	private void addEndDateFeature(final DetailConstraintStatusDecorator failure, final UUIDObject uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
			failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			failure.addEObjectAndFeature(vesselEvent, FleetPackage.eINSTANCE.getVesselEvent_StartAfter());
		}
	}

	private Date getStartDate(final UUIDObject uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
			return slot.getWindowStartWithSlotOrPortTime();
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(vesselEvent.getTimeZone(FleetPackage.eINSTANCE.getVesselEvent_StartBy())));
			calendar.setTime(vesselEvent.getStartBy());
			return calendar.getTime();
		}
		return null;
	}

	private Date getEndDate(final UUIDObject uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
			return slot.getWindowEndWithSlotOrPortTime();
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(vesselEvent.getTimeZone(FleetPackage.eINSTANCE.getVesselEvent_StartAfter())));
			calendar.setTime(vesselEvent.getStartAfter());
			return calendar.getTime();
		}
		return null;
	}

	private String getID(final UUIDObject uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			return "Cargo " + cargo.getName();
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			return "Event " + vesselEvent.getName();
		}
		return "(unknown)";
	}
}
