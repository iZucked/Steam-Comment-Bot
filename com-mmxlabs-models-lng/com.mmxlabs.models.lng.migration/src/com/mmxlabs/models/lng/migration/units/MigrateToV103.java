/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV103 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 102;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 103;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EObjectWrapper referenceModel = modelRoot.getRef("referenceModel");

		// Clear Port mmxid and generate a dummy location mmxid if missing
		{
			final EObjectWrapper portModel = referenceModel.getRef("portModel");
			final List<EObjectWrapper> ports = portModel.getRefAsList("ports");
			if (ports != null) {
				for (final EObjectWrapper port : ports) {
					port.unsetFeature("mmxId");
					final EObjectWrapper location = port.getRef("location");
					if (location != null) {
						final String mmxId = location.getAttrib("mmxId");
						if (mmxId == null || mmxId.isEmpty()) {
							final String name = location.getAttrib("name");
							final String newId = "L_" + name.replaceAll(" ", "_");
							location.setAttrib("mmxId", newId);
						}
					}
				}
			}
		}

		// Pricing model migration.
		EPackage package_PricingPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		EClass class_DerivedIndex = MetamodelUtils.getEClass(package_PricingPackage, "DerivedIndex");
		EClass class_CommodityCurve = MetamodelUtils.getEClass(package_PricingPackage, "CommodityCurve");
		EClass class_CharterCurve = MetamodelUtils.getEClass(package_PricingPackage, "CharterCurve");
		EClass class_CurrencyCurve = MetamodelUtils.getEClass(package_PricingPackage, "CurrencyCurve");
		EClass class_BunkerFuelCurve = MetamodelUtils.getEClass(package_PricingPackage, "BunkerFuelCurve");

		EClass class_YearMonthPoint = MetamodelUtils.getEClass(package_PricingPackage, "YearMonthPoint");

		EObjectWrapper pricingModel = referenceModel.getRef("pricingModel");

		BiFunction<List<EObjectWrapper>, Supplier<EObjectWrapper>, List<EObjectWrapper>> convert = (indices, factory) -> {
			List<EObjectWrapper> curves = new LinkedList<>();
			for (EObjectWrapper index : indices) {
				EObjectWrapper curve = factory.get();
				curve.setAttrib("name", index.getAttrib("name"));
				curve.setAttrib("currencyUnit", index.getAttrib("currencyUnit"));
				curve.setAttrib("volumeUnit", index.getAttrib("volumeUnit"));
				EObjectWrapper data = index.getRef("data");
				if (class_DerivedIndex.isInstance(data)) {
					curve.setAttrib("expression", data.getAttrib("expression"));
				} else {
					List<EObjectWrapper> oldPoints = data.getRefAsList("points");
					if (oldPoints != null) {
						List<EObjectWrapper> newPoints = new LinkedList<>();
						for (EObjectWrapper oldPt : oldPoints) {
							EObjectWrapper newPt = (EObjectWrapper) package_PricingPackage.getEFactoryInstance().create(class_YearMonthPoint);
							newPt.setAttrib("date", oldPt.getAttrib("date"));
							newPt.setAttrib("value", ((Number) oldPt.getAttrib("value")).doubleValue());
							newPoints.add(newPt);
						}

						curve.setRef("points", newPoints);
					}
				}
				curves.add(curve);
			}
			return curves;
		};

		List<EObjectWrapper> baseFuelPrices = pricingModel.getRefAsList("baseFuelPrices");
		if (baseFuelPrices != null) {
			List<EObjectWrapper> bunkerFuelCurves = convert.apply(baseFuelPrices, () -> (EObjectWrapper) package_PricingPackage.getEFactoryInstance().create(class_BunkerFuelCurve));
			pricingModel.setRef("bunkerFuelCurves", bunkerFuelCurves);
			pricingModel.unsetFeature("baseFuelPrices");
		}
		List<EObjectWrapper> charterIndicies = pricingModel.getRefAsList("charterIndices");
		if (charterIndicies != null) {
			List<EObjectWrapper> charterCurves = convert.apply(charterIndicies, () -> (EObjectWrapper) package_PricingPackage.getEFactoryInstance().create(class_CharterCurve));
			pricingModel.setRef("charterCurves", charterCurves);
			pricingModel.unsetFeature("charterIndices");
		}
		List<EObjectWrapper> currencyIndicies = pricingModel.getRefAsList("currencyIndices");
		if (currencyIndicies != null) {
			List<EObjectWrapper> currencyCurves = convert.apply(currencyIndicies, () -> (EObjectWrapper) package_PricingPackage.getEFactoryInstance().create(class_CurrencyCurve));
			pricingModel.setRef("currencyCurves", currencyCurves);
			pricingModel.unsetFeature("currencyIndices");
		}
		List<EObjectWrapper> commodityIndicies = pricingModel.getRefAsList("commodityIndices");
		if (commodityIndicies != null) {
			List<EObjectWrapper> commodityCurves = convert.apply(commodityIndicies, () -> (EObjectWrapper) package_PricingPackage.getEFactoryInstance().create(class_CommodityCurve));
			pricingModel.setRef("commodityCurves", commodityCurves);
			pricingModel.unsetFeature("commodityIndices");
		}
	}
}
