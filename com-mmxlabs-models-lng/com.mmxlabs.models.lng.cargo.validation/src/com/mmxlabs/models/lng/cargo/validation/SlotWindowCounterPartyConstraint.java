/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotWindowCounterPartyConstraint extends AbstractModelMultiConstraint {
	@Override
	public String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			if (cargo.getCargoType() != CargoType.FLEET) {
				for (final Slot<?> s : cargo.getSlots()) {
					if (s.isWindowCounterParty()) {
						final DetailConstraintStatusDecorator failure = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot|" + s.getName()
										+ " Cannot have a counterparty window for an unshipped cargo."));
								failure.addEObjectAndFeature(s, CargoPackage.Literals.SLOT__WINDOW_COUNTER_PARTY);
								failures.add(failure);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
