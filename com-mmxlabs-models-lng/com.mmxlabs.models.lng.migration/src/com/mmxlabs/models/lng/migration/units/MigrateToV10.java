/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV10 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 9;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 10;
	}

	@Override
	public MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV9Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	public MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV10Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateEntityBooksl(loader, model);
	}

	protected void migrateEntityBooksl(final MetamodelLoader loader, final EObject model) {

		// final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		// final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");
		final EReference reference_LNGScenarioModel_commercialModel = MetamodelUtils.getReference(class_LNGScenarioModel, "commercialModel");

		final EPackage package_CommercialModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);

		final EClass class_CommercialModel = MetamodelUtils.getEClass(package_CommercialModel, "CommercialModel");
		final EClass class_BaseLegalEntity = MetamodelUtils.getEClass(package_CommercialModel, "BaseLegalEntity");
		final EClass class_BaseEntityBook = MetamodelUtils.getEClass(package_CommercialModel, "BaseEntityBook");
		final EClass class_SimpleEntityBook = MetamodelUtils.getEClass(package_CommercialModel, "SimpleEntityBook");

		final EReference reference_CommercialModel_entities = MetamodelUtils.getReference(class_CommercialModel, "entities");
		final EReference reference_BaseLegalEntity_tradingBook = MetamodelUtils.getReference(class_BaseLegalEntity, "tradingBook");
		final EReference reference_BaseLegalEntity_shippingBook = MetamodelUtils.getReference(class_BaseLegalEntity, "shippingBook");
		final EReference reference_BaseLegalEntity_taxRates = MetamodelUtils.getReference(class_BaseLegalEntity, "taxRates");
		final EReference reference_BaseEntityBook_taxRates = MetamodelUtils.getReference(class_BaseEntityBook, "taxRates");

		final EObject commercialModel = (EObject) model.eGet(reference_LNGScenarioModel_commercialModel);
		if (commercialModel == null) {
			return;
		}

		final List<EObject> entities = MetamodelUtils.getValueAsTypedList(commercialModel, reference_CommercialModel_entities);
		if (entities != null) {
			for (final EObject entity : entities) {
				final List<EObject> taxRates = MetamodelUtils.getValueAsTypedList(entity, reference_BaseLegalEntity_taxRates);
				{
					final EObject tradingBook = package_CommercialModel.getEFactoryInstance().create(class_SimpleEntityBook);
					if (taxRates != null) {
						tradingBook.eSet(reference_BaseEntityBook_taxRates, EcoreUtil.copyAll(taxRates));
					}
					entity.eSet(reference_BaseLegalEntity_tradingBook, tradingBook);
				}
				{
					final EObject shippingBook = package_CommercialModel.getEFactoryInstance().create(class_SimpleEntityBook);
					if (taxRates != null) {
						shippingBook.eSet(reference_BaseEntityBook_taxRates, EcoreUtil.copyAll(taxRates));
					}
					entity.eSet(reference_BaseLegalEntity_shippingBook, shippingBook);
				}
				entity.eUnset(reference_BaseLegalEntity_taxRates);
			}
		}
	}
}
