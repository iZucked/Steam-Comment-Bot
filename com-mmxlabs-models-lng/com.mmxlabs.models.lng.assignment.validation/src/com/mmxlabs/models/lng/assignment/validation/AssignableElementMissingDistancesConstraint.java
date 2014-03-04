/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.editor.utils.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.editor.utils.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

import static com.mmxlabs.models.lng.cargo.CargoPackage.Literals.*;

/**
 * A constraint to problem EMF level support for the TimeSortConstraintChecker to avoid getting scenarios which do not optimise when there is certain kinds of lateness.
 * 
 * @author Simon Goodall
 * 
 */
public class AssignableElementMissingDistancesConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		final MMXRootObject rootObject = Activator.getDefault().getExtraValidationContext().getRootObject();
		if (target instanceof CargoModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final FleetModel fleetModel = scenarioModel.getFleetModel();
			final CargoModel cargoModel = scenarioModel.getPortfolioModel().getCargoModel();

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, fleetModel);

			@SuppressWarnings("unchecked")
			Map<Pair<Port, Port>, Boolean> hasDistanceMap = (Map<Pair<Port, Port>, Boolean>) ctx.getCurrentConstraintData();
			if (hasDistanceMap == null) {
				hasDistanceMap = checkDistances(((LNGScenarioModel) rootObject).getPortModel());
				ctx.putCurrentConstraintData(hasDistanceMap);
			}

			// Check sequencing for each grouping
			for (final CollectedAssignment collectedAssignment : collectAssignments) {

				// Find end port(s)
				Port prevPort = null;
				EObject prevObject = null;
				EStructuralFeature prevFeature = null;

				Port startPort = null;
				Set<Port> endPorts = null;
				if (collectedAssignment.getVesselOrClass() instanceof Vessel) {
					// Find start port
					final Vessel vessel = (Vessel) collectedAssignment.getVesselOrClass();

					VesselAvailability vesselAvailability = null;
					for (final VesselAvailability va : cargoModel.getVesselAvailabilities()) {
						if (va.getVessel() == vessel) {
							vesselAvailability = va;
							break;

						}
					}
					if (vesselAvailability != null) {
						final Set<Port> startPorts = SetUtils.getObjects(vesselAvailability.getStartAt());
						if (startPorts.size() == 1) {
							startPort = startPorts.iterator().next();
							prevPort = startPort;
							prevObject = vesselAvailability;
							prevFeature = CargoPackage.Literals.VESSEL_AVAILABILITY__START_AT;
						}
						endPorts = SetUtils.getObjects(vesselAvailability.getEndAt());
					}
				}

				// Find end port(s)

				for (final AssignableElement assignment : collectedAssignment.getAssignedObjects()) {

					if (assignment instanceof Cargo) {
						final Cargo cargo = (Cargo) assignment;
						for (final Slot slot : cargo.getSortedSlots()) {
							final Port currentPort = slot.getPort();
							if (prevPort != null) {
								if (!hasDistance(hasDistanceMap, prevPort, currentPort)) {
									reportError(ctx, prevObject, prevFeature, prevPort, slot, CargoPackage.Literals.SLOT__PORT, currentPort, statuses);
								}
								prevPort = currentPort;
								prevObject = slot;
								prevFeature = CargoPackage.Literals.SLOT__PORT;
							}
						}

						// Loop over all sorted slots
					} else if (assignment instanceof CharterOutEvent) {
						final CharterOutEvent charterOutEvent = (CharterOutEvent) assignment;

						final Port currentPort = charterOutEvent.getPort();
						if (prevPort != null) {
							if (!hasDistance(hasDistanceMap, prevPort, currentPort)) {
								reportError(ctx, prevObject, prevFeature, prevPort, charterOutEvent, CargoPackage.Literals.VESSEL_EVENT__PORT, currentPort, statuses);
							}
							prevPort = currentPort;
							prevObject = charterOutEvent;
							prevFeature = CargoPackage.Literals.VESSEL_EVENT__PORT;
						}
						// check reposition port
						if (charterOutEvent.isSetRelocateTo()) {
							prevPort = charterOutEvent.getRelocateTo();
							prevObject = charterOutEvent;
							prevFeature = CargoPackage.Literals.CHARTER_OUT_EVENT__RELOCATE_TO;
						}
					} else if (assignment instanceof VesselEvent) {
						final VesselEvent vesselEvent = (VesselEvent) assignment;
						// single port
						final Port currentPort = vesselEvent.getPort();
						if (prevPort != null) {
							if (!hasDistance(hasDistanceMap, prevPort, currentPort)) {
								reportError(ctx, prevObject, prevFeature, prevPort, vesselEvent, CargoPackage.Literals.VESSEL_EVENT__PORT, currentPort, statuses);
							}
							prevPort = currentPort;
							prevObject = vesselEvent;
							prevFeature = CargoPackage.Literals.VESSEL_EVENT__PORT;
						}
					} else {
						// ??
					}

				}
			}

		}
		return Activator.PLUGIN_ID;
	}

	private void reportError(final IValidationContext ctx, final EObject fromObject, final EStructuralFeature fromFeature, final Port fromPort, final EObject toObject,
			final EStructuralFeature toFeature, final Port toPort, final List<IStatus> statuses) {

		final String msg = String.format("Missing distance: %s to %s (%s to %s).", fromPort.getName(),toPort.getName(), getID(fromObject, fromFeature), getID(toObject, toFeature));
		final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
		failure.addEObjectAndFeature(fromObject, fromFeature);
		failure.addEObjectAndFeature(toObject, toFeature);

		statuses.add(failure);
	}

	private String getID(final EObject target, final EStructuralFeature feature) {
		if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			return "slot \"" + slot.getName() + "\"";
		} else if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;
			return "event \"" + vesselEvent.getName() + "\"";
		} else if (target instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) target;
			final Vessel vessel = vesselAvailability.getVessel();
			final String vesselName = vessel == null ? "Vessel <unnamed>" : "\"" + vessel.getName() + "\"";
			String featureString = "";
			if(feature == VESSEL_AVAILABILITY__START_AT){ 
				featureString = " start ";
			} else if(feature == VESSEL_AVAILABILITY__END_AT){ 
				featureString = " end ";
			}
			return (featureString == "" ? "Vessel " + vesselName : vesselName + featureString);
		}
		return "(unknown)";
	}

	private Map<Pair<Port, Port>, Boolean> checkDistances(final PortModel portModel) {

		final Map<Pair<Port, Port>, Boolean> hasDistanceMap = new HashMap<>();
		for (final Route route : portModel.getRoutes()) {
			for (final RouteLine rl : route.getLines()) {
				if (rl.getDistance() != Integer.MAX_VALUE && rl.getDistance() >= 0) {
					hasDistanceMap.put(new Pair<>(rl.getFrom(), rl.getTo()), Boolean.TRUE);
				}
			}
		}
		return hasDistanceMap;
	}

	private boolean hasDistance(final Map<Pair<Port, Port>, Boolean> map, final Port from, final Port to) {
		final Pair<Port, Port> key = new Pair<>(from, to);
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return false;
	}
}
