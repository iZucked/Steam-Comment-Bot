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
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class RestrictedElementsConstraint extends AbstractModelMultiConstraint {

	private static final String CONTRACT_RESTRICTION = "The contract for %s slot %s is not permitted by the %s slot in this cargo pairing.";
	private static final String PORT_RESTRICTION = "The port for %s slot %s is not permitted by the %s slot in this cargo pairing.";
	private static final String LOAD = "load";
	private static final String DISCHARGE = "discharge";

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Cargo) {
			final Cargo cargo = (Cargo) target;

			for (int i = 0; i < cargo.getSlots().size(); ++i) {
				for (int j = i + 1; j < cargo.getSlots().size(); ++j) {
					final Slot slotI = cargo.getSlots().get(i);
					final Slot slotJ = cargo.getSlots().get(j);
					checkSlot(ctx, slotI.getContract(), slotJ, statuses);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}

	private void checkSlot(final IValidationContext ctx, final Contract contract, final Slot slot, final List<IStatus> statuses) {
		if (contract == null) {
			return;
		}

		final String typeA;
		final String typeB;

		if (slot instanceof LoadSlot) {
			typeA = LOAD;
			typeB = DISCHARGE;
		} else {
			typeB = LOAD;
			typeA = DISCHARGE;
		}

		if (slot.getContract() != null) {
			if (!contract.getRestrictedContracts().isEmpty()) {
				if (contract.isRestrictedListsArePermissive()) {
					// White list
					if (!contract.getRestrictedContracts().contains(slot.getContract())) {
						final String msg = String.format(CONTRACT_RESTRICTION, typeA, slot.getName(), typeB);
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
						statuses.add(d);
					}

				} else {
					// Black list
					if (contract.getRestrictedContracts().contains(slot.getContract())) {
						final String msg = String.format(CONTRACT_RESTRICTION, typeA, slot.getName(), typeB);
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Contract());
						statuses.add(d);
					}
				}
			}
		}
		if (slot.getPort() != null) {
			if (!contract.getRestrictedPorts().isEmpty()) {
				if (contract.isRestrictedListsArePermissive()) {
					// White list
					if (!contract.getRestrictedPorts().contains(slot.getPort())) {
						final String msg = String.format(PORT_RESTRICTION, typeA, slot.getName(), typeB);
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
						statuses.add(d);
					}

				} else {
					// Black list
					if (contract.getRestrictedPorts().contains(slot.getPort())) {
						final String msg = String.format(PORT_RESTRICTION, typeA, slot.getName(), typeB);
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_Port());
						statuses.add(d);
					}
				}
			}
		}
	}
}
