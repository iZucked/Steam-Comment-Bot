/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV52 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 51;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 52;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}

		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");
		if (spotMarketsModel == null) {
			return;
		}
		final List<EObjectWrapper> charterInMarkets = spotMarketsModel.getRefAsList("charterInMarkets");
		if (charterInMarkets != null) {
			charterInMarkets.forEach(market -> {
				EObjectWrapper index = market.getRef("charterInPrice");
				if (index != null) {
					market.setAttrib("charterInRate", index.getAttrib("name"));
					market.unsetFeature("charterInPrice");
				}
			});
		}

		final List<EObjectWrapper> charterOutMarkets = spotMarketsModel.getRefAsList("charterOutMarkets");
		if (charterOutMarkets != null) {
			charterOutMarkets.forEach(market -> {
				EObjectWrapper index = market.getRef("charterOutPrice");
				if (index != null) {
					market.setAttrib("charterOutRate", index.getAttrib("name"));
					market.unsetFeature("charterOutPrice");
				}
			});
		}

	}
}
