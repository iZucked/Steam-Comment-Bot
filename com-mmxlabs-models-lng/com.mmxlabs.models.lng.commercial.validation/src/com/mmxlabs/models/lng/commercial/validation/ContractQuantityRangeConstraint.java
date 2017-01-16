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
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractQuantityRangeConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof Contract) {
			final Contract contract = (Contract) target;
			DetailConstraintStatusDecorator rangeCheckDSD = checkRange(contract, ctx);
			if (rangeCheckDSD != null) {
				failures.add(rangeCheckDSD);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private DetailConstraintStatusDecorator checkRange(Contract contract, final IValidationContext ctx) {
		if (contract.getMinQuantity() > 0 && contract.getMaxQuantity() > 0) {
			final double minQuantity = contract.getMinQuantity();
			final double maxQuantity = contract.getMaxQuantity();

			if (minQuantity > maxQuantity) {
				final String failureMessage = String.format("Contract '%s' has minimum quantity (%.2f) greater than maximum quantity (%.2f)", contract.getName(), minQuantity, maxQuantity);
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__MIN_QUANTITY);
				dsd.addEObjectAndFeature(contract, CommercialPackage.Literals.CONTRACT__MAX_QUANTITY);
				return dsd;
			}

		}
		return null;
	}

}
