/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractCvRangeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof SalesContract) {
			final SalesContract salesContract = (SalesContract) target;
			DetailConstraintStatusDecorator rangeCheckDSD = checkRange(salesContract, ctx);
			if (rangeCheckDSD != null) {
				failures.add(rangeCheckDSD);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private DetailConstraintStatusDecorator checkRange(SalesContract salesContract, final IValidationContext ctx) {
		if (salesContract.isSetMinCvValue() && salesContract.isSetMaxCvValue()) {
			final double minCvValue = salesContract.getMinCvValue();
			final double maxCvValue = salesContract.getMaxCvValue();

			if (minCvValue > maxCvValue) {
				final String failureMessage = String.format("Contract '%s' has minimum CV (%.2f) greater than maximum CV (%.2f)", salesContract.getName(), minCvValue, maxCvValue);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(salesContract, CommercialPackage.Literals.SALES_CONTRACT__MIN_CV_VALUE);
				dsd.addEObjectAndFeature(salesContract, CommercialPackage.Literals.SALES_CONTRACT__MAX_CV_VALUE);
				return dsd;
			}

		}
		return null;
	}

}
