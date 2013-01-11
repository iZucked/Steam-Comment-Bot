/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ContractCvRangeConstraint extends AbstractModelConstraint  {

	@Override
	public IStatus validate(IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof SalesContract) {
			final SalesContract contract = (SalesContract) target;
			if (contract.isSetMinCvValue() && contract.isSetMaxCvValue()) {
				double minCvValue = contract.getMinCvValue();
				double maxCvValue = contract.getMaxCvValue();
			
				if (minCvValue > maxCvValue) {
					String failureMessage = String.format("Contract '%s' has minimum CV %.2f greater than maximum CV %.2f", contract.getName(), minCvValue, maxCvValue);
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
					dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getSalesContract_MinCvValue());
					dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getSalesContract_MaxCvValue());
					return dsd;
				}

			}
		}
		
		return ctx.createSuccessStatus();
	}

}
