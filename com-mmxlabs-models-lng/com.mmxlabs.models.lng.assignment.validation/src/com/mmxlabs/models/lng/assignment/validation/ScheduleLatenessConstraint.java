/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
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
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint to problem EMF level support for the TimeSortConstraintChecker
 * to avoid getting scenarios which do not optimise when there is certain kinds
 * of lateness.
 * 
 * @author Simon Goodall
 * 
 */
public class ScheduleLatenessConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof LNGScenarioModel scenarioModel) {
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
			final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(scenarioModel);
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
			final ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);
			if (collectAssignments == null) {
				return;
			}
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
							problems.add(new Pair<>(prevAssignment, assignment));
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
	}

	private void addStartDateFeature(final DetailConstraintStatusDecorator failure, final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo cargo) {
			final Slot<?> slot = cargo.getSortedSlots().get(0);
			failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else if (uuidObject instanceof VesselEvent vesselEvent) {
			failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartBy());
		}
	}

	private void addEndDateFeature(final DetailConstraintStatusDecorator failure, final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo cargo) {
			final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			final Slot<?> slot = sortedSlots.get(sortedSlots.size() - 1);
			failure.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
		} else if (uuidObject instanceof VesselEvent vesselEvent) {
			failure.addEObjectAndFeature(vesselEvent, CargoPackage.eINSTANCE.getVesselEvent_StartAfter());
		}
	}

	private ZonedDateTime getStartDate(final AssignableElement uuidObject, boolean isStartOfWindow) {
		if (uuidObject instanceof Cargo cargo) {
			final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			final Slot<?> slot = sortedSlots.get(0);
			if (slot instanceof SpotSlot) {
				return null;
			}
			return isStartOfWindow ? slot.getSchedulingTimeWindow().getStart() : slot.getSchedulingTimeWindow().getEndWithFlex();
		} else if (uuidObject instanceof VesselEvent vesselEvent) {
			return vesselEvent.getStartByAsDateTime();
		}
		return null;
	}

	private ZonedDateTime getEndDate(final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo cargo) {
			final List<Slot<?>> sortedSlots = cargo.getSortedSlots();
			final Slot<?> slot = sortedSlots.get(sortedSlots.size() - 1);
			if (slot instanceof SpotSlot) {
				return null;
			}
			final ZonedDateTime windowStartWithSlotOrPortTime = slot.getSchedulingTimeWindow().getStartWithFlex();
			if (windowStartWithSlotOrPortTime != null) {
				return windowStartWithSlotOrPortTime.plusHours(slot.getSchedulingTimeWindow().getDuration());
			}
		} else if (uuidObject instanceof VesselEvent vesselEvent) {
			return vesselEvent.getStartAfterAsDateTime();
		}
		return null;
	}

	private String getID(final AssignableElement uuidObject) {
		if (uuidObject instanceof Cargo cargo) {
			return "Cargo " + cargo.getLoadName();
		} else if (uuidObject instanceof VesselEvent vesselEvent) {
			return "Event " + vesselEvent.getName();
		}
		return "(unknown)";
	}
}
