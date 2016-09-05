/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.adp.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.google.inject.Binding;
import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.BindingRule;
import com.mmxlabs.models.lng.adp.validation.internal.Activator;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * Checks that {@link BaseFuel} values are none-zero
 * 
 * @author Simon Goodall
 * 
 */
public class BindingRuleConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {
		final EObject target = ctx.getTarget();

		if (target instanceof BindingRule) {
			BindingRule bindingRule = (BindingRule) target;
			if (bindingRule.getFlowType() == null) {
				createSimpleStatus(ctx, statuses, bindingRule, ADPPackage.Literals.BINDING_RULE__FLOW_TYPE, "Missing flow type");
			}
			if (bindingRule.getProfile() == null) {
				createSimpleStatus(ctx, statuses, bindingRule, ADPPackage.Literals.BINDING_RULE__PROFILE, "Missing profile");
			}
			if (bindingRule.getSubProfile() == null) {
				createSimpleStatus(ctx, statuses, bindingRule, ADPPackage.Literals.BINDING_RULE__SUB_PROFILE, "Missing sub profile");
			}
			if (bindingRule.getShippingOption() == null) {
				createSimpleStatus(ctx, statuses, bindingRule, ADPPackage.Literals.BINDING_RULE__SHIPPING_OPTION, "Missing shipping option");
			}
			if (bindingRule.getProfile() != null && bindingRule.getSubProfile() != null) {
				if (!bindingRule.getProfile().getSubProfiles().contains(bindingRule.getSubProfile())) {
					createSimpleStatus(ctx, statuses, bindingRule, ADPPackage.Literals.BINDING_RULE__SUB_PROFILE, "Invalid sub-profile");
				}
			}
			if (bindingRule.getSubProfile() == null) {
				createSimpleStatus(ctx, statuses, bindingRule, ADPPackage.Literals.BINDING_RULE__SUB_PROFILE, "Missing sub profile");
			}
		}

		return Activator.PLUGIN_ID;
	}
}
