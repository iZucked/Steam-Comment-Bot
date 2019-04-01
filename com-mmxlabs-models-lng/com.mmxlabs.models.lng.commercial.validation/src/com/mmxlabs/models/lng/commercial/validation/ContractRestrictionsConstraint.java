/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
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
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractRestrictionsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Contract) {
			final Contract contract = (Contract) target;

			if (contract.getRestrictedPorts().isEmpty() && contract.isRestrictedPortsArePermissive()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Contract %s has an empty list of allowed ports.", contract.getName())));
				status.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_RestrictedPortsArePermissive());
				statuses.add(status);
			}
			
			if (contract.getRestrictedContracts().isEmpty() && contract.isRestrictedContractsArePermissive()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Contract %s has an empty list of allowed contracts.", contract.getName())));
				status.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_RestrictedContractsArePermissive());
				statuses.add(status);
			}
			
			if (contract.getRestrictedVessels().isEmpty() && contract.isRestrictedVesselsArePermissive()) {
				final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
						"Contract %s has an empty list of allowed vessels.", contract.getName())));
				status.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_RestrictedVesselsArePermissive());
				statuses.add(status);
			}
		}

		return Activator.PLUGIN_ID;
	}
}
