/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.types.APort;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * A constraint for ensuring that all ports have a cooldown constraint in the given pricing model.
 * 
 * @author hinton
 * 
 */
public class CooldownPricingConstraint extends AbstractModelConstraint {

	/*
	 * (non-Javadoc)
	 * 
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
					// count the number of cooldown prices attached to each port
					final HashMap<APort, Set<CooldownPrice>> pricingPerPort = new HashMap<APort, Set<CooldownPrice>>();
					for (final APort port : ports.getPorts()) {
						pricingPerPort.put(port, new HashSet<CooldownPrice>());
					}
						
					for (final CooldownPrice c : pm.getCooldownPrices()) {
						for (final APort port : SetUtils.getPorts(c.getPorts())) {
							pricingPerPort.get(port).add(c);
						}
					}

					// find all the ports with less than or more than 1 cooldown price
					final List<IStatus> failures = new LinkedList<IStatus>();

					for (final Entry<APort, Set<CooldownPrice>> entry : pricingPerPort.entrySet()) {
						final APort port = entry.getKey();
						final int count = entry.getValue().size();

						if (count != 1 && (port instanceof Port) && ((Port) port).getCapabilities().contains(PortCapability.LOAD)) {
							final String message = String.format("Load port %s has %d cooldown prices specified.", port.getName(), count);
							final DetailConstraintStatusDecorator dcsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dcsd.addEObjectAndFeature(port, null);
							if (count > 0) {
								for (final CooldownPrice c : entry.getValue()) {
									dcsd.addEObjectAndFeature(c, PricingPackage.eINSTANCE.getCooldownPrice_Ports());
								}
							}
							failures.add(dcsd);
						}
					}

					// return an appropriate validation status: success, 1 failure or compound failure
					if (failures.isEmpty()) {
						return ctx.createSuccessStatus();
					} else if (failures.size() == 1) {
						return failures.get(0);
					} else {
						final String heading = String.format("%s ports have problems with their cooldown prices.", failures.size());
						return new MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR, failures.toArray(new IStatus[] {}), heading, null);
					}

				}
			}
		}

		return ctx.createSuccessStatus();
	}
}
