/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint to problem EMF level support for the TimeSortConstraintChecker to avoid getting scenarios which do not optimise when there is certain kinds of lateness.
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleLatenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof LNGScenarioModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) target;
			final FleetModel fleetModel = scenarioModel.getFleetModel();
			final CargoModel cargoModel = scenarioModel.getPortfolioModel().getCargoModel();

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, fleetModel);

			final List<Pair<AssignableElement, AssignableElement>> problems = new LinkedList<>();

			// Check sequencing for each grouping
			for (final CollectedAssignment collectedAssignment : collectAssignments) {
				AssignableElement prevAssignment = null;
				for (final AssignableElement assignment : collectedAssignment.getAssignedObjects()) {
					final Date left = getEndDate(prevAssignment);
					final Date right = getStartDate(assignment);

					if (left != null && right != null) {
						if (left.after(right)) {
							// Uh oh, likely to be an error
							problems.add(new Pair<AssignableElement, AssignableElement>(prevAssignment, assignment));
						}
					}

					prevAssignment = assignment;
				}
			}

			// More than one problem is likely to be a problem for the optimiser
			final int severity = problems.size() > 1 ? IStatus.ERROR : IStatus.WARNING;
			for (final Pair<AssignableElement, AssignableElement> p : problems) {
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"%s and %s overlap causing too much lateness. Change dates or vessel.", getID(p.getFirst()), getID(p.getSecond()))), severity);
				addEndDateFeature(failure, p.getFirst());
				addStartDateFeature(failure, p.getSecond());

				statuses.add(failure);
			}
		}
		return Activator.PLUGIN_ID;
	}

	private void addStartDateFeature(final DetailConstraintStatusDecorator failure, final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final Slot slot = cargo.getSortedSlots().get(0);
			failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
		}
	}

	private void addEndDateFeature(final DetailConstraintStatusDecorator failure, final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
			failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
		}
	}

	private Date getStartDate(final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(0);
			return slot.getWindowStartWithSlotOrPortTime();
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(vesselEvent.getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartBy())));
			calendar.setTime(vesselEvent.getStartBy());
			return calendar.getTime();
		}
		return null;
	}

	private Date getEndDate(final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
			final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(slot.getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart())));
			final Date windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTime();
			if (windowStartWithSlotOrPortTime != null) {
				cal.setTime(windowStartWithSlotOrPortTime);
				cal.add(Calendar.HOUR_OF_DAY, slot.getSlotOrPortDuration());
				return cal.getTime();
			}
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(vesselEvent.getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartAfter())));
			final Date startAfter = vesselEvent.getStartAfter();
			if (startAfter != null) {
				calendar.setTime(startAfter);
				return calendar.getTime();
			}
		}
		return null;
	}

	private String getID(final AssignableElement uuidObject) {
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
