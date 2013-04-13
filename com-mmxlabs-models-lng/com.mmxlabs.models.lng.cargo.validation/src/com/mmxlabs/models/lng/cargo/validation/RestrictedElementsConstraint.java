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

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class RestrictedElementsConstraint extends AbstractModelMultiConstraint {

//	private static final String CONTRACT_RESTRICTION = "The contract for %s slot %s is not permitted by the %s slot for this cargo.";
	private static final String CONTRACT_RESTRICTION = "[Cargo|'%s'] Contract '%s' does not permit %s contract '%s'.";
//	private static final String PORT_RESTRICTION = "The port for %s slot %s is not permitted by the %s slot for this cargo.";
	private static final String PORT_RESTRICTION = "[Cargo|'%s'] Contract '%s' does not permit %s port '%s'.";
	private static final String LOAD = "load";
	private static final String DISCHARGE = "discharge";

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;

			final LoadSlot loadSlot = cargo.getLoadSlot();
			final DischargeSlot dischargeSlot = cargo.getDischargeSlot();

			if (loadSlot != null && dischargeSlot != null) {
				checkSlot(ctx, loadSlot.getContract(), dischargeSlot, cargo.getName(), statuses);
				checkSlot(ctx, dischargeSlot.getContract(), loadSlot, cargo.getName(), statuses);
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void checkSlot(final IValidationContext ctx, final Contract contract, final Slot slot, String cargoName, final List<IStatus> statuses) {
		if (contract == null) {
			return;
		}

		final String contractName = contract.getName();
		final String typeA;
//		final String typeB;

		if (slot instanceof LoadSlot) {
			typeA = LOAD;
//			typeB = DISCHARGE;
		} else {
//			typeB = LOAD;
			typeA = DISCHARGE;
		}

		Contract slotContract = slot.getContract();
		boolean restrictedListsArePermissive = contract.isRestrictedListsArePermissive();
		if (slotContract != null) {
			if (!contract.getRestrictedContracts().isEmpty()) {
				boolean contains = contract.getRestrictedContracts().contains(slotContract);
				if ((restrictedListsArePermissive && !contains) // Whitelist
					|| !restrictedListsArePermissive && contains){ // Blacklist
						final String msg = String.format(CONTRACT_RESTRICTION, cargoName, contractName, typeA, slotContract.getName());
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
						statuses.add(d);
				}
			}
		}
		if (slot.getPort() != null) {
			if (!contract.getRestrictedPorts().isEmpty()) {
				boolean contains = contract.getRestrictedPorts().contains(slot.getPort());
				if ((restrictedListsArePermissive && !contains) // Whitelist
						|| !restrictedListsArePermissive && contains){ // Blacklist
						final String msg = String.format(PORT_RESTRICTION, cargoName, contractName, typeA, slot.getPort().getName());
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
						statuses.add(d);
				}
			}
		}
	}
}
