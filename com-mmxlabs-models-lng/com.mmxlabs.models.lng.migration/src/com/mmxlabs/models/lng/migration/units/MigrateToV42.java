/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV42 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 41;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 42;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		EPackage package_parametersModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ParametersModel);

		EClass class_UserSettings = MetamodelUtils.getEClass(package_parametersModel, "UserSettings");

		EObjectWrapper userSettings = (EObjectWrapper) package_parametersModel.getEFactoryInstance().create(class_UserSettings);

		EObjectWrapper optimiserSettings = model.getRef("parameters");
		if (optimiserSettings != null) {
			EObjectWrapper range = optimiserSettings.getRef("range");
			if (range != null) {
				if (range.isSetFeature("optimiseAfter")) {
					userSettings.setAttrib("periodStart", range.getAttrib("optimiseAfter"));
				}
				if (range.isSetFeature("optimiseBefore")) {
					userSettings.setAttrib("periodEnd", range.getAttrib("optimiseBefore"));
				}
			}

			userSettings.setAttrib("buildActionSets", optimiserSettings.getAttrib("buildActionSets"));
			userSettings.setAttrib("generateCharterOuts", optimiserSettings.getAttrib("generateCharterOuts"));
			userSettings.setAttrib("shippingOnly", optimiserSettings.getAttrib("shippingOnly"));
		}

		model.setRef("userSettings", userSettings);

	}

}
