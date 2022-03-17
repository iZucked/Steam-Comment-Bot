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
 * A component helper for DischargeSlot instances
 * 
 * @generated NOT
 */
public class DischargeSlotComponentHelper extends DefaultComponentHelper {

	public DischargeSlotComponentHelper() {
		super(CargoPackage.Literals.DISCHARGE_SLOT);

		ignoreFeatures.add(CargoPackage.Literals.SLOT__CARGO);
		ignoreFeatures.add(CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE);
		ignoreFeatures.add(CargoPackage.Literals.DISCHARGE_SLOT__TRANSFER_TO);

		addDefaultEditorWithWrapper(CargoPackage.Literals.DISCHARGE_SLOT__FOB_SALE_DEAL_TYPE, NonShppedDealTypeInlineEditorWrapper::new);
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
			if (dischargeSlot.getTransferTo() != null) {
				external.add(dischargeSlot.getTransferTo());
			}
			external.addAll(dischargeSlot.getExtensions());
		}
		external.addAll(super.getExternalEditingRange(root, value));

		return external;
	}
}