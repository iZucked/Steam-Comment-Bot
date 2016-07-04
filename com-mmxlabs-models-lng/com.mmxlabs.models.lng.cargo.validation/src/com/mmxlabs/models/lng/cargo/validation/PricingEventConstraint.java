/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PricingEventConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			Slot slot = (Slot) object;
			if (slot.isSetPricingEvent() && slot.getPricingEvent() == null) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot|" + slot.getName() + " pricing event not set."));
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PricingEvent());
				failures.add(status);
			}
		}
		if (object instanceof Cargo) {

			final Cargo cargo = (Cargo) object;

			final int numberOfSlots = cargo.getSlots().size();
			if (numberOfSlots > 2) {
				for (final Slot slot : cargo.getSlots()) {
					final PricingEvent pricingEvent = slot.getSlotOrDelegatedPricingEvent();
					if (slot instanceof LoadSlot) {
						if (pricingEvent == PricingEvent.START_DISCHARGE || pricingEvent == PricingEvent.END_DISCHARGE) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot|" + slot.getName()
									+ " cannot be priced on discharge event in a complex cargo."));
							status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PricingEvent());
							failures.add(status);
						}
					} else if (slot instanceof DischargeSlot) {
						if (pricingEvent == PricingEvent.START_LOAD || pricingEvent == PricingEvent.END_LOAD) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Slot|" + slot.getName()
									+ " cannot be priced on load event in a complex cargo."));
							status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PricingEvent());
							failures.add(status);
						}

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

}
