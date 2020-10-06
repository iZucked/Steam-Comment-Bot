/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A model constraint for checking that a slot's minimum and maximum volumes are sensible (0 <= min <= max)
 * 
 * @author FM
 * 
 */
public class LoadSlotVolumeConstraint extends AbstractModelMultiConstraint {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public String validate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			final EMFEventType eventType = ctx.getEventType();

			// This is being triggered by a batch mode validation.
			if (eventType == EMFEventType.NULL) {

				final Slot<?> slot = (Slot<?>) object;
				if (slot instanceof SpotSlot) {
					return Activator.PLUGIN_ID;
				}
				if (slot instanceof LoadSlot) {
					final LoadSlot loadSlot = (LoadSlot) slot;
					if (loadSlot.isDESPurchase() && loadSlot.isVolumeCounterParty()) {
						final String name = slot.getName();

						final int maxVolume = loadSlot.getSlotOrDelegateMaxQuantity();
						if (maxVolume == 0 || maxVolume == Integer.MAX_VALUE) {
							final String failureMessage = String.format("Max volume should NOT be zero for C/P volume.");

							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
									String.format("%s %s", name, failureMessage)));
							dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__MAX_QUANTITY);
							failures.add(dsd);
						}
						final int minVolume = loadSlot.getSlotOrDelegateMinQuantity();
						if (minVolume == 0 || minVolume == Integer.MIN_VALUE) {
							final String failureMessage = String.format("Min volume should NOT be zero for C/P volume.");

							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(
									String.format("%s %s", name, failureMessage)));
							dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__MIN_QUANTITY);
							failures.add(dsd);
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}

}
