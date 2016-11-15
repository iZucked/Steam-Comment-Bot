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
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint which checks that the load and discharge quantities for a cargo are compatible, so min discharge volume < max load volume
 * 
 * @author Tom Hinton
 * 
 */
public class CargoMMBTuVolumeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof Cargo) {

			final Cargo cargo = (Cargo) object;

			int loadMinVolume = 0;
			int loadMaxVolume = 0;
			int dischargeMinVolume = 0;
			int dischargeMaxVolume = 0;
			int numberOfSlots = cargo.getSlots().size();
			for (final Slot slot : cargo.getSlots()) {
				if (slot instanceof LoadSlot) {
					loadMinVolume += slot.getSlotOrContractMinQuantity();
					loadMaxVolume += slot.getSlotOrContractMaxQuantity();
				} else if (slot instanceof DischargeSlot) {
					dischargeMinVolume += slot.getSlotOrContractMinQuantity();
					dischargeMaxVolume += slot.getSlotOrContractMaxQuantity();

					if (numberOfSlots > 2) {
						// Check fields are set
						if (!slot.isSetMaxQuantity() || !slot.isSetMinQuantity()) {
							final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Complex Cargo|" + cargo.getLoadName()
									+ " requires min and max discharge volumes to be specified and identical"));

							if (!slot.isSetMaxQuantity()) {
								status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
							} else if (!slot.isSetMinQuantity()) {
								status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
							}
							failures.add(status);
						}

						// Check fields are the same
						if (slot.isSetMaxQuantity() && slot.isSetMinQuantity()) {
							if (slot.getMinQuantity() != slot.getMaxQuantity()) {
								final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Complex Cargo|" + cargo.getLoadName()
										+ " requires min and max discharge volumes to be specified and identical"));

								status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
								status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
								failures.add(status);
							}
						}
					}
				}
			}
			if (loadMaxVolume < dischargeMinVolume) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName()
						+ "] Max load volume less than min discharge)."), IStatus.WARNING);

				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
					} else if (slot instanceof DischargeSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

					}
				}

				failures.add(status);
			}
			if (loadMinVolume > dischargeMaxVolume) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("[Cargo|" + cargo.getLoadName()
						+ "] Min load volume greater than max discharge."), IStatus.WARNING);
				for (final Slot slot : cargo.getSlots()) {
					if (slot instanceof LoadSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
					} else if (slot instanceof DischargeSlot) {
						status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

					}
				}

				failures.add(status);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
