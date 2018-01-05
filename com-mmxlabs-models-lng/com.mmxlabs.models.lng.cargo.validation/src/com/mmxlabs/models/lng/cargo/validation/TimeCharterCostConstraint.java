/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
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
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
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
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();
		if (object instanceof VesselAvailability) {
			final VesselAvailability vesselAvailability = (VesselAvailability) object;
			final Vessel vessel = vesselAvailability.getVessel();
			final String vesselName = vessel == null ? "<Unknown>" : vessel.getName();
			if (vesselAvailability.isSetTimeCharterRate()) {
				addErrors(vesselAvailability, CargoPackage.eINSTANCE.getVesselAvailability_TimeCharterRate(), "charter", ctx, failures);
			}
		}

		return Activator.PLUGIN_ID;
	}
	
	private void addErrors(VesselAvailability vesselAvailability, EAttribute attribute, String msg, final IValidationContext ctx, final List<IStatus> failures) {
		Object feature = vesselAvailability.eGet(attribute);	
		if (feature == null) {
				final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(
						(IConstraintStatus) ctx.createFailureStatus(String.format("Missing %s expression for vessel %s", msg, vesselAvailability.getVessel())));
				dsd.addEObjectAndFeature(vesselAvailability, attribute);
				failures.add(dsd);
			} else {
				final ValidationResult result = PriceExpressionUtils.validatePriceExpression(ctx, vesselAvailability, attribute,
						(String) feature, PriceIndexType.CHARTER);
				if (!result.isOk()) {
					final String message = String.format("[Vessel|'%s']%s", vesselAvailability.getVessel().getName(), result.getErrorDetails());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
					dsd.addEObjectAndFeature(vesselAvailability, attribute);
					failures.add(dsd);
				}
			}
	}


}
