/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import java.util.List;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.ui.displaycomposites.DivertibleContractInlineEditorChangedListener;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for PurchaseContract instances
 *
 */
public class PurchaseContractComponentHelper extends DefaultComponentHelper {

	public PurchaseContractComponentHelper() {
		super(CommercialPackage.Literals.PURCHASE_CONTRACT);
		if (!LicenseFeatures.isPermitted(KnownFeatures.FEATURE_EMISSIONS)) {
			ignoreFeatures.add(CommercialPackage.Literals.PURCHASE_CONTRACT__PIPELINE_EMISSION_RATE);
			ignoreFeatures.add(CommercialPackage.Literals.PURCHASE_CONTRACT__UPSTREAM_EMISSION_RATE);
		}

		addEditor(CommercialPackage.Literals.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE, topClass -> {
			final IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, CommercialPackage.Literals.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE);
			editor.addNotificationChangedListener(new DivertibleContractInlineEditorChangedListener());
			return editor;
		});
	}

	@Override
	protected void sortEditors(List<IInlineEditor> editors) {
		sortEditorBeforeOtherEditor(editors, CommercialPackage.Literals.CONTRACT__CONTRACT_TYPE, CommercialPackage.Literals.CONTRACT__ENTITY);
		sortEditorBeforeOtherEditor(editors, CommercialPackage.Literals.PURCHASE_CONTRACT__DES_PURCHASE_DEAL_TYPE, CommercialPackage.Literals.CONTRACT__SHIPPING_DAYS_RESTRICTION);

		// Move notes to end
		List<IInlineEditor> editorsForFeature = getEditorsForFeature(editors, CommercialPackage.Literals.CONTRACT__NOTES);
		editors.removeAll(editorsForFeature);
		editors.addAll(editorsForFeature);

	}
}