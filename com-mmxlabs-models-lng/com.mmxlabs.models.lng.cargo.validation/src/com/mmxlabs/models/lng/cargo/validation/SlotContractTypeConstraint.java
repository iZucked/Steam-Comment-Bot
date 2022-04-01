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

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.ContractType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotContractTypeConstraint extends AbstractModelMultiConstraint {
	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof LoadSlot slot) {
			final Contract contract = slot.getContract();
			if (contract != null) {
				final ContractType contractType = contract.getContractType();
				if (slot.isDESPurchase()) {
					if (!(contractType == ContractType.DES || contractType == ContractType.BOTH)) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("DES Purchase|" + slot.getName() + " does not have a DES Purchase contract."));
						failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
						failures.add(failure);
					}
				} else {
					if (!(contractType == ContractType.FOB || contractType == ContractType.BOTH)) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("FOB Purchase|" + slot.getName() + " does not have a FOB Purchase contract."));
						failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
						failures.add(failure);
					}
				}
			}
		} else if (target instanceof DischargeSlot slot) {
			final Contract contract = slot.getContract();
			if (contract != null) {
				final ContractType contractType = contract.getContractType();
				if (slot.isFOBSale()) {
					if (!(contractType == ContractType.FOB || contractType == ContractType.BOTH)) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("FOB Sale|" + slot.getName() + " does not have a FOB Sale contract."));
						failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
						failures.add(failure);
					}
				} else {
					if (!(contractType == ContractType.DES || contractType == ContractType.BOTH)) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus("DES Sale|" + slot.getName() + " does not have a DES Sale contract."));
						failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__CONTRACT);
						failures.add(failure);
					}
				}
			}
		}
	}
}
