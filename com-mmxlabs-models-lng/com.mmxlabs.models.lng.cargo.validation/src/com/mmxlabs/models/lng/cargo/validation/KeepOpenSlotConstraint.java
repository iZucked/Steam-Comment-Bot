/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class KeepOpenSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject object = ctx.getTarget();

		if (!extraContext.isRelaxedChecking()) {
			if (object instanceof Slot<?> slot) {
				if (slot.isLocked()) {
					Cargo cargo = slot.getCargo();
					if (cargo != null) {
						final String message;
						message = String.format("[Slot|'%s'] Keep Open can only be set on an open slot. Remove wiring.", slot.getName() == null ? "(no ID)" : slot.getName());

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Locked());
						statuses.add(dsd);
					}
				}
				if (slot.isCancelled()) {
					Cargo cargo = slot.getCargo();
					if (cargo != null) {
						final String message;
						message = String.format("[Slot|'%s'] Cancelled can only be set on an open slot. Remove wiring.", slot.getName() == null ? "(no ID)" : slot.getName());

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Cancelled());
						statuses.add(dsd);
					}
				}
			}
		}
	}
}
