/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 s * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for LoadSlot instances
 * 
 * @generated NOT
 */
public class LoadSlotComponentHelper extends DefaultComponentHelper {

	public LoadSlotComponentHelper() {
		super(CargoPackage.Literals.LOAD_SLOT);

		ignoreFeatures.add(CargoPackage.Literals.SLOT__CARGO);
		ignoreFeatures.add(CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE);
		ignoreFeatures.add(CargoPackage.Literals.LOAD_SLOT__TRANSFER_FROM);

		addEditor(CargoPackage.Literals.LOAD_SLOT__SCHEDULE_PURGE, topClass -> {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_PURGE)) {
				// Hide for spot loads
				if (!CargoPackage.Literals.SPOT_LOAD_SLOT.equals(topClass)) {
					return ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__SCHEDULE_PURGE);
				}
			}
			return null;
		});

		addEditor(CargoPackage.Literals.LOAD_SLOT__VOLUME_COUNTER_PARTY, topClass -> {
			if (!CargoPackage.Literals.SPOT_LOAD_SLOT.equals(topClass)) {
				final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CargoPackage.Literals.LOAD_SLOT__VOLUME_COUNTER_PARTY);
				return new CounterPartyVolumeTypeInlineEditorWrapper(editor);
			}
			return null;
		});

		addDefaultEditorWithWrapper(CargoPackage.Literals.LOAD_SLOT__DES_PURCHASE_DEAL_TYPE, NonShppedDealTypeInlineEditorWrapper::new);
	}

	@Override
	public List<EObject> getExternalEditingRange(final MMXRootObject root, final EObject value) {

		// This is required for the getCargo() feature on the slot
		final List<EObject> external = new LinkedList<>();
		if (value instanceof LoadSlot loadSlot) {
			final Cargo cargo = loadSlot.getCargo();
			if (cargo != null) {
				external.add(cargo);
				external.addAll(cargo.getSlots());
				external.remove(value);
			}
			if (loadSlot.getTransferFrom() != null) {
				external.add(loadSlot.getTransferFrom());
			}
			external.addAll(loadSlot.getExtensions());
		}
		external.addAll(super.getExternalEditingRange(root, value));

		return external;
	}
}