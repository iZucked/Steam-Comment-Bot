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

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotAllowedPortsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Slot<?> slot) {

			var allowedPorts = slot.getSlotOrDelegateAllowedPorts();
			Port port = slot.getPort();
			if (!allowedPorts.isEmpty() && port != null) {

				if (!SetUtils.getObjects(allowedPorts).contains(port)) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus(String.format("Current port %s is not in the list of allowed ports.", port.getName())));
					status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
					statuses.add(status);
				}
			}
		}
	}
}
