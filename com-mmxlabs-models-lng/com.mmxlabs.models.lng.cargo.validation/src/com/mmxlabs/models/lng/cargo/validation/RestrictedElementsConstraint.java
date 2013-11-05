/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IConstraintStatus;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.validation.internal.Activator;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.ui.validation.AbstractModelMultiConstraint;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class RestrictedElementsConstraint extends AbstractModelMultiConstraint {

//	private static final String CONTRACT_RESTRICTION = "The contract for %s slot %s is not permitted by the %s slot for this cargo.";
	private static final String CONTRACT_RESTRICTION = "[Cargo|'%s'] Contract '%s' does not permit %s contract '%s'.";
//	private static final String PORT_RESTRICTION = "The port for %s slot %s is not permitted by the %s slot for this cargo.";
	private static final String PORT_RESTRICTION = "[Cargo|'%s'] Contract '%s' does not permit %s port '%s'.";
	private static final String RESTRICTION = "[Cargo|'%s'] %s '%s' does not permit %s %s '%s'.";
	private static final String CONTRACT = "contract";
	private static final String PORT = "port";
	private static final String SLOT = "slot";
	private static final String LOAD = "load";
	private static final String DISCHARGE = "discharge";
	// some field shorthands
	private static EStructuralFeature CONTRACT__RESTRICTED_CONTRACTS = CommercialPackage.Literals.CONTRACT__RESTRICTED_CONTRACTS;
	private static EStructuralFeature CONTRACT__RESTRICTED_PORTS = CommercialPackage.Literals.CONTRACT__RESTRICTED_PORTS;
	private static EStructuralFeature CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE = CommercialPackage.Literals.CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE;
	private static EStructuralFeature SLOT__RESTRICTED_CONTRACTS = CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS;
	private static EStructuralFeature SLOT__RESTRICTED_PORTS = CargoPackage.Literals.SLOT__RESTRICTED_PORTS;
	private static EStructuralFeature SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE = CargoPackage.Literals.SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE;
	private static EStructuralFeature SLOT__CONTRACT = CargoPackage.Literals.SLOT__CONTRACT;
	private static EStructuralFeature SLOT__PORT = CargoPackage.Literals.SLOT__PORT;

	private static String getNiceClassName(EClass clazz) {
		String name = clazz.getName();
		if (name.equals("LoadSlot")) return SLOT;
		if (name.equals("DischargeSlot")) return SLOT;
		if (name.equals("SalesContract")) return CONTRACT;
		if (name.equals("PurchaseContract")) return CONTRACT;
		if (name.equals("Port")) return PORT;
		return name;
	}

	@Override
	protected String validate(final IValidationContext ctx, final List<IStatus> statuses) {

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
					checkSlotAgainstSlotAndContract(ctx, slotI, slotJ, cargo.getName(), statuses);
				}
			}
		}

		return Activator.PLUGIN_ID;
	}
	
	@SuppressWarnings({ "rawtypes" })
	/**
	 * Check a target slot's feature against a permitted list, adding an error to a list of validation statuses if necessary.
	 * 
	 * @param ctx A validation context
	 * @param source The object containing the permitted list and the permission flag
	 * @param listField The field representing the list of permitted objects
	 * @param permissiveField The flag field representing whether the permission list is inclusive or exclusive 
	 * @param target The slot containing the checked feature
	 * @param checkField The feature to check against the permission list
	 * @param cargoName The name of the containing cargo (for the error message)
	 * @param statuses The list of validation statuses to append any problem to
	 */
	private void checkPermitted(final IValidationContext ctx, NamedObject source, EStructuralFeature listField, EStructuralFeature permissiveField, Slot target, EStructuralFeature checkField, String cargoName, List<IStatus> statuses) {
		if (source == null) {
			return;
		}
		
		EList list = (EList) source.eGet(listField);
		Boolean permissive = (Boolean) source.eGet(permissiveField);
		NamedObject checkObject = (NamedObject) target.eGet(checkField);
		
		boolean result = (checkObject == null) || (list.isEmpty()) || (list.contains(checkObject) == permissive);
		
		if (result == false) {
			final String type = (target instanceof LoadSlot ? LOAD : DISCHARGE);		
			
			final String sourceClassName = getNiceClassName(source.eClass());
			final String checkClassName = getNiceClassName(checkObject.eClass());
			final String msg = String.format(RESTRICTION, cargoName, sourceClassName, source.getName(), type, checkClassName, checkObject.getName());
			final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
			d.addEObjectAndFeature(target, checkField);
			statuses.add(d);
			
		}
	}
	
	private void checkSlotAgainstSlotAndContract(final IValidationContext ctx, final Slot slot1, final Slot slot2, String cargoName, final List<IStatus> statuses) {
		// check slot2 against slot1's contract restrictions
		final Contract contract1 = slot1.getContract();
		checkPermitted(ctx, contract1, CONTRACT__RESTRICTED_CONTRACTS, CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE, slot2, SLOT__CONTRACT, cargoName, statuses);
		checkPermitted(ctx, contract1, CONTRACT__RESTRICTED_PORTS, CONTRACT__RESTRICTED_LISTS_ARE_PERMISSIVE, slot2, SLOT__PORT, cargoName, statuses);
		// check slot2 against slot1's direct restrictions
		checkPermitted(ctx, slot1, SLOT__RESTRICTED_CONTRACTS, SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE, slot2, SLOT__CONTRACT, cargoName, statuses);
		checkPermitted(ctx, slot1, SLOT__RESTRICTED_PORTS, SLOT__RESTRICTED_LISTS_ARE_PERMISSIVE, slot2, SLOT__PORT, cargoName, statuses);
		
	}
	
	/*
	private void checkSlot(final IValidationContext ctx, final Slot slot1, final Slot slot2, String cargoName, final List<IStatus> statuses) {
		final Contract contract = slot1.getContract();
		if (contract == null) {
			return;
		}

		final String contractName = contract.getName();
		final String typeA;
//		final String typeB;

		if (slot2 instanceof LoadSlot) {
			typeA = LOAD;
//			typeB = DISCHARGE;
		} else {
//			typeB = LOAD;
			typeA = DISCHARGE;
		}

		final Contract contract2 = slot2.getContract();
		final Port port2 = slot2.getPort();
		final boolean restrictedListsArePermissive = slot1.getSlotOrContractRestrictedListsArePermissive();
		
		final EList<Contract> restrictedContracts = slot1.getSlotOrContractRestrictedContracts();
		final EList<Port> restrictedPorts = slot1.getSlotOrContractRestrictedPorts();		
		
		if (contract2 != null) {
			if (!contract.getRestrictedContracts().isEmpty()) {
				if (restrictedContracts.contains(contract2) != restrictedListsArePermissive) { 
						final String msg = String.format(CONTRACT_RESTRICTION, cargoName, contractName, typeA, contract2.getName());
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot2, CargoPackage.eINSTANCE.getSlot_Contract());
						statuses.add(d);
				}
			}
		}
		if (slot2.getPort() != null) {
			if (!contract.getRestrictedPorts().isEmpty()) {
				boolean contains = contract.getRestrictedPorts().contains(slot2.getPort());
				if ((restrictedListsArePermissive && !contains) // Whitelist
						|| !restrictedListsArePermissive && contains){ // Blacklist
						final String msg = String.format(PORT_RESTRICTION, cargoName, contractName, typeA, slot2.getPort().getName());
						final DetailConstraintStatusDecorator d = new DetailConstraintStatusDecorator((IConstraintStatus) ctx.createFailureStatus(msg));
						d.addEObjectAndFeature(slot2, CargoPackage.eINSTANCE.getSlot_Port());
						statuses.add(d);
				}
			}
		}
	}
	*/
}
