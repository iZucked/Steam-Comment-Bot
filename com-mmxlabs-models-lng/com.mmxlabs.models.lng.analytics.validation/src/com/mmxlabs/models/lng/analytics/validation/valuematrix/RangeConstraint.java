/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.validation.valuematrix;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.Range;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class RangeConstraint extends AbstractModelMultiConstraint {

	@Override
	public void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();
		if (target instanceof @NonNull final Range range) {
			if (range.getStepSize() <= 0) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Step size should be greater than zero"));
				dsd.addEObjectAndFeature(range, AnalyticsPackage.eINSTANCE.getRange_StepSize());
				failures.add(dsd);
			}
			if (range.getMin() > range.getMax()) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min price must not be greater than max price"));
				dsd.addEObjectAndFeature(range, AnalyticsPackage.eINSTANCE.getRange_Min());
				dsd.addEObjectAndFeature(range, AnalyticsPackage.eINSTANCE.getRange_Max());
				failures.add(dsd);
			}
			if (range.getMax() > 90) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Max price must not be greater than 90"));
				dsd.addEObjectAndFeature(range, AnalyticsPackage.eINSTANCE.getRange_Max());
				failures.add(dsd);
			}
			if (range.getMin() < 1) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min price must be greater than zero"));
				dsd.addEObjectAndFeature(range, AnalyticsPackage.eINSTANCE.getRange_Min());
				failures.add(dsd);
			}
		}
	}
}
