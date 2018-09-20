/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class ADPVesselEventConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(@NonNull final IValidationContext ctx, @NonNull final IExtraValidationContext extraContext, @NonNull final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof VesselEvent) {
			final VesselEvent vesselEvent = (VesselEvent) target;

			final DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName(vesselEvent.getName());

			final ADPModel adpModel = ScenarioModelUtil.getADPModel(extraContext.getScenarioDataProvider());
			if (adpModel != null) {
				if (!(vesselEvent instanceof DryDockEvent)) {
					factory.copyName() //
							.withObjectAndFeature(vesselEvent, MMXCorePackage.Literals.NAMED_OBJECT__NAME) //
							.withMessage("Only dry-dock events are supported in ADP mode.") //
							.make(ctx, statuses);
				}
			}
		}
	}
}
