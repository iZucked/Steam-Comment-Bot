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

public class MigrateToV61 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 60;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 61;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		// Update for new sequence hint definition. 0 is unset and round trip cargoes have no hint.
		final EObjectWrapper cargoModel = model.getRef("cargoModel");
		final List<EObjectWrapper> cargoes = cargoModel.getRefAsList("cargoes");
		if (cargoes != null) {
			cargoes.forEach(c -> {
				int spotIndex = (Integer) c.getAttrib("spotIndex");
				if (spotIndex == -1) {
					c.setAttrib("sequenceHint", 0);
				} else {
					int currentHint = (Integer) c.getAttrib("sequenceHint");
					c.setAttrib("sequenceHint", 1 + currentHint);
				}
			});
		}
	}
}
