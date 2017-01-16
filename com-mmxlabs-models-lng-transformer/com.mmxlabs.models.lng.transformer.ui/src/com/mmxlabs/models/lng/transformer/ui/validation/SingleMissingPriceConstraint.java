/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;

public class SingleMissingPriceConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;
			int missingPriceCount = 0;
			for (final Slot slot : cargo.getSlots()) {

				if (slot.isSetPriceExpression()) {
					if (slot.getPriceExpression().equals(IBreakEvenEvaluator.MARKER)) {
						if (++missingPriceCount > 1) {
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("A cargo can have at most one missing price"));
							dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
							statuses.add(dcsd);
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
