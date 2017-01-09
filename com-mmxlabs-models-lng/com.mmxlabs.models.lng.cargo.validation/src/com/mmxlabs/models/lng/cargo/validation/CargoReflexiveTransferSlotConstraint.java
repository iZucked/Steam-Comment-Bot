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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 */
public class CargoReflexiveTransferSlotConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof DischargeSlot) {
			final DischargeSlot slot = (DischargeSlot) target;
			final Cargo cargo = slot.getCargo();
			final LoadSlot transfer = slot.getTransferTo();

			if (cargo != null && transfer != null && cargo.getSlots().contains(transfer)) {
				final String message = String.format("[Cargo|'%s'] contains slots '%s' and '%s' which represent the same transfer.", cargo.getLoadName(), slot.getName(), transfer.getName());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(cargo, CargoPackage.Literals.CARGO__SLOTS);
				failures.add(dsd);				
			}
		}
		return Activator.PLUGIN_ID;
	}
	
}
