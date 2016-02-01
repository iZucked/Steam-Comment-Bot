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

public class MigrateToV40 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 39;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 40;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		EPackage scenarioPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);

		EClass class_LNGReferenceModel = MetamodelUtils.getEClass(scenarioPackage, "LNGReferenceModel");

		// Move "reference" data into ref model
		EObjectWrapper referenceModel = (EObjectWrapper) scenarioPackage.getEFactoryInstance().create(class_LNGReferenceModel);
		referenceModel.setRef("portModel", model.getRef("portModel"));
		referenceModel.setRef("fleetModel", model.getRef("fleetModel"));
		referenceModel.setRef("pricingModel", model.getRef("pricingModel"));
		referenceModel.setRef("commercialModel", model.getRef("commercialModel"));
		referenceModel.setRef("spotMarketsModel", model.getRef("spotMarketsModel"));
		referenceModel.setRef("analyticsModel", model.getRef("analyticsModel"));
		referenceModel.setRef("costModel", model.getRef("costModel"));

		// Clear references
		model.unsetFeature("portModel");
		model.unsetFeature("fleetModel");
		model.unsetFeature("pricingModel");
		model.unsetFeature("commercialModel");
		model.unsetFeature("spotMarketsModel");
		model.unsetFeature("analyticsModel");
		model.unsetFeature("costModel");

		model.setRef("referenceModel", referenceModel);

		// Move "Portfolio" data into scenario model
		EObjectWrapper portfolioModel = model.getRef("portfolioModel");

		model.setRef("cargoModel", portfolioModel.getRef("cargoModel"));
		model.setRef("scheduleModel", portfolioModel.getRef("scheduleModel"));
		model.setRef("parameters", portfolioModel.getRef("parameters"));
		model.setRef("actualsModel", portfolioModel.getRef("actualsModel"));
		if (portfolioModel.isSetFeature("promptPeriodStart")) {
			model.setAttrib("promptPeriodStart", portfolioModel.getAttrib("promptPeriodStart"));
		}
		if (portfolioModel.isSetFeature("promptPeriodEnd")) {
			model.setAttrib("promptPeriodEnd", portfolioModel.getAttrib("promptPeriodEnd"));
		}
		model.unsetFeature("portfolioModel");
	}

}
