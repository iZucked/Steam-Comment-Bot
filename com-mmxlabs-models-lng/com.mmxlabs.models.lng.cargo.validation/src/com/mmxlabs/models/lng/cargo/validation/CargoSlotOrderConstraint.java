/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

/**
 * Check that the end of any cargo's discharge window is not before the start of its load window.
 * 
 * @author Tom Hinton
 * 
 */
public class CargoSlotOrderConstraint extends AbstractModelMultiConstraint {

	private enum Type {
		Load, Discharge
	}

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;
			if (cargo.getCargoType().equals(CargoType.FLEET)) {

				Type prevSlotType = null;
				Slot prevSlot = null;
				for (final Slot slot : cargo.getSortedSlots()) {
					final Type slotType;
					if (slot instanceof LoadSlot) {
						slotType = Type.Load;
					} else if (slot instanceof DischargeSlot) {
						slotType = Type.Discharge;
					} else {
						// Unknown type
						slotType = null;
					}

					// This should only permit a single load followed by zero or more discharge slots

					if (slotType == Type.Load && prevSlotType != null) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getName()
								+ "' - The load slot should be the first slot in the cargo."));
						dsd.addEObjectAndFeature(prevSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						failures.add(dsd);
					}

					if (slotType == Type.Discharge && prevSlotType == null) {
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getName()
								+ "' - A load slot should be the first slot in the cargo."));
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
						failures.add(dsd);
					}

					prevSlot = slot;
					prevSlotType = slotType;
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
