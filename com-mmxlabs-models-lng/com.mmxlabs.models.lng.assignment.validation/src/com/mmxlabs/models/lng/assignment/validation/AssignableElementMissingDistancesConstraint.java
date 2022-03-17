/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import static com.mmxlabs.models.lng.cargo.CargoPackage.Literals.VESSEL_AVAILABILITY__END_AT;
import static com.mmxlabs.models.lng.cargo.CargoPackage.Literals.VESSEL_AVAILABILITY__START_AT;

import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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
public class AssignableElementMissingDistancesConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		final MMXRootObject rootObject = extraContext.getRootObject();
		if (target instanceof CargoModel cargoModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);
			final ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider);
			if (collectAssignments == null) {
				return;
			}

			// Check sequencing for each grouping
			for (final CollectedAssignment collectedAssignment : collectAssignments) {

				// Find end port(s)
				Port prevPort = null;
				EObject prevObject = null;
				EStructuralFeature prevFeature = null;

				Port startPort = null;
				Set<Port> endPorts = null;
				VesselAvailability vesselAvailability = null;
				if (collectedAssignment.getVesselAvailability() != null) {
					// Find start port
					vesselAvailability = collectedAssignment.getVesselAvailability();
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

					if (assignment instanceof Cargo cargo) {
						for (final Slot<?> slot : cargo.getSortedSlots()) {
							final Port currentPort = slot.getPort();
							if (prevPort != null && currentPort != null) {
								if (!modelDistanceProvider.hasAnyDistance(prevPort, currentPort)) {
									reportError(ctx, prevObject, prevFeature, prevPort, slot, CargoPackage.Literals.SLOT__PORT, currentPort, statuses);
								}
							}
							prevPort = currentPort;
							prevObject = slot;
							prevFeature = CargoPackage.Literals.SLOT__PORT;
						}

						// Loop over all sorted slots
					} else if (assignment instanceof CharterOutEvent charterOutEvent) {

						final Port currentPort = charterOutEvent.getPort();
						if (prevPort != null && currentPort != null) {
							if (!modelDistanceProvider.hasAnyDistance(prevPort, currentPort)) {
								reportError(ctx, prevObject, prevFeature, prevPort, charterOutEvent, CargoPackage.Literals.VESSEL_EVENT__PORT, currentPort, statuses);
							}
						}
						prevPort = currentPort;
						prevObject = charterOutEvent;
						prevFeature = CargoPackage.Literals.VESSEL_EVENT__PORT;
						// check reposition port
						if (charterOutEvent.isSetRelocateTo()) {
							prevPort = charterOutEvent.getRelocateTo();
							prevObject = charterOutEvent;
							prevFeature = CargoPackage.Literals.CHARTER_OUT_EVENT__RELOCATE_TO;
						}
					} else if (assignment instanceof VesselEvent vesselEvent) {
						// single port
						final Port currentPort = vesselEvent.getPort();
						if (prevPort != null && currentPort != null) {
							if (!modelDistanceProvider.hasAnyDistance(prevPort, currentPort)) {
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

				// Check end port
				if (prevPort != null && endPorts != null && !endPorts.isEmpty()) {
					boolean foundDistance = false;
					for (final Port p : endPorts) {

						if (modelDistanceProvider.hasAnyDistance(prevPort, p)) {
							foundDistance = true;
							break;
						}
					}
					if (!foundDistance) {

						final String msg = String.format("Missing distance between port (%s) and vessel (%s) end port(s) and (%s to %s).", getPortName(prevPort), getVesselName(vesselAvailability),
								getID(prevObject, prevFeature), getID(vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AT));
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						failure.addEObjectAndFeature(prevPort, prevFeature);
						failure.addEObjectAndFeature(vesselAvailability, CargoPackage.Literals.VESSEL_AVAILABILITY__END_AT);

						statuses.add(failure);
					}
				}
			}
		}
	}

	private void reportError(final IValidationContext ctx, final EObject fromObject, final EStructuralFeature fromFeature, final Port fromPort, final EObject toObject,
			final EStructuralFeature toFeature, final Port toPort, final List<IStatus> statuses) {

		final String msg = String.format("Missing distance: %s to %s (%s to %s).", getPortName(fromPort), getPortName(toPort), getID(fromObject, fromFeature), getID(toObject, toFeature));
		final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
		failure.addEObjectAndFeature(fromObject, fromFeature);
		failure.addEObjectAndFeature(toObject, toFeature);

		statuses.add(failure);
	}

	private String getPortName(final Port port) {
		if (port == null) {
			return "<Unspecified port>";
		}
		return port.getName();
	}

	private String getVesselName(final VesselAvailability vesselAvailability) {
		if (vesselAvailability == null) {
			return "<Unspecified vessel>";
		}

		final Vessel vessel = vesselAvailability.getVessel();
		if (vessel == null) {
			return "<Unspecified vessel>";
		}
		return vessel.getName();
	}

	private String getID(final EObject target, final EStructuralFeature feature) {
		if (target instanceof Slot<?> slot) {
			return "slot \"" + slot.getName() + "\"";
		} else if (target instanceof VesselEvent vesselEvent) {
			return "event \"" + vesselEvent.getName() + "\"";
		} else if (target instanceof VesselAvailability vesselAvailability) {
			final Vessel vessel = vesselAvailability.getVessel();
			final String vesselName = vessel == null ? "Vessel <unnamed>" : "\"" + vessel.getName() + "\"";
			String featureString = "";
			if (feature == VESSEL_AVAILABILITY__START_AT) {
				featureString = " start ";
			} else if (feature == VESSEL_AVAILABILITY__END_AT) {
				featureString = " end ";
			}
			return ("".equals(featureString) ? "Vessel " + vesselName : vesselName + featureString);
		}
		return "(unknown)";
	}
}
