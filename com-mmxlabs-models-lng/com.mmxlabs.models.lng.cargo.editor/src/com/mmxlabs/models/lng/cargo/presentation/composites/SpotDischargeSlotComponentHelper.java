/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for SpotDischargeSlot instances
 *
 * @generated NOT
 */
public class SpotDischargeSlotComponentHelper extends DefaultComponentHelper {

	public SpotDischargeSlotComponentHelper() {
		super(CargoPackage.Literals.SPOT_DISCHARGE_SLOT);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {

		// This is required for the getCargo() feature on the slot
		final List<EObject> external = new LinkedList<>();
		if (value instanceof DischargeSlot dischargeSlot) {
			final Cargo cargo = dischargeSlot.getCargo();
			if (cargo != null) {
				external.add(cargo);
				external.addAll(cargo.getSlots());
				external.remove(value);
			}
		}
		external.addAll(super.getExternalEditingRange(root, value));

		return external;
	}
}