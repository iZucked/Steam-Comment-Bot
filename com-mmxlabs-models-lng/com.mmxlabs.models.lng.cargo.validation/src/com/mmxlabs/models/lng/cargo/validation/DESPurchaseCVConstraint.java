/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

/**
 * Constraint to ensure a DES Purchase has a valid CV set.
 * 
 * @author Simon Goodall
 * 
 */
public class DESPurchaseCVConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> failures) {
		final EObject target = ctx.getTarget();

		if (target instanceof LoadSlot) {
			final LoadSlot loadSlot = (LoadSlot) target;
			// Is there a slot CV value?
			if (!loadSlot.isSetCargoCV()) {
				// No? Then check the contract for a CV
				if (loadSlot.isSetContract()) {
					Contract c = loadSlot.getContract();
					if (c instanceof PurchaseContract) {
						PurchaseContract purchaseContract = (PurchaseContract) c;
						if (!purchaseContract.isSetCargoCV()) {
							Port preferredPort = purchaseContract.getPreferredPort();
							// We can actually have a CV set on a discharge port (field is not unsettable) and this will be used through the getUnsetValue methods.
							// if (preferredPort != null && preferredPort.getCapabilities().contains(PortCapability.LOAD)) {
							if (preferredPort != null && preferredPort.getCvValue() != 0.0) {
								return Activator.PLUGIN_ID;
							}
						}
					}
				}
				// No contract CV, check the port
				final Port port = loadSlot.getPort();
				// We can actually have a CV set on a discharge port (field is not unsettable) and this will be used through the getUnsetValue methods.
				// if (port != null && !port.getCapabilities().contains(PortCapability.LOAD)) {
				if (port != null && port.getCvValue() == 0.0) {
					final String format = "[DES Purchase|%s] needs a CV set or be linked to a load port.";
					final String failureMessage = String.format(format, loadSlot.getName());
					final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(failureMessage));
					dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getLoadSlot_CargoCV());
					failures.add(dsd);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
}
