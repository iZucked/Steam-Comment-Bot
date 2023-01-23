/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.presentation.composites;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.presentation.editors.ADPSpotIndexInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for ShippingOption instances
 *
 * @generated NOT
 */
public class ShippingOptionComponentHelper extends DefaultComponentHelper {

	public ShippingOptionComponentHelper() {
		super(ADPPackage.Literals.SHIPPING_OPTION);

		addEditor(ADPPackage.Literals.SHIPPING_OPTION__SPOT_INDEX, topClass -> new ADPSpotIndexInlineEditor(ADPPackage.Literals.SHIPPING_OPTION__SPOT_INDEX));
	}
}