/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV59 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 58;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 59;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		if (referenceModel == null) {
			return;
		}

		final EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");
		if (pricingModel == null) {
			return;
		}

		processUnits(pricingModel.getRefAsList("commodityIndices"));
		processUnits(pricingModel.getRefAsList("baseFuelPrices"));
		processUnits(pricingModel.getRefAsList("charterIndices"));

		createConversionFactor("MwH", "mmBtu", 3.409511, pricingModel, loader);
		createConversionFactor("bbl", "mmBtu", 0.180136, pricingModel, loader);
		createConversionFactor("therm", "mmBtu", 0.1, pricingModel, loader);

	}

	private void createConversionFactor(final String from, final String to, final double factor, final EObjectWrapper pricingModel, final MetamodelLoader loader) {
		final EPackage pkg_PricingPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);

		final EClass class_UnitConversion = MetamodelUtils.getEClass(pkg_PricingPackage, "UnitConversion");

		final EObjectWrapper uc = (EObjectWrapper) pkg_PricingPackage.getEFactoryInstance().create(class_UnitConversion);
		uc.setAttrib("from", from);
		uc.setAttrib("to", to);
		uc.setAttrib("factor", factor);

		pricingModel.getRefAsList("conversionFactors").add(uc);
	}

	private void processUnits(@Nullable final List<EObjectWrapper> indicies) {

		if (indicies != null) {
			for (final EObjectWrapper index : indicies) {
				if (index != null) {
					final String units = index.getAttrib("units");
					if (units != null && units.length() > 0) {
						final int idx = units.indexOf('/');
						if (idx >= 0) {
							final String currency = units.substring(0, idx);
							final String volume = units.substring(idx + 1);
							index.setAttrib("currencyUnit", currency);
							index.setAttrib("volumeUnit", volume);
						} else {
							index.setAttrib("currencyUnit", units);
						}
					}
					if (units != null) {
						index.unsetFeature("units");
					}
				}
			}
		}
	}
}
