/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * A command provider which supplements commands executed on ship-to-ship slots to force synchronisation of load and discharge slots bound together in a ship-to-ship transfer. Because these slots
 * represent a single physical event, they need to be matched on volume, date, duration and (notional) port.
 * 
 * @author Simon McGregor
 * 
 */
public class SlotShipToShipBindingCommandProvider extends SynchronisedFeatureCommandProvider {
	@Override
	protected Set<EStructuralFeature> makeBoundFeatures() {
		// construct the set of structural features which are to be synchronised
		// Keep in sync with the new ship to ship context menu in CargoEditingCommands
		HashSet<EStructuralFeature> result = new HashSet<EStructuralFeature>();
		result.add(CargoPackage.Literals.SLOT__DURATION);
		result.add(CargoPackage.Literals.SLOT__WINDOW_START);
		result.add(CargoPackage.Literals.SLOT__WINDOW_SIZE);
		result.add(CargoPackage.Literals.SLOT__WINDOW_START_TIME);
		result.add(CargoPackage.Literals.SLOT__PRICE_EXPRESSION);
		result.add(CargoPackage.Literals.SLOT__PORT);
		result.add(CargoPackage.Literals.SLOT__MIN_QUANTITY);
		result.add(CargoPackage.Literals.SLOT__MAX_QUANTITY);
		return result;
	}

	@Override
	protected EObject getSynchronisedObject(final MMXRootObject root, final EObject owner) {
		if (owner instanceof LoadSlot) {
			return ((LoadSlot) owner).getTransferFrom();
		}

		if (owner instanceof DischargeSlot) {
			return ((DischargeSlot) owner).getTransferTo();
		}

		// no paired object exists
		return null;
	}

}
