/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.pricing.validation.utils.PriceExpressionUtils;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

/**
 * A constraint for ensuring that all ports have a cooldown constraint in the given pricing model.
 * 
 * @author hinton
 * 
 */
public class CooldownPricingConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(IValidationContext ctx, final IExtraValidationContext extraContext, List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		
		if (target instanceof CooldownPrice) {
			CooldownPrice c = (CooldownPrice) target;
			if (c.getExpression() == null) {
				final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus("Cooldown definition is missing a price expression."));
				dcsd.addEObjectAndFeature(c,  PricingPackage.Literals.PORTS_EXPRESSION_MAP__EXPRESSION);
				failures.add(dcsd);
			} else {
				PriceExpressionUtils.validatePriceExpression(ctx, c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__EXPRESSION, c.getExpression(), failures);			
			}
			
//			
		}
		
		if (target instanceof PricingModel) {
			final PricingModel pm = (PricingModel) target;
			final MMXRootObject rootObject = extraContext.getRootObject();
			if (rootObject instanceof LNGScenarioModel) {
				final PortModel ports = ((LNGScenarioModel)rootObject).getPortModel();
				if (ports != null) {
					// count the number of cooldown prices attached to each port
					final HashMap<Port, Set<CooldownPrice>> pricingPerPort = new HashMap<Port, Set<CooldownPrice>>();
					for (final Port port : ports.getPorts()) {
						pricingPerPort.put(port, new HashSet<CooldownPrice>());
					}

					for (final CooldownPrice c : pm.getCooldownPrices()) {
						for (final Port port : SetUtils.getObjects(c.getPorts())) {
							pricingPerPort.get(port).add(c);
						}
						
					}


					for (final Entry<Port, Set<CooldownPrice>> entry : pricingPerPort.entrySet()) {
						final Port port = entry.getKey();
						final int count = entry.getValue().size();

						if (count != 1 && port.getCapabilities().contains(PortCapability.LOAD)) {
							final String message = String.format("Load port %s has %d cooldown prices specified - there should be exactly one price.", port.getName(), count);
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dcsd.addEObjectAndFeature(port, null);
							if (count > 0) {
								for (final CooldownPrice c : entry.getValue()) {
									dcsd.addEObjectAndFeature(c, PricingPackage.Literals.PORTS_EXPRESSION_MAP__PORTS);
								}
							}
							failures.add(dcsd);
						}
					}
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
