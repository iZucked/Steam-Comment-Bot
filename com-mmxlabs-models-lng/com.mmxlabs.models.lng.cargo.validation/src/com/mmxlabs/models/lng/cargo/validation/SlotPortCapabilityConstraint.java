/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A model constraint for checking that a slot's minimum and maximum volumes are sensible (0 <= min <= max)
 * 
 * @author Tom Hinton
 * 
 */
public class SlotPortCapabilityConstraint extends AbstractModelConstraint {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			final EMFEventType eventType = ctx.getEventType();

			// This is being triggered by a batch mode validation.
			if (eventType == EMFEventType.NULL) {

				final Slot slot = (Slot) object;

				final Port port = slot.getPort();
				if (port != null) {
					final EList<PortCapability> capabilities = port.getCapabilities();

					if (slot instanceof LoadSlot) {
						if (!((LoadSlot) slot).isDESPurchase() && !capabilities.contains(PortCapability.LOAD)) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(PortCapability.LOAD.getName()));
							dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
							dsd.addEObjectAndFeature(((LoadSlot) slot).getCargo(), CargoPackage.eINSTANCE.getCargo_LoadSlot());
							return dsd;
						}
					}

					if (slot instanceof DischargeSlot) {
						if (!((DischargeSlot) slot).isFOBSale() && !capabilities.contains(PortCapability.DISCHARGE)) {
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(PortCapability.DISCHARGE.getName()));
							dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
							dsd.addEObjectAndFeature(((DischargeSlot) slot).getCargo(), CargoPackage.eINSTANCE.getCargo_DischargeSlot());
							return dsd;
						}
					}
				}
			}
		}
		return ctx.createSuccessStatus();
	}

}
