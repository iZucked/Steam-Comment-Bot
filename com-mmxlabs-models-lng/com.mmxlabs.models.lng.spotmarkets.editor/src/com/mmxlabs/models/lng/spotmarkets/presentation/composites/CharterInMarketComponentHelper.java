/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.impl.PlaceholderInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for CharterInMarket instances
 *
 * @generated NOT
 */
public class CharterInMarketComponentHelper extends DefaultComponentHelper {
	public CharterInMarketComponentHelper() {
		super(SpotMarketsPackage.Literals.CHARTER_IN_MARKET);

		ignoreFeatures.add(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__OVERRIDE_INACCESSIBLE_ROUTES);
		ignoreFeatures.add(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__INACCESSIBLE_ROUTES);

		addEditor(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM, topClass -> {
			if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_MTM)) {
				return ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM);
			} else {
				return new PlaceholderInlineEditor(SpotMarketsPackage.Literals.CHARTER_IN_MARKET__MTM);
			}
		});
	}
}