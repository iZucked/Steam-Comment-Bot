/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

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
			final MMXRootObject rootObject = Activator.getDefault().getExtraValidationContext().getRootObject();
			if (rootObject != null) {
				final PortModel ports = rootObject.getSubModel(PortModel.class);
				if (ports != null) {
					final HashSet<APort> unpricedPorts = new HashSet<APort>();
					unpricedPorts.addAll(ports.getPorts());
					for (final CooldownPrice c : pm.getCooldownPrices()) {
						unpricedPorts.removeAll(SetUtils.getPorts(c.getPorts()));
					}
					if (!unpricedPorts.isEmpty()) {
						final SortedSet<String> names = new TreeSet<String>();
						for (final APort p : unpricedPorts) names.add(""+p.getName());
						return ctx.createFailureStatus(names.toString());
					}
				}
			}
		}
		
		return ctx.createSuccessStatus();
	}
}
