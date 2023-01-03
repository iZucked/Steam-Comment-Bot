/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class BreakEvenCharterOutConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();

		if (target instanceof Slot<?> slot) {
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final SpotMarketsModel spotMarketsModel = ((LNGScenarioModel) rootObject).getReferenceModel().getSpotMarketsModel();

				if (spotMarketsModel == null) {
					// Nothing to validate against
					return;
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
					return;
				}
			}
		}
	}
}
