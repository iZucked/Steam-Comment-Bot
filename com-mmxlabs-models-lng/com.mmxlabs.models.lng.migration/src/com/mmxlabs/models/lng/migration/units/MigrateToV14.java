/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV14 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 13;
	}

	@Override
	public int getDestinationVersion() {
		return -14;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV13Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV14Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// Nothing to do - model is forward compatible.
	}

}
