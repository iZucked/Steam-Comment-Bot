/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV65a extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 64;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 65;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}
		final EObjectWrapper spotMarketsModel = model.getRef("spotMarketsModel");
		if (spotMarketsModel == null) {
			return;
		}

		final EPackage pkg_CommercialPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		final EClass class_ExpressionPriceParas = MetamodelUtils.getEClass(pkg_CommercialPackage, "ExpressionPriceParameters");
		final EClass class_DateShiftExpressionPriceParas = MetamodelUtils.getEClass(pkg_CommercialPackage, "DateShiftExpressionPriceParameters");

		final List<String> marketGroups = Lists.newArrayList("desPurchaseSpotMarket", "desSalesSpotMarket", "fobPurchasesSpotMarket", "fobSalesSpotMarket");
		for (final String marketGroup : marketGroups) {

			final EObjectWrapper mg = spotMarketsModel.getRef(marketGroup);
			if (mg != null) {
				final List<EObjectWrapper> markets = mg.getRefAsList("markets");
				if (markets != null) {
					for (final EObjectWrapper spotMarket : markets) {
						final EObjectWrapper priceInfo = spotMarket.getRef("priceInfo");
						if (class_ExpressionPriceParas.isInstance(priceInfo)) {
							final EObjectWrapper newInfo = (EObjectWrapper) pkg_CommercialPackage.getEFactoryInstance().create(class_DateShiftExpressionPriceParas);
							newInfo.setAttrib("priceExpression", priceInfo.getAttrib("priceExpression"));
							spotMarket.setRef("priceInfo", newInfo);

						}
					}
				}
			}
		}
	}
}
