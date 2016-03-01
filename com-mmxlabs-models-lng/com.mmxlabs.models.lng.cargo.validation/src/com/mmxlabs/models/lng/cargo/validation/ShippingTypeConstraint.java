/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ShippingTypeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {

		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			final CargoDeliveryType cargoType = cargo.getCargoType() == CargoType.FLEET  ? CargoDeliveryType.SHIPPED : CargoDeliveryType.NOT_SHIPPED;

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
						requiredCargoType = dischargeSlot.getSlotOrContractDeliveryType();
						if (contract instanceof SalesContract) {
							featureChecked = CargoPackage.Literals.SLOT__CONTRACT;
							featureCheckedName = String.format("Sales contract '%s'", contract.getName());
						}
					}
					if (requiredCargoType != CargoDeliveryType.ANY && cargoType != requiredCargoType) {
						final String format = "%s specifies '%s' purchase but cargo shipping type is '%s'.";
						final String error = String.format(format, featureCheckedName, requiredCargoType.getName(), cargoType.getName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(error, IStatus.ERROR));
						dsd.addEObjectAndFeature(dischargeSlot, featureChecked);
						failures.add(dsd);
					}
				}
				
				if (slot instanceof LoadSlot) {

					final LoadSlot loadSlot = (LoadSlot) slot;

					// get the required cargo type from the delivery slot or the contract
					if (loadSlot.isSetSalesDeliveryType()) {
						requiredCargoType = loadSlot.getSalesDeliveryType();
						featureChecked = CargoPackage.Literals.LOAD_SLOT__SALES_DELIVERY_TYPE;
						featureCheckedName = "slot";
					} else {
						final Contract contract = loadSlot.getContract();
						requiredCargoType = loadSlot.getSlotOrContractDeliveryType();
						if (contract instanceof SalesContract) {
							featureChecked = CargoPackage.Literals.SLOT__CONTRACT;
							featureCheckedName = String.format("Purchase contract '%s'", contract.getName());
						}
					}
					if (requiredCargoType != CargoDeliveryType.ANY && cargoType != requiredCargoType) {
						final String format = "%s specifies '%s' sale but cargo shipping type is '%s'.";
						final String error = String.format(format, featureCheckedName, requiredCargoType.getName(), cargoType.getName());
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(error, IStatus.ERROR));
						dsd.addEObjectAndFeature(loadSlot, featureChecked);
						failures.add(dsd);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
