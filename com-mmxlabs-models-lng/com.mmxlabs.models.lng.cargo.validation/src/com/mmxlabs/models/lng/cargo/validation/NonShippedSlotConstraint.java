/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NonShippedSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) target;
			if (loadSlot.isDESPurchase() && loadSlot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DEST_WITH_SOURCE) {
				String type = "DES Purchase";
				String name = loadSlot.getName();
				if (loadSlot.getSlotOrDelegateContractRestrictions().isEmpty() && loadSlot.getSlotOrDelegatePortRestrictions().isEmpty()) {
					final String message = String.format("%s %s| Port or contract restriction must be set with this DES type.", type, name);
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					failure.addEObjectAndFeature(target, CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE_DEAL_TYPE);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_PORTS);

					statuses.add(failure);
				}
			}

		} else if (target instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) target;
			if (dischargeSlot.isFOBSale() && dischargeSlot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.SOURCE_WITH_DEST) {
				String type = "FOB Sale";
				String name = dischargeSlot.getName();
				if (dischargeSlot.getSlotOrDelegateContractRestrictions().isEmpty() && dischargeSlot.getSlotOrDelegatePortRestrictions().isEmpty()) {
					final String message = String.format("%s %s| Port or contract restriction must be set with this FOB type.", type, name);
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					failure.addEObjectAndFeature(target, CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS);
					failure.addEObjectAndFeature(target, CargoPackage.Literals.SLOT__RESTRICTED_PORTS);

					statuses.add(failure);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
