/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.validation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.google.common.base.Joiner;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PortCostEntry;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.validation.internal.Activator;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class PortCostsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof PortCost) {
			final PortCost portCost = (PortCost) target;
			boolean containsLoad = false;
			boolean containsDischarge = false;
			List<String> portNames = new LinkedList<String>();
			for (Port port : SetUtils.getObjects(portCost.getPorts())) {
				if (port.getCapabilities().contains(PortCapability.DISCHARGE)) {
					containsDischarge = true;
				}
				if (port.getCapabilities().contains(PortCapability.LOAD)) {
					containsLoad = true;
				}
				portNames.add(port.getName());
			}
			Joiner joiner = Joiner.on(", ").skipNulls();
			String portsInQuestion = joiner.join(portNames);
			
			for (PortCostEntry entry : portCost.getEntries()) {
				PortCapability capability = entry.getActivity();
				if (capability.equals(PortCapability.LOAD) && containsLoad && entry.getCost() <= 0) {
					final String failureMessage = String.format("Port%s [%s] contain%s a load capability but load cost is <= $0", portNames.size() > 1 ? "s" : "", portsInQuestion, portNames.size() > 1 ? "" : "s");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
					dsd.addEObjectAndFeature(portCost, PricingPackage.Literals.COST_MODEL__PORT_COSTS);
					failures.add(dsd);
				}
				if (capability.equals(PortCapability.DISCHARGE) && containsDischarge && entry.getCost() <= 0) {
					final String failureMessage = String.format("Port%s [%s] contain%s a discharge capability but discharge cost is <= $0", portNames.size() > 1 ? "s" : "", portsInQuestion, portNames.size() > 1 ? "" : "s");
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage, IStatus.WARNING));
					dsd.addEObjectAndFeature(portCost, PricingPackage.Literals.COST_MODEL__PORT_COSTS);
					failures.add(dsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
