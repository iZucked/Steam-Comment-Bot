/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class RestrictedElementsConstraint extends AbstractModelMultiConstraint {

	private static final String CONTRACT_RESTRICTION = "[Cargo|'%s'] '%s' does not permit %s contract '%s'.";
	private static final String PORT_RESTRICTION = "[Cargo|'%s] '%s' does not permit %s port '%s'.";
	private static final String LOAD = "load";
	private static final String DISCHARGE = "discharge";

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;

			for (int i = 0; i < cargo.getSlots().size(); ++i) {
				for (int j = 0; j < cargo.getSlots().size(); ++j) {
					if (i == j) {
						continue;
					}
					final Slot slotI = cargo.getSlots().get(i);
					final Slot slotJ = cargo.getSlots().get(j);
					checkSlotAgainstSlotAndContract(ctx, slotI, slotJ, cargo.getLoadName(), statuses);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	/**
	 * Check slot2 can be paired to slot1 based on slot1's restrictions
	 * 
	 * @param ctx
	 * @param slot1
	 * @param slot2
	 * @param cargoName
	 * @param statuses
	 */
	private void checkSlotAgainstSlotAndContract(final IValidationContext ctx, final Slot slot1, final Slot slot2, final String cargoName, final List<IStatus> statuses) {

		final String typeA;

		if (slot2 instanceof LoadSlot) {
			typeA = LOAD;
		} else {
			typeA = DISCHARGE;
		}

		final List<Contract> restrictedContracts;
		final List<?> restrictedPorts;
		boolean restrictedListsArePermissive = false;
		final String cause;
		// if (slot1.isSetRestrictedContracts() || slot1.isSetRestrictedListsArePermissive() || slot1.isSetRestrictedPorts()) {
		if (slot1.isOverrideRestrictions()) {
			restrictedListsArePermissive = slot1.isRestrictedListsArePermissive();
			restrictedContracts = slot1.getRestrictedContracts();
			restrictedPorts = slot1.getRestrictedPorts();
			cause = "Slot " + slot1.getName();
		} else {
			final Contract contract = slot1.getContract();
			if (contract == null) {
				if (slot1 instanceof SpotSlot) {
					SpotSlot spotSlot = (SpotSlot) slot1;
					SpotMarket market = spotSlot.getMarket();
					if (market != null) {
						restrictedListsArePermissive = market.isRestrictedListsArePermissive();
						restrictedContracts = market.getRestrictedContracts();
						restrictedPorts = new LinkedList<>(SetUtils.getObjects(market.getRestrictedPorts()));
						cause = "Spot market " + market.getName();
					} else {
						restrictedListsArePermissive = false;
						restrictedContracts = Collections.emptyList();
						restrictedPorts = Collections.emptyList();
						cause = "Slot " + slot1.getName();
					}
				} else {
					restrictedListsArePermissive = false;
					restrictedContracts = Collections.emptyList();
					restrictedPorts = Collections.emptyList();
					cause = "Slot " + slot1.getName();
				}
			} else {
				restrictedListsArePermissive = contract.isRestrictedListsArePermissive();
				restrictedContracts = contract.getRestrictedContracts();
				restrictedPorts = new LinkedList<>(SetUtils.getObjects(contract.getRestrictedPorts()));
				cause = "Contract " + contract.getName();
			}
		}

		final Contract contract2 = slot2.getContract();
		if (contract2 != null) {
			if (!restrictedContracts.isEmpty()) {
				if (restrictedContracts.contains(contract2) != restrictedListsArePermissive) {
					final String msg = String.format(CONTRACT_RESTRICTION, cargoName, cause, typeA, contract2.getName());
					final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
					d.addEObjectAndFeature(slot2, CargoPackage.eINSTANCE.getSlot_Contract());
					statuses.add(d);
				}
			}
		}
		final Port port2 = slot2.getPort();
		if (slot2.getPort() != null) {
			if (!restrictedPorts.isEmpty()) {
				final boolean contains = restrictedPorts.contains(slot2.getPort());
				if ((restrictedListsArePermissive && !contains) // Whitelist
						|| !restrictedListsArePermissive && contains) { // Blacklist
					final String msg = String.format(PORT_RESTRICTION, cargoName, cause, typeA, slot2.getPort().getName());
					final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
					d.addEObjectAndFeature(slot2, CargoPackage.eINSTANCE.getSlot_Port());
					statuses.add(d);
				}
			}
		}
	}
}
