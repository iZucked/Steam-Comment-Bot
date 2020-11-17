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
import org.eclipse.jdt.annotation.NonNull;

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

	public static int SENSIBLE_M3 = 300_000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse .emf.validation.IValidationContext)
	 */
	@Override
	public String validate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			final Slot<?> slot = (Slot<?>) object;

			final EMFEventType eventType = ctx.getEventType();

			// This is being triggered by a batch mode validation.
			if (eventType == EMFEventType.NULL) {

				String name = slot.getName();
				testVolumeValueConstraint(ctx, failures, slot, name);
				checkThatEverythingIsOverriddenWhenUnitsSet(ctx, failures, slot);
				checkSensibleValues(ctx, failures, slot, name);
				if (slot.isSetOperationalTolerance()) {
					if (slot.getOperationalTolerance() < 0.0) {
						final String failureMessage = "Operational tolerance is less than zero";

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("%s %s", slot.getName(), failureMessage)));
						dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__OPERATIONAL_TOLERANCE);
						failures.add(dsd);
					}
					if (slot.getOperationalTolerance() > 1.0) {
						final String failureMessage = "Operational tolerance is greater than 100%";

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
								(IConstraintStatus) ctx.createFailureStatus(String.format("%s %s", slot.getName(), failureMessage)));
						dsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__OPERATIONAL_TOLERANCE);
						failures.add(dsd);
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}

	private void checkSensibleValues(@NonNull IValidationContext ctx, @NonNull List<IStatus> failures, Slot slot, String name) {
		if (slot.getSlotOrDelegateMinQuantity() > SENSIBLE_M3 && slot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.M3) {
			final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
					.createFailureStatus(String.format("Slot|%s min volume limit (%s) is not sensible, note units are in M3", slot.getName(), slot.getSlotOrDelegateMinQuantity()), IStatus.ERROR));
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit());
			failures.add(status);
		}
		if (slot.getSlotOrDelegateMaxQuantity() != 0 && slot.getSlotOrDelegateMaxQuantity() != Integer.MAX_VALUE) {
			if (slot.getSlotOrDelegateMaxQuantity() > SENSIBLE_M3 && slot.getSlotOrDelegateVolumeLimitsUnit() == VolumeUnits.M3) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx
						.createFailureStatus(String.format("Slot|%s max volume limit (%s) is not sensible, note units are in M3", slot.getName(), slot.getSlotOrDelegateMaxQuantity()), IStatus.ERROR));
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
				status.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_VolumeLimitsUnit());
				failures.add(status);
			}
		}
	}

	private void testVolumeValueConstraint(IValidationContext ctx, List<IStatus> failures, final Slot slot, String name) {
		// TODO return some placeholders for the error message
		String slotName = "Slot '" + name + "': ";
		if (slot.getSlotOrDelegateMinQuantity() < 0) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(slotName + "Negative min volume."));

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());
			failures.add(dsd);
		}
		if (slot.getSlotOrDelegateMaxQuantity() < 0) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(slotName + "Negative max volume."));

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());
			failures.add(dsd);
		}
		if (slot.getSlotOrDelegateMinQuantity() > slot.getSlotOrDelegateMaxQuantity()) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(slotName + "Min volume greater than max volume."));

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MinQuantity());

			dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_MaxQuantity());

			failures.add(dsd);
		}
	}

	private void checkThatEverythingIsOverriddenWhenUnitsSet(IValidationContext ctx, List<IStatus> failures, Slot slot) {
		// Only check if the parent and child have different units
		if (slot.isSetVolumeLimitsUnit() && (slot.getVolumeLimitsUnit() != slot.getUnsetValue(CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT))) {
			boolean volLimitsOverriden = checkVolumeLimitsOverriden(slot);
			if (!volLimitsOverriden) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(
								String.format("%s volume limit units have been set [%s], min and max volume limits must also be set.", slot.getName(), getString(slot.getVolumeLimitsUnit()))),
						IStatus.ERROR);
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

	private boolean checkVolumeLimitsOverriden(Slot<?> slot) {
		if (slot.isSetMinQuantity() && slot.isSetMaxQuantity()) {
			return true;
		}
		return false;
	}

}
