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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class CargoRestrictedSlotConstraint extends AbstractModelMultiConstraint {
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Slot) {
			Slot slot = (Slot) object;
			if (slot.getCargo() != null) {
				final Cargo cargo = slot.getCargo();
				final List<Slot> srl = slot.getRestrictedSlots();
				final boolean permissive = slot.isRestrictedSlotsArePermissive();
				if (!srl.isEmpty()) {
					for (final Slot s : cargo.getSlots()) {
						if (s.equals(slot)) continue;
						boolean fail = false;
						if (permissive) {
							if (!srl.contains(s)) {
								fail = true;
							}
						} else {
							if (srl.contains(s)) {
								fail = true;
							}
						}
						if (fail) {
							final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cargo|" + cargo.getLoadName()
							+ String.format(" Slot %s is not allowed to be paired to slot %s.",slot.getName(),s.getName())));
							failure.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__RESTRICTED_SLOTS);
							failures.add(failure);
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
