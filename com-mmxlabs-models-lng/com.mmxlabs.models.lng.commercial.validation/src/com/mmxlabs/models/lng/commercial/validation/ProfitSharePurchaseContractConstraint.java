/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract;
import com.mmxlabs.models.lng.commercial.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class ProfitSharePurchaseContractConstraint extends AbstractModelConstraint {
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();

		final List<IStatus> failures = new LinkedList<IStatus>();

		if (target instanceof ProfitSharePurchaseContract) {
			final ProfitSharePurchaseContract contract = (ProfitSharePurchaseContract) target;
			if (contract.getBaseMarketMultiplier() < 0.001) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Base market multipler should be a positive number"));
				dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getProfitSharePurchaseContract_BaseMarketMultiplier());
				failures.add(dsd);
			}
			if (contract.getRefMarketMultiplier() < 0.001) {

				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Reference market multipler should be a positive number"));
				dsd.addEObjectAndFeature(contract, CommercialPackage.eINSTANCE.getProfitSharePurchaseContract_RefMarketMultiplier());
				failures.add(dsd);
			}
		}
		if (failures.isEmpty()) {
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
