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

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class LoadSlotCVConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof LoadSlot) {
			final LoadSlot slot = (LoadSlot) target;
			DetailConstraintStatusDecorator rangeCheckDSD = checkCVRange(slot, ctx);
			if (rangeCheckDSD != null) {
				failures.add(rangeCheckDSD);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private DetailConstraintStatusDecorator checkCVRange(LoadSlot slot, final IValidationContext ctx) {
		double cv = slot.getSlotOrDelegatedCV();
		if (cv < 1.0) {
			final String message = String.format("Slot|%s CV is %.2f (should be greater than 1.0)", slot.getName(), cv);
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.LOAD_SLOT__CARGO_CV);
			return dcsd;
		} else if (cv > 40.0) {
			final String message = String.format("Slot|%s CV is %.2f (should be less than 40.0)", slot.getName(), cv);
			final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
			dcsd.addEObjectAndFeature(slot, CargoPackage.Literals.LOAD_SLOT__CARGO_CV);
			return dcsd;
		}
		return null;
	}

}
