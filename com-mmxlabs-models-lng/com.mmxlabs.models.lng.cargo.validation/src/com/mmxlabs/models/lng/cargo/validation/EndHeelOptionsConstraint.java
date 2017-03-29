/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A constraint which checks {@link StartHeelOptions}
 * 
 * @author Simon Goodall
 */
public class EndHeelOptionsConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(IValidationContext ctx) {

		final List<IStatus> failures = new LinkedList<IStatus>();
		final EObject object = ctx.getTarget();
		if (object instanceof StartHeelOptions) {
			StartHeelOptions heelOptions = (StartHeelOptions) object;

			if (heelOptions.getMinVolumeAvailable() > heelOptions.getMaxVolumeAvailable()) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Min volume is less that max volume"));
				dsd.addEObjectAndFeature(heelOptions, CargoPackage.eINSTANCE.getStartHeelOptions_MinVolumeAvailable());
				dsd.addEObjectAndFeature(heelOptions, CargoPackage.eINSTANCE.getStartHeelOptions_MaxVolumeAvailable());
				failures.add(dsd);
			}

			if (heelOptions.getMaxVolumeAvailable() > 0) {
				if (heelOptions.getCvValue() < 0.001) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A CV value should be specified"));
					dsd.addEObjectAndFeature(heelOptions, CargoPackage.eINSTANCE.getStartHeelOptions_CvValue());
					failures.add(dsd);
				}
				if (heelOptions.getCvValue() > 40.0) {
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("CV value is too high"));
					dsd.addEObjectAndFeature(heelOptions, CargoPackage.eINSTANCE.getStartHeelOptions_CvValue());
					failures.add(dsd);
				}

				if (heelOptions.getPriceExpression() != null && !heelOptions.getPriceExpression().isEmpty()) {

					ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, heelOptions, CargoPackage.Literals.START_HEEL_OPTIONS__PRICE_EXPRESSION,
							heelOptions.getPriceExpression());
					if (!result.isOk()) {
						String message = String.format("[Heel Price] %s", result.getErrorDetails());
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dcsd.addEObjectAndFeature(heelOptions, CargoPackage.Literals.START_HEEL_OPTIONS__PRICE_EXPRESSION);
						failures.add(dcsd);
					}
				}
			}
		}
		if (failures.isEmpty())

		{
			return ctx.createSuccessStatus();
		} else if (failures.size() == 1) {
			return failures.get(0);
		} else {
			final MultiStatus multi = new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, null, null);
			for (final IStatus s : failures) {
				multi.add(s);
			}
			return multi;
		}
	}
}
