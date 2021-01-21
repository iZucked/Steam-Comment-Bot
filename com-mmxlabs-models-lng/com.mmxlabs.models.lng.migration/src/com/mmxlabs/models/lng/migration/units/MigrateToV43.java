/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV43 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 42;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 43;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();

		// Ensure all actuals items have a ReturnActuals instance.

		final EPackage package_actualsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ActualsModel);

		final EClass class_ReturnActuals = MetamodelUtils.getEClass(package_actualsModel, "ReturnActuals");

		final EObjectWrapper actualsModel = model.getRef("actualsModel");
		if (actualsModel != null) {

			final List<EObjectWrapper> cargoActualsList = actualsModel.getRefAsList("cargoActuals");
			if (cargoActualsList != null) {
				for (final EObjectWrapper cargoActuals : cargoActualsList) {
					if (cargoActuals.getRef("returnActuals") == null) {
						cargoActuals.setRef("returnActuals", package_actualsModel.getEFactoryInstance().create(class_ReturnActuals));
					}
				}
			}
		}

	}
}
