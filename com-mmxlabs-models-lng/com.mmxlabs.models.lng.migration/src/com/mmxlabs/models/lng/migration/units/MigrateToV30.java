/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV30 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 29;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 30;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader metamodelLoader, final EObjectWrapper model) {

	}

}
