/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PurchaseContractCvConstraint extends AbstractModelConstraint {

	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		if (target instanceof PurchaseContract) {
			final PurchaseContract contract = (PurchaseContract) target;
			if (!contract.isSetCargoCV()) {
				final Port port = contract.getPreferredPort();
				if (port == null) {
					final String failureMessage = String.format("Purchase Contract '%s' needs a preferred load port or a CV value specified", contract.getName());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
					dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_PreferredPort());
					dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getPurchaseContract_CargoCV());
					return dsd;
				} else if (port.getCapabilities().contains(PortCapability.LOAD) == false) {
					final String failureMessage = String.format("Purchase Contract '%s' needs it's preferred port to be a load port or a CV value specified", contract.getName());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
					dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getContract_PreferredPort());
					dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getPurchaseContract_CargoCV());
					return dsd;
				}
			}
		}

		return ctx.createSuccessStatus();
	}

}
