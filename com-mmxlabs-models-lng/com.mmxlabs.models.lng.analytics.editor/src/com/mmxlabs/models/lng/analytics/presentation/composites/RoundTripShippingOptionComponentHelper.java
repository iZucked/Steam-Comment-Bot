/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.fleet.ui.inlineeditors.TextualVesselReferenceInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for RoundTripShippingOption instances
 *
 * @generated NOT
 */
public class RoundTripShippingOptionComponentHelper extends DefaultComponentHelper {

	public RoundTripShippingOptionComponentHelper() {
		super(AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION);

		addEditor(AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__VESSEL, topClass -> new TextualVesselReferenceInlineEditor(AnalyticsPackage.Literals.ROUND_TRIP_SHIPPING_OPTION__VESSEL));

	}
}