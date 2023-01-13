/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.presentation.composites;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.ui.ComponentHelperUtils;
import com.mmxlabs.models.ui.editors.impl.PermissiveRestrictiveInlineEditor;
import com.mmxlabs.models.ui.impl.DefaultComponentHelper;

/**
 * A component helper for SpotMarket instances
 *
 * @generated NOT
 */
public class SpotMarketComponentHelper extends DefaultComponentHelper {

	public SpotMarketComponentHelper() {
		super(SpotMarketsPackage.Literals.SPOT_MARKET);

		addEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE,
				topClass -> new PermissiveRestrictiveInlineEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE));

		addEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS, topClass -> {
			if (topClass == SpotMarketsPackage.Literals.FOB_PURCHASES_MARKET || topClass == SpotMarketsPackage.Literals.DES_SALES_MARKET) {
				return ComponentHelperUtils.createDefaultEditor(topClass, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS);
			}
			return null;
		});

		addEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE, topClass -> {
			if (topClass == SpotMarketsPackage.Literals.FOB_PURCHASES_MARKET || topClass == SpotMarketsPackage.Literals.DES_SALES_MARKET) {
				return new PermissiveRestrictiveInlineEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE);
			}
			return null;
		});

		addEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE,
				topClass -> new PermissiveRestrictiveInlineEditor(SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE));

		addDefaultEditorForLicenseFeature(KnownFeatures.FEATURE_MTM, SpotMarketsPackage.Literals.SPOT_MARKET__MTM);
	}
}