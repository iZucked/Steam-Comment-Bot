/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DeliverToSpotFlow;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * 
 * @author Simon Goodall
 * 
 */
public class DeliverToSpotFlowConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof DeliverToSpotFlow) {
			final DeliverToSpotFlow flow = (DeliverToSpotFlow) target;

			if (flow.getMarket() == null) {
				createSimpleStatus(ctx, statuses, "Missing spot market allocation", flow, ADPPackage.Literals.DELIVER_TO_SPOT_FLOW__MARKET);
			}

		}

		return Activator.PLUGIN_ID;
	}
}
