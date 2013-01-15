/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.internal;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV1 extends AbstractMigrationUnit {

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 0;
	}

	@Override
	public int getDestinationVersion() {
		return 1;
	}

	@Override
	protected MetamodelLoader createSourceMetamodelLoader() {
		return MetamodelVersionsUtil.getV0Loader();
	}

	@Override
	protected MetamodelLoader createDestinationMetamodelLoader() {
		return MetamodelVersionsUtil.getV1Loader();
	}

	@Override
	protected void doMigration(Map<ModelsLNGSet_v1, EObject> models) {
		// TODO: Migrate!

	}
}
