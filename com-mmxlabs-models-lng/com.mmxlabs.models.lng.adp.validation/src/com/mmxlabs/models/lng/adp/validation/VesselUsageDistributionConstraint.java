/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.VesselUsageDistribution;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.lng.types.util.ValidationConstants;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusFactory;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class VesselUsageDistributionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof final VesselUsageDistribution distribution) {
			DetailConstraintStatusFactory factory = DetailConstraintStatusFactory.makeStatus() //
					.withName("Vessel Usage") //
					.withTag(ValidationConstants.TAG_ADP);
			if (distribution.getCargoes() <= 0) {
				factory.copyName() //
						.withObjectAndFeature(target, ADPPackage.eINSTANCE.getVesselUsageDistribution_Cargoes()) //
						.withMessage("Number of cargoes must be greater than zero") //
						.make(ctx, statuses);
			}
			if (SetUtils.getObjects(distribution.getVessels()).isEmpty()) {
				factory.copyName() //
						.withObjectAndFeature(target, ADPPackage.eINSTANCE.getVesselUsageDistribution_Vessels()) //
						.withMessage("No vessels selected") //
						.make(ctx, statuses);
			}
		}
	}
}
