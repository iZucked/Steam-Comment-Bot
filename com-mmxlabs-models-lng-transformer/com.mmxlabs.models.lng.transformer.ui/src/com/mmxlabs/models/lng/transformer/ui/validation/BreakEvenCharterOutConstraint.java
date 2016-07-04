/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.transformer.ui.internal.Activator;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;
import com.mmxlabs.scheduler.optimiser.scheduleprocessor.breakeven.IBreakEvenEvaluator;

public class BreakEvenCharterOutConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Slot) {

			final Slot slot = (Slot) target;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final SpotMarketsModel spotMarketsModel = ((LNGScenarioModel) rootObject).getReferenceModel().getSpotMarketsModel();

				if (spotMarketsModel == null) {
					// Nothing to validate against
					return Activator.PLUGIN_ID;
				}
				boolean charterOutEnabled = false;
				for (final CharterOutMarket ccm : spotMarketsModel.getCharterOutMarkets()) {
					if (ccm.isEnabled()) {
						charterOutEnabled = true;
						break;
					}
				}

				if (!charterOutEnabled) {
					// Charter out mode not enabled
					return Activator.PLUGIN_ID;
				}

				if (slot.isSetPriceExpression() && slot.getPriceExpression().contains(IBreakEvenEvaluator.MARKER)) {
					final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator(
							(IConstraintStatus) ctx.createFailureStatus("Missing prices cannot be used with charter out generation"));
					dcsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_PriceExpression());
					statuses.add(dcsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
