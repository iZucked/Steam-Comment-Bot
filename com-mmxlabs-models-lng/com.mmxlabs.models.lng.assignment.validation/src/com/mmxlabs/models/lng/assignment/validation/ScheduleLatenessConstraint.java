/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

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
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
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
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel,  spotMarketsModel);

			final List<Pair<AssignableElement, AssignableElement>> problems = new LinkedList<>();

			// Check sequencing for each grouping
			for (final CollectedAssignment collectedAssignment : collectAssignments) {

				if (collectedAssignment.isSpotVessel() && collectedAssignment.getSpotIndex() == -1) {
					continue;
				}

				AssignableElement prevAssignment = null;
				for (final AssignableElement assignment : collectedAssignment.getAssignedObjects()) {
					final ZonedDateTime left = getEndDate(prevAssignment);
					final ZonedDateTime right = getStartDate(assignment, false);

					if (left != null && right != null) {
						if (left.isAfter(right)) {
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
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("%s and %s overlap causing too much lateness. Change dates or vessel.", getID(p.getFirst()), getID(p.getSecond()))),
						severity);
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

	private ZonedDateTime getStartDate(final AssignableElement uuidObject, boolean isStartOfWindow) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(0);
			if (slot instanceof SpotSlot) {
				return null;
			}
			return isStartOfWindow ? slot.getWindowStartWithSlotOrPortTime() : slot.getWindowEndWithSlotOrPortTimeWithFlex();
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			return vesselEvent.getStartByAsDateTime();
		}
		return null;
	}

	private ZonedDateTime getEndDate(final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			final EList<Slot> sortedSlots = cargo.getSortedSlots();
			final Slot slot = sortedSlots.get(sortedSlots.size() - 1);
			if (slot instanceof SpotSlot) {
				return null;
			}
			final ZonedDateTime windowStartWithSlotOrPortTime = slot.getWindowStartWithSlotOrPortTimeWithFlex();
			if (windowStartWithSlotOrPortTime != null) {
				return windowStartWithSlotOrPortTime.plusHours(slot.getSlotOrPortDuration());
			}
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			return vesselEvent.getStartAfterAsDateTime();
		}
		return null;
	}

	private String getID(final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo) {
			final Cargo cargo = (Cargo) uuidObject;
			return "Cargo " + cargo.getLoadName();
		} else if (uuidObject instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) uuidObject;
			return "Event " + vesselEvent.getName();
		}
		return "(unknown)";
	}
}
