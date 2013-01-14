/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.trading.integration.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.IBreakEvenEvaluator;
import com.mmxlabs.trading.integration.internal.Activator;

public class SingleMissingPriceConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			final LoadSlot loadSlot = cargo.getLoadSlot();
			final DischargeSlot dischargeSlot = cargo.getDischargeSlot();

			if (loadSlot != null && dischargeSlot != null) {
				if (loadSlot.isSetPriceExpression() && dischargeSlot.isSetPriceExpression()) {
					if (loadSlot.getPriceExpression().contains(IBreakEvenEvaluator.MARKER) && dischargeSlot.getPriceExpression().equals(IBreakEvenEvaluator.MARKER)) {
						final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A cargo can have at most one missing price"));
						dcsd.addEObjectAndFeature(cargo.getLoadSlot(), CargoPackage.eINSTANCE.getSlot_PriceExpression());
						dcsd.addEObjectAndFeature(cargo.getDischargeSlot(), CargoPackage.eINSTANCE.getSlot_PriceExpression());
						statuses.add(dcsd);

					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
