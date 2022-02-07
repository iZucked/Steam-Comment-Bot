/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.presentation.composites;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for CharterOutEvent instances
 *
 * @generated NOT
 */
public class CharterOutEventComponentHelper extends DefaultComponentHelper {

	public CharterOutEventComponentHelper() {
		super(CargoPackage.Literals.CHARTER_OUT_EVENT);
		ignoreFeatures.add(CargoPackage.Literals.ASSIGNABLE_ELEMENT__SPOT_INDEX);
//		addEditor(CargoPackage.Literals.VESSEL_EVENT__PORT, topClass -> new TextualPortReferenceInlineEditor(CargoPackage.Literals.VESSEL_EVENT__PORT));
	}
}