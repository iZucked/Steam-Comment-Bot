/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier;
import com.mmxlabs.models.lng.cargo.util.SlotClassifier.SlotType;
import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class NominatedVesselConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof AssignableElement) {
			final AssignableElement assignableElement = (AssignableElement) object;

			final Slot slot = getValidObject(assignableElement);
			if (slot == null) {
				return Activator.PLUGIN_ID;
			}

			final AVesselSet<? extends Vessel> vessel = assignableElement.getAssignment();

			if (slot instanceof LoadSlot) {
				// DES Purchase
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (SlotClassifier.classify(loadSlot) == SlotType.DES_Buy_AnyDisPort) {

					if (vessel == null) {
						final String type = loadSlot.isDESPurchase() ? "DES" : "FOB";
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
								"Divertable %s Purchase|%s needs a nominated vessel", type, loadSlot.getName())));
						failure.addEObjectAndFeature(assignableElement, FleetPackage.Literals.ASSIGNABLE_ELEMENT__ASSIGNMENT);
						failures.add(failure);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private @Nullable
	Slot getValidObject(@Nullable final EObject eObj) {

		if (eObj instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) eObj;
			if (loadSlot.isDESPurchase()) {
				return loadSlot;
			}
		}
		if (eObj instanceof DischargeSlot) {
			final DischargeSlot dischargeSlot = (DischargeSlot) eObj;
			if (dischargeSlot.isFOBSale()) {
				return dischargeSlot;
			}
		}
		return null;
	}
}
