/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class DivertibleSlotConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> failures) {
		final EObject object = ctx.getTarget();

		// Valid slot data checks
		if (object instanceof Slot) {
			if (object instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) object;
				if (loadSlot.isDESPurchase()) {
					if (loadSlot.isDivertible() && (loadSlot.getPort() != null && !loadSlot.getPort().getCapabilities().contains(PortCapability.LOAD))) {
						final String message = String.format("DES Purchase|%s is divertable and needs a load port.", loadSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Divertible());
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);
					}
					if (!loadSlot.isDivertible() && (loadSlot.getPort() != null && !loadSlot.getPort().getCapabilities().contains(PortCapability.DISCHARGE))) {
						final String message = String.format("DES Purchase|%s is not divertable and needs a discharge port.", loadSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Divertible());
						dsd.addEObjectAndFeature(loadSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);
					}
				}

			}

			else if (object instanceof DischargeSlot) {
				final DischargeSlot dischargeSlot = (DischargeSlot) object;
				if (dischargeSlot.isFOBSale()) {
					if (dischargeSlot.isDivertible() && (dischargeSlot.getPort() != null && !dischargeSlot.getPort().getCapabilities().contains(PortCapability.DISCHARGE))) {
						final String message = String.format("FOB Sale|%s is divertable and needs a discharge port.", dischargeSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Divertible());
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);
					}
					if (!dischargeSlot.isDivertible() && (dischargeSlot.getPort() != null && !dischargeSlot.getPort().getCapabilities().contains(PortCapability.LOAD))) {
						final String message = String.format("FOB Sale|%s is not divertable and needs a load port.", dischargeSlot.getName());
						final IConstraintStatus status = (IConstraintStatus) ctx.createFailureStatus(message);
						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator(status);
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Divertible());
						dsd.addEObjectAndFeature(dischargeSlot, CargoPackage.eINSTANCE.getSlot_Port());
						failures.add(dsd);
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}
}
