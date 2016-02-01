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

public class MigrateToV39 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 38;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 39;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		EPackage pricingPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);

		EObjectWrapper pricingModel = model.getRef("pricingModel");

		EClass class_CostModel = MetamodelUtils.getEClass(pricingPackage, "CostModel");
		EObjectWrapper costModel = (EObjectWrapper)pricingPackage.getEFactoryInstance().create(class_CostModel);

		costModel.setRef("routeCosts", pricingModel.getRefAsList("routeCosts"));
		costModel.setRef("portCosts", pricingModel.getRefAsList("portCosts"));
		costModel.setRef("cooldownCosts", pricingModel.getRefAsList("cooldownPrices"));
		costModel.setRef("baseFuelCosts", pricingModel.getRef("fleetCost").getRefAsList("baseFuelPrices"));

		// Clear references
		pricingModel.unsetFeature("routeCosts");
		pricingModel.unsetFeature("portCosts");
		pricingModel.unsetFeature("cooldownPrices");
		pricingModel.unsetFeature("fleetCost");
		
		model.setRef("costModel", costModel);
	}

}
