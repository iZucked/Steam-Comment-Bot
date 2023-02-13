/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.displaycomposites.VolumeModeEditorWrapper;
import com.mmxlabs.models.lng.analytics.displaycomposites.WindowModeEditorWrapper;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for SellOpportunity instances
 *
 * @generated NOT
 */
public class SellOpportunityComponentHelper extends DefaultComponentHelper {

	public SellOpportunityComponentHelper() {
		super(AnalyticsPackage.Literals.SELL_OPPORTUNITY);

		addEditor(AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT, topClass -> new TextualPortReferenceInlineEditor(AnalyticsPackage.Literals.SELL_OPPORTUNITY__PORT));
		
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.SELL_OPPORTUNITY__VOLUME_UNITS, VolumeModeEditorWrapper::new);
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.SELL_OPPORTUNITY__MIN_VOLUME, VolumeModeEditorWrapper::new);
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.SELL_OPPORTUNITY__MAX_VOLUME, VolumeModeEditorWrapper::new);
		addDefaultEditorWithWrapper(AnalyticsPackage.Literals.SELL_OPPORTUNITY__SPECIFY_WINDOW, WindowModeEditorWrapper::new);
	}
}