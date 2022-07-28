/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.pricing.util.PriceIndexUtils.PriceIndexType;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils.ValidationResult;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Check that if a time charter rate is set, it is not zero.
 * 
 * @author Simon Goodall
 * 
 */
public class TimeCharterCostConstraint extends AbstractModelMultiConstraint {

	@Override
	protected void doValidate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselCharter vesselCharter) {
			final Vessel vessel = vesselCharter.getVessel();
			final String vesselName = vessel == null ? "<Unknown>" : vessel.getName();
			if (vesselCharter.isSetTimeCharterRate()) {
				addErrors(vesselCharter, CargoPackage.eINSTANCE.getVesselCharter_TimeCharterRate(), "charter", ctx, failures);
			}
		}
	}

	private void addErrors(VesselCharter vesselCharter, EAttribute attribute, String msg, final IValidationContext ctx, final List<IStatus> failures) {
		Object feature = vesselCharter.eGet(attribute);
		if (feature == null) {
			final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
					(IConstraintStatus) ctx.createFailureStatus(String.format("Missing %s expression for vessel %s", msg, vesselCharter.getVessel())));
			dsd.addEObjectAndFeature(vesselCharter, attribute);
			failures.add(dsd);
		} else {
			final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, vesselCharter, attribute, (String) feature, PriceIndexType.CHARTER);
			if (!result.isOk()) {
				final String message = String.format("[Vessel|'%s']%s", vesselCharter.getVessel().getName(), result.getErrorDetails());
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
				dsd.addEObjectAndFeature(vesselCharter, attribute);
				failures.add(dsd);
			}
		}
	}

}
