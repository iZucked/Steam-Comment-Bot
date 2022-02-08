/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for NominatedShippingOption instances
 *
 * @generated NOT
 */
public class NominatedShippingOptionComponentHelper extends DefaultComponentHelper {

	public NominatedShippingOptionComponentHelper() {
		super(AnalyticsPackage.Literals.NOMINATED_SHIPPING_OPTION);

		addEditor(AnalyticsPackage.Literals.NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL,
				topClass -> new TextualVesselReferenceInlineEditor(AnalyticsPackage.Literals.NOMINATED_SHIPPING_OPTION__NOMINATED_VESSEL));

	}
}