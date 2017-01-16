/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
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
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		if (object instanceof Cargo) {
			final Cargo cargo = (Cargo) object;

			// String builder to store cargo type
			final StringBuilder sb = new StringBuilder();

			Type prevSlotType = null;
			Slot prevSlot = null;
			EList<Slot> sortedSlots = cargo.getSortedSlots();
			for (final Slot slot : sortedSlots) {
				final Type slotType;
				if (slot instanceof LoadSlot) {
					slotType = Type.Load;
					sb.append("L");
				} else if (slot instanceof DischargeSlot) {
					slotType = Type.Discharge;
					sb.append("D");
				} else {
					sb.append("U");
					// Unknown type
					slotType = null;
				}

				// This should only permit a single load followed by zero or more discharge slots

				if (slotType == Type.Load && prevSlotType != null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName()
							+ "' - The load slot should be the first slot in the cargo."));
					dsd.addEObjectAndFeature(prevSlot, CargoPackage.eINSTANCE.getSlot_WindowStart());
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
					failures.add(dsd);
				}

				if (slotType == Type.Discharge && prevSlotType == null) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName()
							+ "' - A load slot should be the first slot in the cargo."));
					dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_WindowStart());
					failures.add(dsd);
				}

				prevSlot = slot;
				prevSlotType = slotType;
			}

			if (cargo.getCargoType().equals(CargoType.FLEET)) {
				// Examine string for valid slot combinations
				final String cargoType = sb.toString();
				if (!(cargoType.equals("LD") || cargoType.equals("LDD"))) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName()
							+ "' - Cargo should be LD or LDD."));
					dsd.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots());
					failures.add(dsd);
				}
			} else {
				// Examine string for valid slot combinations
				final String cargoType = sb.toString();
				if (!cargoType.equals("LD")) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("'" + cargo.getLoadName()
							+ "' - Cargo should be LD."));
					dsd.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots());
					failures.add(dsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
