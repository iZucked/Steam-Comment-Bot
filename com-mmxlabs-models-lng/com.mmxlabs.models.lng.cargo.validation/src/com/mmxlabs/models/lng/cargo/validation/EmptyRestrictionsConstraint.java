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
		List<Contract> restrictedContractsFSlot = new LinkedList<Contract>();
		List<APortSet<Port>> restrictedPortSetsFSlot = new LinkedList<APortSet<Port>>();

		boolean restrictedSlotsArePermissive = slot.isRestrictedSlotsArePermissive();
		
		boolean restrictedPortsArePermissiveSet = slot.isSetRestrictedPortsArePermissive();
		boolean restrictedContractsArePermissiveSet = slot.isSetRestrictedContractsArePermissive();
		


		final String name = slot.getName();

		if (slot instanceof SpotSlot) {
			SpotSlot spotSlot = (SpotSlot) slot;
			SpotMarket market = spotSlot.getMarket();
			if (market != null) {
				restrictedContractsArePermissiveSet = market.isRestrictedContractsArePermissive();
				restrictedPortsArePermissiveSet = market.isRestrictedPortsArePermissive();
				restrictedContracts = market.getRestrictedContracts();
				restrictedPortSets = new LinkedList<>(SetUtils.getObjects(market.getRestrictedPorts()));
			} else {
				restrictedContractsArePermissiveSet = false;
				restrictedPortsArePermissiveSet = false;
				restrictedContracts = Collections.emptyList();
				restrictedPortSets = Collections.emptyList();
			}
		} else {
			restrictedContracts = slot.getSlotOrDelegateContractRestrictions();
			restrictedPortSets = slot.getSlotOrDelegatePortRestrictions();
			
			restrictedContractsFSlot = slot.getRestrictedContracts();
			restrictedPortSetsFSlot = slot.getRestrictedPorts();
		}
		
		if (restrictedContracts.isEmpty() && restrictedContractsArePermissiveSet) {
			final String msg = String.format("%s: Empty contracts restriction list.", name);
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive());
			statuses.add(d);
		}
		
		if (restrictedPortSets.isEmpty() && restrictedPortsArePermissiveSet) {
			final String msg = String.format("%s: Empty ports restriction list.", name);
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive());
			statuses.add(d);
		}
		if (restrictedSlots.isEmpty() && restrictedSlotsArePermissive) {
			final String msg = String.format("%s: Empty allowed slots list.", name);
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedSlotsArePermissive());
			statuses.add(d);
		}
		
		if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedPortsArePermissive()) && restrictedPortSetsFSlot.isEmpty()) {
			final String msg = String.format("%s: Ports restriction list should also be overriden.", name);
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedPorts());
			statuses.add(d);
		}
		
		if (slot.eIsSet(CargoPackage.eINSTANCE.getSlot_RestrictedContractsArePermissive()) && restrictedContractsFSlot.isEmpty()) {
			final String msg = String.format("%s: Contracts restriction list should also be overriden.", name);
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(slot, CargoPackage.eINSTANCE.getSlot_RestrictedContracts());
			statuses.add(d);
		}
	}
}
