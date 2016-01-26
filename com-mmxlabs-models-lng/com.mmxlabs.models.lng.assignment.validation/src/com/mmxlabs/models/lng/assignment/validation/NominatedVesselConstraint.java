/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.assignment.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.assignment.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class NominatedVesselConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		final LoadSlot slot = getValidObject(object);
		if (slot == null) {
			return Activator.PLUGIN_ID;
		}

		// DES Purchase
		if (SlotClassifier.classify(slot) == SlotType.DES_Buy_AnyDisPort) {

			if (slot.getNominatedVessel() == null) {
				final String type = slot.isDESPurchase() ? "DES" : "FOB";
				final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Divertable %s Purchase|%s needs a nominated vessel", type, slot.getName())));
				failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__NOMINATED_VESSEL);
				failures.add(failure);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private @Nullable
	LoadSlot getValidObject(@Nullable final EObject eObj) {

		if (eObj instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) eObj;
			if (loadSlot.isDESPurchase()) {
				return loadSlot;
			}
		}
		return null;
	}
}
