/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class SlotPortCapabilityConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(@NonNull IValidationContext ctx, @NonNull IExtraValidationContext extraContext, @NonNull List<IStatus> statuses) {

		final EObject object = ctx.getTarget();
		if (object instanceof Slot) {
			final EMFEventType eventType = ctx.getEventType();

			// This is being triggered by a batch mode validation.
			if (eventType == EMFEventType.NULL) {

				final Slot slot = (Slot) object;

				final Port port = slot.getPort();
				// Null ports outside of CargoModel are ok (specifically we expect them to be SpotSlots for non-shipped markets which will be filled in when the Schedule is applied to the scenario.)
				if (port == null) {
					if (slot.eContainer() instanceof CargoModel || extraContext.getContainer(slot) instanceof CargoModel) {
						String message = String.format("[Slot|%s has no port", slot.getName());

						final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
						dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
						statuses.add(dsd);
					}
				}

				if (port != null) {
					PortCapability requiredCapability = null;
					Cargo cargo = null;

					if (slot instanceof LoadSlot) {
						final LoadSlot loadSlot = (LoadSlot) slot;
						cargo = loadSlot.getCargo();

						if (loadSlot.getTransferFrom() != null) {
							requiredCapability = PortCapability.TRANSFER;
						} else if (!loadSlot.isDESPurchase()) {
							requiredCapability = PortCapability.LOAD;
						}
					}

					else if (slot instanceof DischargeSlot) {
						final DischargeSlot dischargeSlot = (DischargeSlot) slot;
						cargo = dischargeSlot.getCargo();

						if (dischargeSlot.getTransferTo() != null) {
							requiredCapability = PortCapability.TRANSFER;
						} else if (!dischargeSlot.isFOBSale()) {
							requiredCapability = PortCapability.DISCHARGE;
						}
					}

					final EList<PortCapability> capabilities = port.getCapabilities();
					if (requiredCapability != null) {
						if (!capabilities.contains(requiredCapability)) {
							String message = String.format("[Slot|%s] Port %s lacks the required %s capability", slot.getName(), port.getName(), requiredCapability);
							final DetailConstraintStatusDecorator dsd = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(message));
							dsd.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
							if (cargo != null) {
								dsd.addEObjectAndFeature(cargo, CargoPackage.eINSTANCE.getCargo_Slots());
							}
							statuses.add(dsd);
						}
					}
				}
			}
		}
		return Activator.PLUGIN_ID;
	}

}
