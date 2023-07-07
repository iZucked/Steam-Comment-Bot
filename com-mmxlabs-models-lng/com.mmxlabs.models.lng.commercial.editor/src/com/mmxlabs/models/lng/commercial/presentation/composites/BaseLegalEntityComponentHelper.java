/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.presentation.composites;

import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for BaseLegalEntity instances
 *
 * @generated NOT
 */
public class BaseLegalEntityComponentHelper extends DefaultComponentHelper {

	public BaseLegalEntityComponentHelper() {
		super(CommercialPackage.Literals.BASE_LEGAL_ENTITY);
		ignoreFeatures.add(CommercialPackage.Literals.BASE_LEGAL_ENTITY__BUSINESS_UNITS);
		ignoreFeatures.add(CommercialPackage.Literals.BASE_LEGAL_ENTITY__THIRD_PARTY);
	}
}