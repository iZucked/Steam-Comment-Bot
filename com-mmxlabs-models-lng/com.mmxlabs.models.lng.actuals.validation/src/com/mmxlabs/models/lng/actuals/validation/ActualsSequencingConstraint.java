/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.validation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.actuals.util.ActualsAssignableDateProvider;
import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.util.AssignmentEditorHelper;
import com.mmxlabs.models.lng.cargo.util.CollectedAssignment;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.StartHeelOptions;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.util.ModelDistanceProvider;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.LNGScenarioSharedModelTypes;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.date.DateTimeFormatsProvider;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * This constraint ensures that an {@link CargoActuals} is preceded by another {@link CargoActuals}, or the vessel start. (or a vessel event with warning). This ensures proper heel tracking etc.
 * 
 * @author Simon Goodall
 * 
 */
public class ActualsSequencingConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		final MMXRootObject rootObject = extraContext.getRootObject();
		if (target instanceof ActualsModel actualsModel) {
			final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
			final SpotMarketsModel spotMarketsModel = scenarioModel.getReferenceModel().getSpotMarketsModel();
			final CargoModel cargoModel = scenarioModel.getCargoModel();
			final PortModel portModel = ScenarioModelUtil.getPortModel(scenarioModel);

			ModelDistanceProvider modelDistanceProvider = extraContext.getScenarioDataProvider().getExtraDataProvider(LNGScenarioSharedModelTypes.DISTANCES, ModelDistanceProvider.class);

			// Build up lookup tables
			final Map<Cargo, CargoActuals> cargoActualsMap = new HashMap<>();
			final Map<Slot, SlotActuals> slotActualsMap = new HashMap<>();
			final Map<LoadSlot, LoadActuals> loadActualsMap = new HashMap<>();
			final Map<DischargeSlot, DischargeActuals> dischargeActualsMap = new HashMap<>();

			for (final CargoActuals cargoActuals : actualsModel.getCargoActuals()) {
				final Cargo cargo = cargoActuals.getCargo();
				// Skip broken date
				if (cargo == null) {
					continue;
				}
				cargoActualsMap.put(cargo, cargoActuals);

				for (final Slot slot : cargo.getSlots()) {
					for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
						if (slot == slotActuals.getSlot()) {
							slotActualsMap.put(slot, slotActuals);
							if (slot instanceof LoadSlot && slotActuals instanceof LoadActuals) {
								loadActualsMap.put((LoadSlot) slot, (LoadActuals) slotActuals);
							} else if (slot instanceof DischargeSlot && slotActuals instanceof DischargeActuals) {
								dischargeActualsMap.put((DischargeSlot) slot, (DischargeActuals) slotActuals);
							}
						}
					}
				}
			}

			final List<CollectedAssignment> collectAssignments = AssignmentEditorHelper.collectAssignments(cargoModel, portModel, spotMarketsModel, modelDistanceProvider,
					new ActualsAssignableDateProvider(actualsModel));
			if (collectAssignments != null) {
				// Check sequencing for each grouping
				for (final CollectedAssignment collectedAssignment : collectAssignments) {
					VesselCharter va = collectedAssignment.getVesselCharter();

					// Assume vessel start is Actualised, but check state!
					boolean previousElementHasActuals = true;

					AssignableElement prevObject = null;
					CargoActuals prevActuals = null;
					for (final AssignableElement assignment : collectedAssignment.getAssignedObjects()) {
						boolean currentElementHasActuals = false;
						CargoActuals currentActuals = null;

						LoadActuals loadActuals = null;
						if (assignment instanceof Cargo) {
							final Cargo cargo = (Cargo) assignment;
							if (cargoActualsMap.containsKey(cargo)) {
								currentActuals = cargoActualsMap.get(cargo);
								{
									final Slot slot = currentActuals.getCargo().getSortedSlots().get(0);
									if (slot instanceof LoadSlot) {
										loadActuals = loadActualsMap.get(slot);
									}
								}
								currentElementHasActuals = true;
							}
						} else if (assignment instanceof VesselEvent) {
							currentElementHasActuals = false;
						}

						if (va != null && currentElementHasActuals && loadActuals != null) {

							final Port actualPort = loadActuals.getTitleTransferPoint();
							final ZonedDateTime actualOperationsStart = loadActuals.getOperationsStartAsDateTime();
							if (actualOperationsStart != null) {
								final Port startPort = va.getStartAt();
								if (startPort != null && !startPort.equals(actualPort)) {
									// Error
									final String msg = String.format("Actualised %s and vessel %s starting port do not match (%s - %s)", getID(assignment), getVesselName(va.getVessel()),
											getPortName(actualPort), getPortName(startPort));

									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), IStatus.ERROR);
									failure.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_CHARTER__START_AT);
									failure.addEObjectAndFeature(cargoActualsMap.get(assignment), ActualsPackage.Literals.SLOT_ACTUALS__TITLE_TRANSFER_POINT);

									statuses.add(failure);
								} else {
									// Too many ports, should be picked up by a different constraint.
								}
								if (va.isSetStartAfter() || va.isSetStartBy()) {
									// check dates match. Note start by/after is UTC, cargo/event local time.

									ZonedDateTime startAfterAsDateTime = va.getStartAfterAsDateTime();
									if (startAfterAsDateTime != null && !startAfterAsDateTime.isEqual(actualOperationsStart)) {
										final String msg = String.format("Actualised %s operations start date (%s in UTC) and vessel %s start date (%s) do not match", getID(assignment),
												getDateString(actualOperationsStart.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()), getVesselName(va.getVessel()),
												getDateString(va.getStartAfter()));

										final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), IStatus.ERROR);
										failure.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_CHARTER__START_AFTER);
										failure.addEObjectAndFeature(cargoActualsMap.get(assignment), ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);

										statuses.add(failure);

									}
									ZonedDateTime startByAsDateTime = va.getStartByAsDateTime();
									if (startByAsDateTime != null && !startByAsDateTime.isEqual(actualOperationsStart)) {
										final String msg = String.format("Actualised %s operations start date (%s in UTC) and vessel %s start date (%s) do not match", getID(assignment),
												getDateString(actualOperationsStart.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()), getVesselName(va.getVessel()),
												getDateString(va.getStartBy()));

										final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), IStatus.ERROR);
										failure.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_CHARTER__START_BY);
										failure.addEObjectAndFeature(cargoActualsMap.get(assignment), ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
										statuses.add(failure);

									}
								}

								final StartHeelOptions startHeel = va.getStartHeel();
								if (loadActuals.getStartingHeelM3() > 0 || startHeel.getMaxVolumeAvailable() > 0) {
									if (Math.abs(loadActuals.getCV() - startHeel.getCvValue()) > 0.0005) {
										final String msg = String.format("Actualised %s and vessel %s starting heel CV do not match (%.3f - %.3f)", getID(assignment),
												getVesselName(va.getVessel()), loadActuals.getCV(), startHeel.getCvValue());

										final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), IStatus.ERROR);
										failure.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_CHARTER__START_HEEL);
										failure.addEObjectAndFeature(startHeel, CommercialPackage.Literals.START_HEEL_OPTIONS__CV_VALUE);
										failure.addEObjectAndFeature(cargoActualsMap.get(assignment), ActualsPackage.Literals.SLOT_ACTUALS__CV);

										statuses.add(failure);

									}
								}
								if (loadActuals.getStartingHeelM3() < startHeel.getMinVolumeAvailable() || loadActuals.getStartingHeelM3() > startHeel.getMaxVolumeAvailable()) {
									// Error
									final String msg = String.format("Actualised %s and vessel %s starting heel quantities do not match (%,.3f - [%,.3f, %,.3f])", getID(assignment),
											getVesselName(va.getVessel()), (double) loadActuals.getStartingHeelM3(), (double) startHeel.getMinVolumeAvailable(),
											(double) startHeel.getMaxVolumeAvailable());

									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), IStatus.ERROR);
									failure.addEObjectAndFeature(va, CargoPackage.Literals.VESSEL_CHARTER__START_HEEL);
									failure.addEObjectAndFeature(startHeel, CommercialPackage.Literals.START_HEEL_OPTIONS__MIN_VOLUME_AVAILABLE);
									failure.addEObjectAndFeature(startHeel, CommercialPackage.Literals.START_HEEL_OPTIONS__MAX_VOLUME_AVAILABLE);
									failure.addEObjectAndFeature(cargoActualsMap.get(assignment), ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_M3);

									statuses.add(failure);
								}
							}
							// Reset this variable so no longer in scope for future iterations. We could
							// also use a "firstElement" boolean.
							va = null;
						}

						if (prevObject != null && previousElementHasActuals == false && currentElementHasActuals == true) {
							// Only warn about previous missing actuals if an event.
							final int status = /* prevObject instanceof VesselEvent ? IStatus.WARNING : */ IStatus.ERROR;

							final String msg = String.format("Cargo %s is not preceded by another actualised event.", getID(prevObject));

							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg), status);
							failure.addEObjectAndFeature(prevObject, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
							failure.addEObjectAndFeature(assignment, MMXCorePackage.Literals.NAMED_OBJECT__NAME);
							failure.addEObjectAndFeature(cargoActualsMap.get(assignment), MMXCorePackage.Literals.NAMED_OBJECT__NAME);

							statuses.add(failure);
						}

						// If current actuals is false, check the window!/port matches

						// Check previous return actuals data matches current data
						if (currentActuals != null && prevActuals != null) {
							final ReturnActuals returnActuals = prevActuals.getReturnActuals();

							if (returnActuals != null && loadActuals != null) {
								if (returnActuals.getTitleTransferPoint() != loadActuals.getTitleTransferPoint()) {
									final String message = String.format("Load actuals and previous return actuals %s does not match", "Port");
									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
									failure.addEObjectAndFeature(loadActuals, ActualsPackage.Literals.SLOT_ACTUALS__TITLE_TRANSFER_POINT);
									failure.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__TITLE_TRANSFER_POINT);
									statuses.add(failure);

								}
								if (Math.abs(returnActuals.getEndHeelM3() - loadActuals.getStartingHeelM3()) > 0.0005) {
									final String message = String.format("Load actuals and previous return actuals %s does not match", "heel in m3");
									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
									failure.addEObjectAndFeature(loadActuals, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_M3);
									failure.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__END_HEEL_M3);
									statuses.add(failure);

								}

								if (returnActuals.getOperationsStart() == null || loadActuals.getOperationsStart() == null
										|| !returnActuals.getOperationsStart().equals(loadActuals.getOperationsStart())) {
									final String message = String.format("Load actuals and previous return actuals %s does not match", "operations start");
									final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
									failure.addEObjectAndFeature(loadActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
									failure.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START);
									statuses.add(failure);

								}
							}

						}

						prevObject = assignment;
						previousElementHasActuals = currentElementHasActuals;
						prevActuals = currentActuals;
					}
				}
			}
		}
	}

	private String getDateString(final LocalDateTime date) {
		return date.format(DateTimeFormatsProvider.INSTANCE.createDateTimeStringDisplayFormatter());
	}

	private String getID(final EObject target) {
		if (target instanceof Cargo cargo) {
			return "cargo \"" + cargo.getLoadName() + "\"";
		} else if (target instanceof Slot<?> slot) {
			return "slot \"" + slot.getName() + "\"";
		} else if (target instanceof VesselEvent vesselEvent) {
			return "event \"" + vesselEvent.getName() + "\"";
		}
		return "(unknown)";
	}

	private String getPortName(final Port port) {
		if (port == null) {
			return "<Unspecified port>";
		}
		return port.getName();
	}

	private String getVesselName(final Vessel vessel) {
		if (vessel == null) {
			return "<Unspecified vessel>";
		}
		return vessel.getName();
	}

}
