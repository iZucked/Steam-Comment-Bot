/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.IInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for ContractProfile instances
 *
 * @generated NOT
 */
public class ContractProfileComponentHelper extends DefaultComponentHelper {

	public ContractProfileComponentHelper() {
		super(ADPPackage.Literals.CONTRACT_PROFILE);

		ignoreFeatures.add(ADPPackage.Literals.CONTRACT_PROFILE__CUSTOM);

		addEditor(ADPPackage.Literals.CONTRACT_PROFILE__CONTRACT, topClass -> {
			IInlineEditor editor = ComponentHelperUtils.createDefaultEditor(topClass, ADPPackage.Literals.CONTRACT_PROFILE__CONTRACT);
			editor.setEditorLocked(true);
			editor.setEditorEnabled(false);
			return editor;
		});
	}

}