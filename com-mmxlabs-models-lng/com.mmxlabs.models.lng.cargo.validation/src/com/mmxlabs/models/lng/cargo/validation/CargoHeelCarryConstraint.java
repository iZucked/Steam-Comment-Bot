/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoHeelCarryConstraint extends AbstractModelMultiConstraint {
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_HEEL_RETENTION)) {
			if (object instanceof Cargo cargo) {
				LoadSlot load = null;
				DischargeSlot discharge = null;
				for (final Slot<?> s : cargo.getSlots()) {
					if (s instanceof LoadSlot loadSlot) {
						if (!(loadSlot instanceof SpotSlot) && (loadSlot.isSetArriveCold() && !loadSlot.isArriveCold())) {
							load = loadSlot;
						}
					} else if (s instanceof DischargeSlot dischargeSlot) {
						if (!(dischargeSlot instanceof SpotSlot) && dischargeSlot.isHeelCarry()) {
							discharge = dischargeSlot;
						}
					}
				}
				if (load != null && discharge != null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Cargo|" + cargo.getLoadName() + " Incompatible heel requirements. "));
					failure.addEObjectAndFeature(load, CargoPackage.Literals.LOAD_SLOT__ARRIVE_COLD);
					failure.addEObjectAndFeature(discharge, CargoPackage.Literals.DISCHARGE_SLOT__HEEL_CARRY);
					failures.add(failure);
				}
			}
		}
	}
}
