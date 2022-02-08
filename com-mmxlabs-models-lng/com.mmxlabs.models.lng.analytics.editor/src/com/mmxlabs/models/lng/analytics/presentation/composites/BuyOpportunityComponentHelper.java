/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.displaycomposites.VolumeModeEditorWrapper;
import com.mmxlabs.models.lng.analytics.displaycomposites.WindowModeEditorWrapper;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for BuyOpportunity instances
 *
 * @generated NOT
 */
public class BuyOpportunityComponentHelper extends DefaultComponentHelper {

	public BuyOpportunityComponentHelper() {
		super(AnalyticsPackage.Literals.BUY_OPPORTUNITY);

		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.BUY_OPPORTUNITY__VOLUME_UNITS, VolumeModeEditorWrapper::new);
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.BUY_OPPORTUNITY__MIN_VOLUME, VolumeModeEditorWrapper::new);
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.BUY_OPPORTUNITY__MAX_VOLUME, VolumeModeEditorWrapper::new);
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.BUY_OPPORTUNITY__SPECIFY_WINDOW, WindowModeEditorWrapper::new);
	}
}