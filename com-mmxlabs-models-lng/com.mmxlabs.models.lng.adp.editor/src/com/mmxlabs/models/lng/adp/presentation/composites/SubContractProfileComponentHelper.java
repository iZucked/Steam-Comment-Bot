/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.presentation.editors.ShippingDaysRestrictionInlineEditorChangedListener;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for SubContractProfile instances
 *
 * @generated NOT
 */
public class SubContractProfileComponentHelper extends DefaultComponentHelper {

	public SubContractProfileComponentHelper() {
		super(ADPPackage.Literals.SUB_CONTRACT_PROFILE);
		ignoreFeatures.add(ADPPackage.Literals.SUB_CONTRACT_PROFILE__NAME);
		ignoreFeatures.add(ADPPackage.Literals.SUB_CONTRACT_PROFILE__SLOT_TEMPLATE_ID);
		ignoreFeatures.add(ADPPackage.Literals.SUB_CONTRACT_PROFILE__SHIPPING_DAYS);

		addEditor(ADPPackage.Literals.SUB_CONTRACT_PROFILE__PORT, topClass -> new TextualPortReferenceInlineEditor(ADPPackage.Literals.SUB_CONTRACT_PROFILE__PORT));

		addEditor(ADPPackage.Literals.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL, topClass -> {
			IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.SUB_CONTRACT_PROFILE__NOMINATED_VESSEL);
			editor.addNotificationChangedListener(new ShippingDaysRestrictionInlineEditorChangedListener());
			return editor;
		});
	}
}