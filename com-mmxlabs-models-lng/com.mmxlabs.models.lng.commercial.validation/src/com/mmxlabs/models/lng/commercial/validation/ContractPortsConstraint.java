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
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ContractPortsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Contract) {
			final Contract contract = (Contract) target;

			if (!contract.getAllowedPorts().isEmpty()) {
				if (contract.getPreferredPort() != null) {
					
					if (!SetUtils.getObjects(contract.getAllowedPorts()).contains(contract.getPreferredPort())) {
						final DetailConstraintStatusDecorator status = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(String.format(
								"Contract preferred port %s is not in the list of allowed ports.", contract.getPreferredPort().getName())));
						status.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_PreferredPort());
						statuses.add(status);
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
