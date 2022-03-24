/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LoadSlotPurgeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof LoadSlot slot) {
			if (slot.isSchedulePurge()) {
				if (!slot.isRestrictedVesselsArePermissive()) {
					final String message = String.format("Slot|%s with scheduled purge should have a single permitted vessel", slot.getName());
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.LOAD_SLOT__SCHEDULE_PURGE);
					dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE);
					failures.add(dcsd);
				} else {
					if (SetUtils.getObjects(slot.getSlotOrDelegateVesselRestrictions()).size() != 1) {

						final String message = String.format("Slot|%s with scheduled purge should have a single permitted vessel", slot.getName());

						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.LOAD_SLOT__SCHEDULE_PURGE);
						dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.SLOT__RESTRICTED_VESSELS);
						failures.add(dcsd);
					}
				}
			}
		}
	}
}
