/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NominatedVesselConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (!(extraContext.getContainer(object) instanceof CargoModel)) {
			return;
		}

		if (object instanceof LoadSlot slot) {

			// DES Purchase
			if (slot.isDESPurchase() && slot.getSlotOrDelegateDESPurchaseDealType() == DESPurchaseDealType.DIVERT_FROM_SOURCE) {
				if (slot.getNominatedVessel() == null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("Divertible DES Purchase|%s needs a nominated vessel", slot.getName())));
					failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
					failures.add(failure);
				} else if (slot.getNominatedVessel().isMarker()) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("Divertible DES Purchase|%s needs a nominated non-marker vessel", slot.getName())));
					failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
					failures.add(failure);
				}
			}
		}

		else if (object instanceof DischargeSlot slot) {

			// FOB Sale
			if (slot.isFOBSale() && slot.getSlotOrDelegateFOBSaleDealType() == FOBSaleDealType.DIVERT_TO_DEST) {
				if (slot.getNominatedVessel() == null) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("Divertible FOB Sale|%s needs a nominated vessel", slot.getName())));
					failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
					failures.add(failure);
				} else if (slot.getNominatedVessel().isMarker()) {
					final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("Divertible FOB Sale|%s needs a nominated non-marker vessel", slot.getName())));
					failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
					failures.add(failure);
				}
			}
		}
	}

}
