/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV170 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 169;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 170;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EPackage pricingPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EClass cooldownPriceTmpClass = MetamodelUtils.getEClass(pricingPackage, "CooldownPriceTmp");
		final EFactory pricingFactory = pricingPackage.getEFactoryInstance();

		EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
		EObjectWrapper costModel = referenceModel.getRef("costModel");

		List<EObjectWrapper> cooldownPrices = costModel.getRefAsList("cooldownCosts");
		if (cooldownPrices != null) {
			List<EObjectWrapper> newPrices = new LinkedList<>();
			for (var oldPrice : cooldownPrices) {
				EObjectWrapper newPrice = (EObjectWrapper) pricingFactory.create(cooldownPriceTmpClass);

				if (oldPrice.getAttribAsBoolean("lumpsum")) {
					newPrice.setAttrib("lumpsumExpression", oldPrice.getAttrib("expression"));
				} else {
					newPrice.setAttrib("volumeExpression", oldPrice.getAttrib("expression"));
				}
				newPrice.setRef("ports", oldPrice.getRefAsList("ports"));
				
				newPrices.add(newPrice);
			}
			
			costModel.unsetFeature("cooldownCosts");
			costModel.setRef("cooldownCostsTmp", newPrices);
		}
	}
}