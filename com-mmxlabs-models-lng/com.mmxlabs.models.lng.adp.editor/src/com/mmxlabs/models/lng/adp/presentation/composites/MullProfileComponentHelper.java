/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ui.inlineeditors.MullCargoWrapperInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for MullProfile instances
 *
 * @generated NOT
 */
public class MullProfileComponentHelper extends DefaultComponentHelper {
	public MullProfileComponentHelper() {
		super(ADPPackage.Literals.MULL_PROFILE);
		
		addEditor(ADPPackage.Literals.MULL_PROFILE__CARGOES_TO_KEEP, topClass -> new MullCargoWrapperInlineEditor(ADPPackage.Literals.MULL_PROFILE__CARGOES_TO_KEEP));
	}

}