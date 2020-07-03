/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.util.SetUtils;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;
import com.mmxlabs.models.ui.validation.IExtraValidationContext;

public class EmptyRestrictionsConstraint extends AbstractModelMultiConstraint {

	@Override
	protected String validate(final IValidationContext ctx, final IExtraValidationContext extraContext, final List<IStatus> statuses) {

		final EObject target = ctx.getTarget();
		if (target instanceof Slot) {
			final Slot slot = (Slot) target;
			checkSlot(ctx, slot, statuses);
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
	private void checkSlot(final IValidationContext ctx, final Slot slot, final List<IStatus> statuses) {

		final List<Slot> restrictedSlots = slot.getRestrictedSlots();
		final List<Contract> restrictedContracts;
		final List<APortSet<Port>> restrictedPortSets;

		boolean restrictedSlotsArePermissive = slot.isRestrictedSlotsArePermissive();
		boolean restrictedContractsArePermissive = slot.getSlotOrDelegateContractRestrictionsArePermissive();
		boolean restrictedPortsArePermissive = slot.getSlotOrDelegatePortRestrictionsArePermissive();


		final Contract contract = slot.getContract();

		if (slot instanceof SpotSlot) {
			SpotSlot spotSlot = (SpotSlot) slot;
			SpotMarket market = spotSlot.getMarket();
			if (market != null) {
				restrictedContractsArePermissive = market.isRestrictedContractsArePermissive();
				restrictedPortsArePermissive = market.isRestrictedPortsArePermissive();
				restrictedContracts = market.getRestrictedContracts();
				restrictedPortSets = new LinkedList<>(SetUtils.getObjects(market.getRestrictedPorts()));
			} else {
				restrictedContractsArePermissive = false;
				restrictedPortsArePermissive = false;
				restrictedContracts = Collections.emptyList();
				restrictedPortSets = Collections.emptyList();
			}
		} else {
			restrictedContracts = slot.getSlotOrDelegateContractRestrictions();
			restrictedPortSets = slot.getSlotOrDelegatePortRestrictions();
		}
		
		if (restrictedContracts.isEmpty() && restrictedContractsArePermissive) {
			final String msg = "Empty allowed contracts list.";
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive());
			statuses.add(d);
		}
		
		if (restrictedPortSets.isEmpty() && restrictedPortsArePermissive) {
			final String msg = "Empty allowed ports list.";
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive());
			statuses.add(d);
		}
		if (restrictedSlots.isEmpty() && restrictedSlotsArePermissive) {
			final String msg = "Empty allowed slots list.";
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedSlotsArePermissive());
			statuses.add(d);
		}
	}
}
