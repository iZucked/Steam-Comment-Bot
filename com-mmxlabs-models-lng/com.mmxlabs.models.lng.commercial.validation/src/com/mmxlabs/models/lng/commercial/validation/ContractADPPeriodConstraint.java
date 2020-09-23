/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

public class ContractADPPeriodConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Contract) {
			final Contract contract = (Contract) target;
			
			if (contract.isSetStartDate()) {
				if (contract.getStartDate().getMonthValue() != (contract.getContractYearStart() + 1)) {
					final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
							"Contract %s start date is not aligned ", contract.getName())));
					status.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_StartDate());
					statuses.add(status);
				}
				if (contract.isSetEndDate()) {
					if (contract.getStartDate().isAfter(contract.getEndDate())) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
								"Contract %s start date is after the contract end date", contract.getName())));
						status.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_EndDate());
						statuses.add(status);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
