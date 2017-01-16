/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractTypeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		// Valid slot data checks
		if (object instanceof Slot) {

			final Slot slot = (Slot) object;
			final Contract contract = slot.getContract();
			if (contract == null) {
				return Activator.PLUGIN_ID;
			}
			String message = null;
			if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				if (loadSlot.isDESPurchase()) {
					if (contract.getContractType() == ContractType.FOB) {
						message = String.format("DES Purchase|%s contract is FOB Purchase only!", slot.getName());
					}
				} else {
					if (contract.getContractType() == ContractType.DES) {
						message = String.format("FOB Purchase|%s contract is DES Purchase only!", slot.getName());
					}
				}
			} else if (object instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) object;
				if (dischargeSlot.isFOBSale()) {
					if (contract.getContractType() == ContractType.DES) {
						message = String.format("FOB Sale|%s contract is DES Sale only!", slot.getName());
					}
				} else {
					if (contract.getContractType() == ContractType.FOB) {
						message = String.format("DES Sale|%s contract is FOB Sale!", slot.getName());
					}
				}
			}
			if (message != null) {
				final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
				dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_ShippingDaysRestriction());
				failures.add(dsd);
			}
		}
		return Activator.PLUGIN_ID;
	}

}
