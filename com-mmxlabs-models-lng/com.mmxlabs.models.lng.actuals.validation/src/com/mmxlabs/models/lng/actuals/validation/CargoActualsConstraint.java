/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.validation;

import java.time.ZonedDateTime;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.ReturnActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.actuals.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoActualsConstraint extends AbstractModelMultiConstraint {

	private enum Type {
		Load, Discharge, Return
	}

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof CargoActuals) {
			final CargoActuals cargoActuals = (CargoActuals) object;

			if (cargoActuals.getCargoReference() == null || cargoActuals.getCargoReference().isEmpty()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Actuals needs cargo reference"));
				status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE);
				failures.add(status);
			}
			if (cargoActuals.getReturnActuals() == null) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Actuals needs return actuals"));
				status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE);
				failures.add(status);
			}

			if (cargoActuals.getCargo() == null) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo Actuals needs a cargo"));
				status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__CARGO);
				failures.add(status);
			} else {

				boolean checkReturnActuals = false;

				final Cargo cargo = cargoActuals.getCargo();
				if (cargo.isAllowRewiring()) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Actualised cargo should not allow re-wiring"));
					status.addEObjectAndFeature(cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING);
					failures.add(status);
				}

				// Check vessel assignments
				if (cargo.getCargoType() == CargoType.FLEET) {
					checkReturnActuals = true;

					if (!cargo.isLocked()) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Actualised cargo vessel assignment should be locked"));
						status.addEObjectAndFeature(cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED);
						failures.add(status);
					}

					final VesselAssignmentType vesselAssignmentType = cargo.getVesselAssignmentType();
					if (vesselAssignmentType instanceof VesselAvailability) {

						final VesselAvailability vesselAvailability = (VesselAvailability) vesselAssignmentType;
						final Vessel vessel = vesselAvailability.getVessel();

						if (vessel != null && !vessel.equals(cargoActuals.getVessel())) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Assigned and Actual vessel differ"));
							status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL);
							status.addEObjectAndFeature(cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE);
							failures.add(status);
						}
					}
				} else {

					for (final Slot slot : cargo.getSlots()) {
						if (slot instanceof LoadSlot) {
							final LoadSlot loadSlot = (LoadSlot) slot;
							if (loadSlot.isDESPurchase()) {
								if (loadSlot.getNominatedVessel() != null && !loadSlot.getNominatedVessel().equals(cargoActuals.getVessel())) {
									final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
											(IConstraintStatus) ctx.createFailureStatus("Assigned and Actual vessel differ"));
									status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL);
									status.addEObjectAndFeature(loadSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
									failures.add(status);
								}
								if (loadSlot.isDivertible()) {
									checkReturnActuals = true;
								}

							}
						} else if (slot instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot;
							if (dischargeSlot.isFOBSale()) {
								if (dischargeSlot.getNominatedVessel() != null && !dischargeSlot.getNominatedVessel().equals(cargoActuals.getVessel())) {
									final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
											(IConstraintStatus) ctx.createFailureStatus("Assigned and Actual vessel differ"));
									status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL);
									status.addEObjectAndFeature(dischargeSlot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
									failures.add(status);
								}
							}
						}
					}
				}

				boolean distanceNeeded = true;

				for (final Slot slot : cargo.getSlots()) {
					assert slot != null;
					final SlotType slotClassification = SlotClassifier.classify(slot);
					if (slotClassification == SlotType.FOB_Sale) {
						distanceNeeded = false;
					}
					if (slotClassification == SlotType.DES_Buy) {
						distanceNeeded = false;
					}

				}

				for (final SlotActuals slotActuals : cargoActuals.getActuals()) {
					final Slot slot = slotActuals.getSlot();

					if (slot != null) {
						if (!cargoActuals.getCargo().getSlots().contains(slot)) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot actuals slot is not part of Cargo"));
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__SLOT);
							status.addEObjectAndFeature(cargo, CargoPackage.Literals.CARGO__SLOTS);
							failures.add(status);
						}
					}

					if (slotActuals.getTitleTransferPoint() == null) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot actual needs a title transfer point"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__TITLE_TRANSFER_POINT);
						failures.add(status);
					} else if (slot != null && slotActuals.getTitleTransferPoint() != slot.getPort()) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Slot actuals title transfer point and slot port must match"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__TITLE_TRANSFER_POINT);
						status.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__PORT);
						failures.add(status);
					}
					if (slotActuals.getRoute() == null) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot actual needs a route"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__ROUTE);
						failures.add(status);
					}
					if (slotActuals.getOperationsStart() == null) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot actual needs an operations start date"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
						failures.add(status);
					}
					if (slotActuals.getOperationsEnd() == null) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot actual needs an operations end date"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END);
						failures.add(status);
					}

					if (slotActuals.getOperationsStart() != null && slotActuals.getOperationsEnd() != null) {
						if (slotActuals.getOperationsStart().isAfter(slotActuals.getOperationsEnd())) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Operations start date is after operations end"));
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END);
							failures.add(status);
						}
						// if (slot instanceof LoadSlot) {
						// if (((LoadSlot) slot).isDESPurchase() && !slot.isDivertible()) {
						// if (!slotActuals.getOperationsStart().equals(slotActuals.getOperationsEnd())) {
						// final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
						// (IConstraintStatus) ctx.createFailureStatus("Operations start and end date should be the same for a DES purchase"));
						// status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
						// status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END);
						// failures.add(status);
						// }
						// }
						// }
						// if (slot instanceof DischargeSlot) {
						// if (((DischargeSlot) slot).isFOBSale()) {
						// if (!slotActuals.getOperationsStart().equals(slotActuals.getOperationsEnd())) {
						// final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
						// (IConstraintStatus) ctx.createFailureStatus("Operations start and end date should be the same for a FOB sale"));
						// status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
						// status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_END);
						// failures.add(status);
						// }
						// }
						// }
					}

					// Sanity check cv and m3 -> mmbtu conversions
					if (Math.abs((slotActuals.getVolumeInMMBtu() / slotActuals.getCV()) - slotActuals.getVolumeInM3()) > 0.5) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Slot actual CV, volume in m3 and volume in mmBtu do not match up"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__CV);
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_M3);
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_MM_BTU);
						failures.add(status);
					}
					if (slotActuals instanceof LoadActuals) {
						final LoadActuals loadActuals = (LoadActuals) slotActuals;
						if (Math.abs((loadActuals.getStartingHeelMMBTu() / slotActuals.getCV()) - loadActuals.getStartingHeelM3()) > 0.5) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Slot actual CV, heel volume in m3 and heel volume in mmBtu do not match up"));
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__CV);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_M3);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_MMB_TU);
							failures.add(status);
						}
					} else if (slotActuals instanceof DischargeActuals) {
						final DischargeActuals dischargeActuals = (DischargeActuals) slotActuals;
						if (Math.abs((dischargeActuals.getEndHeelMMBTu() / slotActuals.getCV()) - dischargeActuals.getEndHeelM3()) > 0.5) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Slot actual CV, heel volume in m3 and heel volume in mmBtu do not match up"));
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__CV);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.DISCHARGE_ACTUALS__END_HEEL_M3);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.DISCHARGE_ACTUALS__END_HEEL_MMB_TU);
							failures.add(status);
						}

					}

					if (distanceNeeded && slotActuals.getDistance() == 0) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot actual needs a distance"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__DISTANCE);
						failures.add(status);
					}

					final ReturnActuals returnActuals = cargoActuals.getReturnActuals();
					if (returnActuals != null) {
						final ZonedDateTime returnOperationsStart = returnActuals.getOperationsStartAsDateTime();
						if (returnOperationsStart != null && slotActuals.getOperationsStartAsDateTime() != null) {
							if (returnOperationsStart.isBefore(slotActuals.getOperationsStartAsDateTime())) {
								final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus("Return actuals date is before slot actuals date"));
								status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START);
								status.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START);
								failures.add(status);
							}
						}
					}

				}
				final ReturnActuals returnActuals = cargoActuals.getReturnActuals();
				if (returnActuals != null) {

					if (checkReturnActuals) {

						if (returnActuals.getTitleTransferPoint() == null) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Return Actuals needs a title transfer point"));
							status.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__TITLE_TRANSFER_POINT);
							failures.add(status);
						}

						if (returnActuals.getOperationsStart() == null) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Return actual needs an operations start date"));
							status.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START);
							failures.add(status);
						}
					}
					// // Sanity check cv and m3 -> mmbtu conversions
					// if (Math.abs((returnActuals.getCV() * returnActuals.getEndHeelM3()) - returnActuals.getEndHeelMMBTu()) > 1.0) {
					// final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
					// (IConstraintStatus) ctx.createFailureStatus("Return actual CV, heel volume in m3 and volume in mmBtu do not match up"));
					// status.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__CV);
					// status.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__END_HEEL_M3);
					// status.addEObjectAndFeature(returnActuals, ActualsPackage.Literals.RETURN_ACTUALS__END_HEEL_MMB_TU);
					// failures.add(status);
					// }
				}

				// Check slot sort order
				{
					// String builder to store cargo type
					final StringBuilder sb = new StringBuilder();

					Type prevSlotType = null;
					EObject prevSlot = null;
					final List<EObject> sortedSlots = cargoActuals.getSortedActuals();
					ZonedDateTime prevOperationsStart = null;
					EStructuralFeature prevOperationsStartFeature = null;
					for (final EObject obj : sortedSlots) {
						final Type slotType;
						ZonedDateTime operationsStart = null;
						EStructuralFeature operationsStartFeature = null;
						if (obj instanceof SlotActuals && ((SlotActuals) obj).getSlot() instanceof LoadSlot) {
							slotType = Type.Load;
							sb.append("L");
							operationsStart = ((SlotActuals) obj).getOperationsStartAsDateTime();
							operationsStartFeature = ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START;
						} else if (obj instanceof SlotActuals && ((SlotActuals) obj).getSlot() instanceof DischargeSlot) {
							slotType = Type.Discharge;
							sb.append("D");
							operationsStart = ((SlotActuals) obj).getOperationsStartAsDateTime();
							operationsStartFeature = ActualsPackage.Literals.SLOT_ACTUALS__OPERATIONS_START;
						} else if (obj instanceof ReturnActuals) {
							slotType = Type.Return;
							sb.append("R");
							operationsStart = ((ReturnActuals) obj).getOperationsStartAsDateTime();
							operationsStartFeature = ActualsPackage.Literals.RETURN_ACTUALS__OPERATIONS_START;
						} else {
							sb.append("U");
							// Unknown type
							slotType = null;
						}

						// This should only permit a single load followed by zero or more discharge slots
						if (slotType == Type.Load && prevSlotType != null) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "' - The load slot should be the first slot in the cargo."));
							dsd.addEObjectAndFeature(cargoActuals, ActualsPackage.eINSTANCE.getCargoActuals_CargoReference());
							dsd.addEObjectAndFeature(obj, ActualsPackage.eINSTANCE.getSlotActuals_OperationsStart());
							failures.add(dsd);
						}

						if (slotType == Type.Discharge && prevSlotType == null) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "' - A load slot should be the first slot in the cargo."));
							dsd.addEObjectAndFeature(cargoActuals, ActualsPackage.eINSTANCE.getCargoActuals_CargoReference());
							dsd.addEObjectAndFeature(obj, ActualsPackage.eINSTANCE.getSlotActuals_OperationsStart());
							failures.add(dsd);
						}
						if (prevSlotType == Type.Return) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "' - Return actuals should be the last item in the cargo."));
							dsd.addEObjectAndFeature(cargoActuals, ActualsPackage.eINSTANCE.getCargoActuals_CargoReference());
							dsd.addEObjectAndFeature(prevSlot, ActualsPackage.eINSTANCE.getReturnActuals_OperationsStart());
							failures.add(dsd);
						}

						if (prevOperationsStart != null && operationsStart != null) {
							if (prevOperationsStart.isAfter(operationsStart)) {
								final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
										(IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName() + "' - Cargo actuals date order is incorrect"));
								dsd.addEObjectAndFeature(obj, operationsStartFeature);
								dsd.addEObjectAndFeature(prevSlot, prevOperationsStartFeature);
								failures.add(dsd);

							}
						}

						prevSlot = obj;
						prevSlotType = slotType;
						prevOperationsStart = operationsStart;
						prevOperationsStartFeature = operationsStartFeature;
					}
				}

			}
		}

		return Activator.PLUGIN_ID;
	}
}
