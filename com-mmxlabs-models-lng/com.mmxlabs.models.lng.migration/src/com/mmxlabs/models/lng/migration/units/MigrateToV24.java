/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV24 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 23;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 24;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV23Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV24Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_MMXCore = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass class_NamedObject = MetamodelUtils.getEClass(package_MMXCore, "NamedObject");
		final EAttribute attribute_NamedObject_name = MetamodelUtils.getAttribute(class_NamedObject, "name");

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");

		final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");

		final EReference reference_SpotMarketsModel_charteringSpotMarkets = MetamodelUtils.getReference(class_SpotMarketsModel, "charteringSpotMarkets");
		final EReference reference_SpotMarketsModel_charterInMarkets = MetamodelUtils.getReference(class_SpotMarketsModel, "charterInMarkets");
		final EReference reference_SpotMarketsModel_charterOutMarkets = MetamodelUtils.getReference(class_SpotMarketsModel, "charterOutMarkets");

		final EClass class_CharterCostModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "CharterCostModel");
		final EClass class_CharterOutMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "CharterOutMarket");
		final EClass class_CharterInMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "CharterInMarket");

		final EAttribute attribute_CharterCostModel_enabled = MetamodelUtils.getAttribute(class_CharterCostModel, "enabled");
		final EAttribute attribute_CharterInMarket_enabled = MetamodelUtils.getAttribute(class_CharterInMarket, "enabled");
		final EAttribute attribute_CharterOutMarket_enabled = MetamodelUtils.getAttribute(class_CharterOutMarket, "enabled");

		final EReference reference_CharterCostModel_vesselClasses = MetamodelUtils.getReference(class_CharterCostModel, "vesselClasses");
		final EReference reference_CharterInMarket_vesselClass = MetamodelUtils.getReference(class_CharterInMarket, "vesselClass");
		final EReference reference_CharterOutMarket_vesselClass = MetamodelUtils.getReference(class_CharterOutMarket, "vesselClass");

		final EReference reference_CharterCostModel_charterInPrice = MetamodelUtils.getReference(class_CharterCostModel, "charterInPrice");
		final EReference reference_CharterCostModel_charterOutPrice = MetamodelUtils.getReference(class_CharterCostModel, "charterOutPrice");
		final EAttribute attribute_CharterCostModel_spotCharterCount = MetamodelUtils.getAttribute(class_CharterCostModel, "spotCharterCount");
		final EAttribute attribute_CharterCostModel_minCharterOutDuration = MetamodelUtils.getAttribute(class_CharterCostModel, "minCharterOutDuration");

		final EReference reference_CharterInMarket_charterInPrice = MetamodelUtils.getReference(class_CharterInMarket, "charterInPrice");
		final EAttribute attribute_CharterInMarket_spotCharterCount = MetamodelUtils.getAttribute(class_CharterInMarket, "spotCharterCount");

		final EReference reference_CharterOutMarket_charterOutPrice = MetamodelUtils.getReference(class_CharterOutMarket, "charterOutPrice");
		final EAttribute attribute_CharterOutMarket_minCharterOutDuration = MetamodelUtils.getAttribute(class_CharterOutMarket, "minCharterOutDuration");

		final List<EObject> charterInMarkets = new LinkedList<>();
		final List<EObject> charterOutMarkets = new LinkedList<>();

		final EObject spotMarketModel = (EObject) model.eGet(reference_LNGScenarioModel_spotMarketsModel);
		if (spotMarketModel == null) {
			return;
		}

		final List<EObject> charterCostModels = MetamodelUtils.getValueAsTypedList(spotMarketModel, reference_SpotMarketsModel_charteringSpotMarkets);

		if (charterCostModels != null) {
			for (final EObject charterCostModel : charterCostModels) {
				final List<EObject> vesselClasses = MetamodelUtils.getValueAsTypedList(charterCostModel, reference_CharterCostModel_vesselClasses);
				if (vesselClasses != null) {
					for (final EObject vesselClass : vesselClasses) {
						if (charterCostModel.eIsSet(reference_CharterCostModel_charterInPrice)) {
							final EObject charterInMarket = package_SpotMarketsModel.getEFactoryInstance().create(class_CharterInMarket);

							charterInMarket.eSet(attribute_CharterInMarket_enabled, charterCostModel.eGet(attribute_CharterCostModel_enabled));
							charterInMarket.eSet(reference_CharterInMarket_vesselClass, vesselClass);
							final EObject curve = (EObject) charterCostModel.eGet(reference_CharterCostModel_charterInPrice);
							charterInMarket.eSet(reference_CharterInMarket_charterInPrice, curve);
							charterInMarket.eSet(attribute_CharterInMarket_spotCharterCount, charterCostModel.eGet(attribute_CharterCostModel_spotCharterCount));

							final String name = String.format("%s - %s", vesselClass.eGet(attribute_NamedObject_name), curve.eGet(attribute_NamedObject_name));
							charterInMarket.eSet(attribute_NamedObject_name, name);

							charterInMarkets.add(charterInMarket);
						}
						if (charterCostModel.eIsSet(reference_CharterCostModel_charterOutPrice)) {
							final EObject charterOutMarket = package_SpotMarketsModel.getEFactoryInstance().create(class_CharterOutMarket);

							charterOutMarket.eSet(attribute_CharterOutMarket_enabled, charterCostModel.eGet(attribute_CharterCostModel_enabled));
							charterOutMarket.eSet(reference_CharterOutMarket_vesselClass, vesselClass);
							final EObject curve = (EObject) charterCostModel.eGet(reference_CharterCostModel_charterOutPrice);
							charterOutMarket.eSet(reference_CharterOutMarket_charterOutPrice, curve);
							charterOutMarket.eSet(attribute_CharterOutMarket_minCharterOutDuration, charterCostModel.eGet(attribute_CharterCostModel_minCharterOutDuration));

							final String name = String.format("%s - %s", vesselClass.eGet(attribute_NamedObject_name), curve.eGet(attribute_NamedObject_name));
							charterOutMarket.eSet(attribute_NamedObject_name, name);

							charterOutMarkets.add(charterOutMarket);
						}
					}
				}

			}

		}
		spotMarketModel.eUnset(reference_SpotMarketsModel_charteringSpotMarkets);
		spotMarketModel.eSet(reference_SpotMarketsModel_charterInMarkets, charterInMarkets);
		spotMarketModel.eSet(reference_SpotMarketsModel_charterOutMarkets, charterOutMarkets);
	}
}
