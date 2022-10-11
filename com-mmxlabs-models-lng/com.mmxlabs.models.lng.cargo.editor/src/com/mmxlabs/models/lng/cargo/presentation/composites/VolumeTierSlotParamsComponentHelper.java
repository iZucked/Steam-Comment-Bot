/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import java.util.List;

import com.mmxlabs.models.lng.cargo.editor.SlotContractExtensionWrapper;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.VolumeTierPriceParameters;
import com.mmxlabs.models.lng.commercial.VolumeTierSlotParams;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

public class VolumeTierSlotParamsComponentHelper extends DefaultComponentHelper {

	public VolumeTierSlotParamsComponentHelper() {
		super(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS);

		addEditor(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);
			return new SlotContractExtensionWrapper<VolumeTierPriceParameters, VolumeTierSlotParams>(editor, VolumeTierPriceParameters.class, VolumeTierSlotParams.class);
		});
		addEditor(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__THRESHOLD, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__THRESHOLD);
			final var wrapper = new SlotContractExtensionWrapper<VolumeTierPriceParameters, VolumeTierSlotParams>(editor, VolumeTierPriceParameters.class, VolumeTierSlotParams.class);
			wrapper.setEnabledFeature(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);
			return wrapper;
		});
		addEditor(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__LOW_EXPRESSION);
			final var wrapper = new SlotContractExtensionWrapper<VolumeTierPriceParameters, VolumeTierSlotParams>(editor, VolumeTierPriceParameters.class, VolumeTierSlotParams.class);
			wrapper.setEnabledFeature(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);
			return wrapper;
		});
		addEditor(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__HIGH_EXPRESSION, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__HIGH_EXPRESSION);
			final var wrapper = new SlotContractExtensionWrapper<VolumeTierPriceParameters, VolumeTierSlotParams>(editor, VolumeTierPriceParameters.class, VolumeTierSlotParams.class);
			wrapper.setEnabledFeature(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);
			return wrapper;
		});
		addEditor(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__VOLUME_LIMITS_UNIT, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__VOLUME_LIMITS_UNIT);
			final var wrapper = new SlotContractExtensionWrapper<VolumeTierPriceParameters, VolumeTierSlotParams>(editor, VolumeTierPriceParameters.class, VolumeTierSlotParams.class);
			wrapper.setEnabledFeature(CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);
			return wrapper;
		});
	}

	@Override
	protected void sortEditors(final List<IInlineEditor> editors) {

		// Move the override flag to the head of the list
		final List<IInlineEditor> featureEditors = getEditorsForFeature(editors, CommercialPackage.Literals.VOLUME_TIER_SLOT_PARAMS__OVERRIDE_CONTRACT);
		if (featureEditors.isEmpty()) {
			return;
		}

		editors.removeAll(featureEditors);
		editors.addAll(0, featureEditors);
	}
}