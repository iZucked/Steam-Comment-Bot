/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;

import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.ValidationSupport;

/**
 * A constraint for ensuring that all ports have a cooldown constraint in the given pricing model.
 * 
 * @author hinton
 *
 */
public class CooldownPricingConstraint extends AbstractModelConstraint {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.validation.AbstractModelConstraint#validate(org.eclipse.emf.validation.IValidationContext)
	 */
	@Override
	public IStatus validate(final IValidationContext ctx) {
		final EObject target = ctx.getTarget();
		
		if (target instanceof PricingModel) {
			final PricingModel pm = (PricingModel) target;
			final MMXRootObject rootObject = ValidationSupport.getInstance().getParentObjectType(MMXRootObject.class, pm);
			if (rootObject != null) {
				final PortModel ports = rootObject.getSubModel(PortModel.class);
				if (ports != null) {
					final HashSet<APort> pricedPorts = new HashSet<APort>();
					for (final CooldownPrice c : pm.getCooldownPrices()) {
						pricedPorts.addAll(SetUtils.getPorts(c.getPorts()));
					}
					pricedPorts.removeAll(ports.getPorts());
					if (!pricedPorts.isEmpty()) {
						return ctx.createFailureStatus(pricedPorts.toString());
					}
				}
			}
		}
		
		return ctx.createSuccessStatus();
	}
}
