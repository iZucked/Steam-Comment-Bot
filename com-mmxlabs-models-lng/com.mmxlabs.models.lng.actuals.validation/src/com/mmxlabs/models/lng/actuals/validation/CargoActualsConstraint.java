/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.actuals.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.CargoActuals;
import com.mmxlabs.models.lng.actuals.DischargeActuals;
import com.mmxlabs.models.lng.actuals.LoadActuals;
import com.mmxlabs.models.lng.actuals.SlotActuals;
import com.mmxlabs.models.lng.actuals.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CargoActualsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof CargoActuals) {
			final CargoActuals cargoActuals = (CargoActuals) object;

			if (cargoActuals.getCargoReference() == null || cargoActuals.getCargoReference().isEmpty()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Actuals needs cargo reference"));
				status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__CARGO_REFERENCE);
				failures.add(status);
			}

			if (cargoActuals.getCargo() == null) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo Actuals needs a cargo"));
				status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__CARGO);
				failures.add(status);
			} else {
				final Cargo cargo = cargoActuals.getCargo();
				if (cargo.isAllowRewiring()) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Actualised cargo should not allow re-wiring"));
					status.addEObjectAndFeature(cargo, CargoPackage.Literals.CARGO__ALLOW_REWIRING);
					failures.add(status);
				}

				// Check vessel assignments
				if (cargo.getCargoType() == CargoType.FLEET) {

					if (!cargo.isLocked()) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Actualised cargo vessel assignment should be locked"));
						status.addEObjectAndFeature(cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__LOCKED);
						failures.add(status);
					}

					if (!cargo.getAssignment().equals(cargoActuals.getVessel())) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Assigned and Actual vessel differ"));
						status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL);
						status.addEObjectAndFeature(cargo, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);
						failures.add(status);
					}
				} else {
					for (final Slot slot : cargo.getSlots()) {
						if (slot instanceof LoadSlot) {
							final LoadSlot loadSlot = (LoadSlot) slot;
							if (loadSlot.isDESPurchase()) {
							if (loadSlot.getAssignment() != null  && !loadSlot.getAssignment().equals(cargoActuals.getVessel())) {
									final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Assigned and Actual vessel differ"));
									status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL);
									status.addEObjectAndFeature(loadSlot, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);
									failures.add(status);
								}

							}
						} else if (slot instanceof DischargeSlot) {
							final DischargeSlot dischargeSlot = (DischargeSlot) slot;
							if (dischargeSlot.isFOBSale()) {
								if (!dischargeSlot.getAssignment().equals(cargoActuals.getVessel())) {
									final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Assigned and Actual vessel differ"));
									status.addEObjectAndFeature(cargoActuals, ActualsPackage.Literals.CARGO_ACTUALS__VESSEL);
									status.addEObjectAndFeature(dischargeSlot, CargoPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);
									failures.add(status);
								}
							}
						}
					}
				}

				for (final SlotActuals slotActuals : cargoActuals.getActuals()) {

					// Sanity check cv and m3 -> mmbtu conversions
					if (Math.abs((slotActuals.getCV() * slotActuals.getVolumeInM3()) - slotActuals.getVolumeInMMBtu()) > 1.0) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("Slot actual CV, volume in m3 and volume in mmBtu do not match up"));
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__CV);
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_M3);
						status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__VOLUME_IN_MM_BTU);
						failures.add(status);
					}
					if (slotActuals instanceof LoadActuals) {
						final LoadActuals loadActuals = (LoadActuals) slotActuals;
						if (Math.abs((slotActuals.getCV() * loadActuals.getStartingHeelM3()) - loadActuals.getStartingHeelMMBTu()) > 1.0) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Slot actual CV, heel volume in m3 and heel volume in mmBtu do not match up"));
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__CV);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_M3);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.LOAD_ACTUALS__STARTING_HEEL_MMB_TU);
							failures.add(status);
						}
					} else if (slotActuals instanceof DischargeActuals) {
						final DischargeActuals dischargeActuals = (DischargeActuals) slotActuals;
						if (Math.abs((slotActuals.getCV() * dischargeActuals.getEndHeelM3()) - dischargeActuals.getEndHeelMMBTu()) > 1.0) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
									(IConstraintStatus) ctx.createFailureStatus("Slot actual CV, heel volume in m3 and heel volume in mmBtu do not match up"));
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.SLOT_ACTUALS__CV);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.DISCHARGE_ACTUALS__END_HEEL_M3);
							status.addEObjectAndFeature(slotActuals, ActualsPackage.Literals.DISCHARGE_ACTUALS__END_HEEL_MMB_TU);
							failures.add(status);
						}

					}

				}

			}
		}

		return Activator.PLUGIN_ID;
	}
}
