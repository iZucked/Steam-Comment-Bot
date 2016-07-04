/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A model constraint for checking that a slot's minimum and maximum volumes are sensible (0 <= min <= max)
 * 
 * @author Tom Hinton
 * 
 */
public class SlotVolumeConstraint extends AbstractModelMultiConstraint {
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

				final Slot slot = (Slot) object;
				String name = slot.getName();
				testVolumeValueConstraint(ctx, failures, slot, name);
				checkThatEverythingIsOverriddenWhenUnitsSet(ctx, failures, slot);
			}
		}
		return Activator.PLUGIN_ID;
	}

	private void testVolumeValueConstraint(IValidationContext ctx, List<IStatus> failures, final Slot slot, String name) {
		// TODO return some placeholders for the error message
		if (slot.getSlotOrContractMinQuantity() < 0) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(name, "Negative min volume"));

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			failures.add(dsd);
		}
		if (slot.getSlotOrContractMaxQuantity() < 0) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(name, "Negative max volume"));

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
			failures.add(dsd);
		}
		if (slot.getSlotOrContractMinQuantity() > slot.getSlotOrContractMaxQuantity()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(name, "Min volume greater than max volume."));

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

			failures.add(dsd);
		}
	}

	private void checkThatEverythingIsOverriddenWhenUnitsSet(IValidationContext ctx, List<IStatus> failures, Slot slot) {
		if (slot.isSetVolumeLimitsUnit()) {
			boolean volLimitsOverriden = checkVolumeLimitsOverriden(slot);
			if (!volLimitsOverriden) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(slot.getName(),
						String.format("volume limit units have been set [%s], min and max volume limits must also be set.", getString(slot.getVolumeLimitsUnit()))), IStatus.ERROR);
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit());
				failures.add(status);
			}
		}
	}

	private Object getString(VolumeUnits volumeLimitsUnit) {
		switch (volumeLimitsUnit) {
		case M3:
			return "m³";
		case MMBTU:
			return "mmBtu";
		}
		return volumeLimitsUnit.getName();
	}

	private boolean checkVolumeLimitsOverriden(Slot slot) {
		if (slot.isSetMinQuantity() && slot.isSetMaxQuantity()) {
			return true;
		}
		return false;
	}

}
