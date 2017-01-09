/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DischargeSlotCVRangeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof DischargeSlot) {
			final DischargeSlot slot = (DischargeSlot) target;
			DetailConstraintStatusDecorator rangeCheckDSD = checkRange(slot, ctx);
			if (rangeCheckDSD != null) {
				failures.add(rangeCheckDSD);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private DetailConstraintStatusDecorator checkRange(DischargeSlot slot, final IValidationContext ctx) {
		if (slot.isSetMinCvValue() && slot.isSetMaxCvValue()) {
			final double minCvValue = slot.getMinCvValue();
			final double maxCvValue = slot.getMaxCvValue();

			if (minCvValue > maxCvValue) {
				final String failureMessage = String.format("Discharge slot '%s' has minimum CV (%.2f) greater than maximum CV (%.2f)", slot.getName(), minCvValue, maxCvValue);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getDischargeSlot_MinCvValue());
				dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getDischargeSlot_MaxCvValue());
				return dsd;
			}

		}
		return null;
	}

}
