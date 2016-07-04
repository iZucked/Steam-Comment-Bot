/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoTypeConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			if (cargo.getCargoType() != CargoType.FLEET) {

				for (final Slot s : cargo.getSlots()) {
					if (s instanceof LoadSlot) {
						final LoadSlot loadSlot = (LoadSlot) s;
						if (loadSlot.isDESPurchase()) {
							if (cargo.getCargoType() == CargoType.FOB) {
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo|" + cargo.getLoadName()
										+ " Cannot pair a DES Purchase to a FOB Sale."));
								failure.addEObjectAndFeature(loadSlot, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE);
								failures.add(failure);
							}
						}
					} else if (s instanceof DischargeSlot) {
						final DischargeSlot dischargeSlot = (DischargeSlot) s;
						if (dischargeSlot.isFOBSale()) {
							if (cargo.getCargoType() == CargoType.DES) {
								final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo|" + cargo.getLoadName()
										+ " Cannot pair a DES Purchase to a FOB Sale."));
								failure.addEObjectAndFeature(dischargeSlot, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE);
								failures.add(failure);
							}
						}
					}

				}

			}

			//
			// }
		}

		return Activator.PLUGIN_ID;
	}
}
