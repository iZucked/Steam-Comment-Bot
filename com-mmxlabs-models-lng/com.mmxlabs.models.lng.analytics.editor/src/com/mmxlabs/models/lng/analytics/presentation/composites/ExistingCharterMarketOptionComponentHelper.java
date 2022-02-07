/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for ExistingCharterMarketOption instances
 *
 * @generated NOT
 */
public class ExistingCharterMarketOptionComponentHelper extends DefaultComponentHelper {

	public ExistingCharterMarketOptionComponentHelper() {
		super(AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION);

		addEditor(AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX,
				topClass -> new ExistingMarketSpotIndexInlineEditor(AnalyticsPackage.Literals.EXISTING_CHARTER_MARKET_OPTION__SPOT_INDEX));
	}
}