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
import com.mmxlabs.models.lng.adp.ShippingOption;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks that {@link BaseFuel} values are none-zero
 * 
 * @author Simon Goodall
 * 
 */
public class ShippingOptionConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof ShippingOption) {
			ShippingOption shippingOption = (ShippingOption) target;

			if (shippingOption.getVessel() == null && shippingOption.getVesselAssignmentType() == null) {
				createSimpleStatus(ctx, statuses, "Missing default vessel allocation", shippingOption, ADPPackage.Literals.SHIPPING_OPTION__VESSEL,
						ADPPackage.Literals.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE);
			}
			if (shippingOption.getVessel() != null && shippingOption.getVesselAssignmentType() != null) {
				createSimpleStatus(ctx, statuses, "Choose nominated vessel or vessel assignment type", shippingOption, ADPPackage.Literals.SHIPPING_OPTION__VESSEL,
						ADPPackage.Literals.SHIPPING_OPTION__VESSEL_ASSIGNMENT_TYPE);
			}

		}

		return Activator.PLUGIN_ID;
	}
}
