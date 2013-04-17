/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ShippingTypeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> failures) {

		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			final CargoDeliveryType cargoType = cargo.getCargoType() == CargoType.DES ? CargoDeliveryType.NOT_SHIPPED : CargoDeliveryType.SHIPPED;

			for (final Slot slot : cargo.getSlots()) {

				EStructuralFeature featureChecked = null;
				String featureCheckedName = null;

				CargoDeliveryType requiredCargoType = CargoDeliveryType.ANY;

				if (slot instanceof DischargeSlot) {

					final DischargeSlot dischargeSlot = (DischargeSlot) slot;

					// get the required cargo type from the delivery slot or the contract
					if (dischargeSlot.isSetPurchaseDeliveryType()) {
						requiredCargoType = dischargeSlot.getPurchaseDeliveryType();
						featureChecked = CargoPackage.Literals.DISCHARGE_SLOT__PURCHASE_DELIVERY_TYPE;
						featureCheckedName = "slot";
					} else {
						final Contract contract = dischargeSlot.getContract();
						if (contract instanceof SalesContract) {
							requiredCargoType = ((SalesContract) contract).getPurchaseDeliveryType();
							featureChecked = CargoPackage.Literals.SLOT__CONTRACT;
							featureCheckedName = String.format("sales contract '%s'", contract.getName());
						}
					}
					if (requiredCargoType != CargoDeliveryType.ANY && cargoType != requiredCargoType) {
						final String format = "The %s specifies a purchase shipping type of '%s' which does not match the cargo shipping type of '%s'.";
						final String error = String.format(format, featureCheckedName, requiredCargoType.getName(), cargoType.getName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(error, IStatus.ERROR));
						dsd.addEObjectAndFeature(dischargeSlot, featureChecked);
						failures.add(dsd);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
