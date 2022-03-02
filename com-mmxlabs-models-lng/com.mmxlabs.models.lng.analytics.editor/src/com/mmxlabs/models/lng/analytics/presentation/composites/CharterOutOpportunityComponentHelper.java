/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.presentation.composites;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.port.ui.editorpart.TextualPortReferenceInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for CharterOutOpportunity instances
 *
 * @generated NOT
 */
public class CharterOutOpportunityComponentHelper extends DefaultComponentHelper {

	public CharterOutOpportunityComponentHelper() {
		super(AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY);

		addEditor(AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__PORT, topClass -> new TextualPortReferenceInlineEditor(AnalyticsPackage.Literals.CHARTER_OUT_OPPORTUNITY__PORT));
	}

}