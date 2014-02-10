/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.UsageCrossReferencer;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

/**
 * @since 8.0
 */
public class MigrateToV9 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 7;
	}

	@Override
	public int getDestinationVersion() {
		return -8;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV6Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV7Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateEntities(loader, model);
		
		
		migrateScenarioFleetModel(loader, model);
	}

	protected void migrateEntities(MetamodelLoader loader, EObject model) {

		final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass uuidObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "UUIDObject");
		final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
		final EStructuralFeature attribute_NamedObject_name = MetamodelUtils.getAttribute(namedObject_Class, "name");
		final EStructuralFeature attribute_UUIDObject_uuid = MetamodelUtils.getAttribute(uuidObject_Class, "uuid");

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");

		final EReference reference_LNGScenarioModel_commercialModel = MetamodelUtils.getReference(class_LNGScenarioModel, "commercialModel");

		final EPackage package_CommercialModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);

		final EClass class_CommercialModel = MetamodelUtils.getEClass(package_CommercialModel, "CommercialModel");
		final EClass class_BaseLegalEntity = MetamodelUtils.getEClass(package_CommercialModel, "BaseLegalEntity");
		final EClass class_LegalEntity = MetamodelUtils.getEClass(package_CommercialModel, "LegalEntity");

		final EReference reference_CommercialModel_entities = MetamodelUtils.getReference(class_CommercialModel, "entities");

		final EReference reference_BaseLegalEntity_taxRates = MetamodelUtils.getReference(class_BaseLegalEntity, "taxRates");

		EObject commercialModel = (EObject) model.eGet(reference_LNGScenarioModel_commercialModel);
		List<EObject> entities = MetamodelUtils.getValueAsTypedList(commercialModel, reference_CommercialModel_entities);

		Map<EObject, EObject> originalToNew = new HashMap<>();
		for (EObject entity : entities) {

			if (class_BaseLegalEntity.isInstance(entity)) {
				if (!class_LegalEntity.isInstance(entity)) {
					// Create new entity
					EObject newEntity = package_CommercialModel.getEFactoryInstance().create(class_LegalEntity);
					// Copy base attributes
					newEntity.eSet(attribute_NamedObject_name, entity.eGet(attribute_NamedObject_name));
					newEntity.eSet(attribute_UUIDObject_uuid, entity.eGet(attribute_UUIDObject_uuid));
					// Move tax data
					newEntity.eSet(reference_BaseLegalEntity_taxRates, entity.eGet(reference_BaseLegalEntity_taxRates));

					// Create map for reference update
					originalToNew.put(entity, newEntity);
				}
			}
		}

		Map<EObject, Collection<EStructuralFeature.Setting>> usagesByOriginal = UsageCrossReferencer.findAll(originalToNew.keySet(), model);

		for (final Map.Entry<EObject, EObject> entry : originalToNew.entrySet()) {
			final EObject originalEntity = entry.getKey();
			final EObject newEntity = entry.getValue();

			final Collection<EStructuralFeature.Setting> usages = usagesByOriginal.get(originalEntity);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					EcoreUtil.replace(setting, originalEntity, newEntity);
				}
			}
			EcoreUtil.replace(originalEntity, newEntity);
		}
	}
	protected void migrateScenarioFleetModel(MetamodelLoader loader, EObject model) {
		
		final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass uuidObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "UUIDObject");
		final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
		final EStructuralFeature attribute_NamedObject_name = MetamodelUtils.getAttribute(namedObject_Class, "name");
		final EStructuralFeature attribute_UUIDObject_uuid = MetamodelUtils.getAttribute(uuidObject_Class, "uuid");
		
		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		
		final EReference reference_LNGScenarioModel_commercialModel = MetamodelUtils.getReference(class_LNGScenarioModel, "commercialModel");
		
		final EPackage package_CommercialModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		
		final EClass class_CommercialModel = MetamodelUtils.getEClass(package_CommercialModel, "CommercialModel");
		final EClass class_BaseLegalEntity = MetamodelUtils.getEClass(package_CommercialModel, "BaseLegalEntity");
		final EClass class_LegalEntity = MetamodelUtils.getEClass(package_CommercialModel, "LegalEntity");
		
		final EReference reference_CommercialModel_entities = MetamodelUtils.getReference(class_CommercialModel, "entities");
		
		final EReference reference_BaseLegalEntity_taxRates = MetamodelUtils.getReference(class_BaseLegalEntity, "taxRates");
		
		EObject commercialModel = (EObject) model.eGet(reference_LNGScenarioModel_commercialModel);
		List<EObject> entities = MetamodelUtils.getValueAsTypedList(commercialModel, reference_CommercialModel_entities);
		
		Map<EObject, EObject> originalToNew = new HashMap<>();
		for (EObject entity : entities) {
			
			if (class_BaseLegalEntity.isInstance(entity)) {
				if (!class_LegalEntity.isInstance(entity)) {
					// Create new entity
					EObject newEntity = package_CommercialModel.getEFactoryInstance().create(class_LegalEntity);
					// Copy base attributes
					newEntity.eSet(attribute_NamedObject_name, entity.eGet(attribute_NamedObject_name));
					newEntity.eSet(attribute_UUIDObject_uuid, entity.eGet(attribute_UUIDObject_uuid));
					// Move tax data
					newEntity.eSet(reference_BaseLegalEntity_taxRates, entity.eGet(reference_BaseLegalEntity_taxRates));
					
					// Create map for reference update
					originalToNew.put(entity, newEntity);
				}
			}
		}
		
		Map<EObject, Collection<EStructuralFeature.Setting>> usagesByOriginal = UsageCrossReferencer.findAll(originalToNew.keySet(), model);
		
		for (final Map.Entry<EObject, EObject> entry : originalToNew.entrySet()) {
			final EObject originalEntity = entry.getKey();
			final EObject newEntity = entry.getValue();
			
			final Collection<EStructuralFeature.Setting> usages = usagesByOriginal.get(originalEntity);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					EcoreUtil.replace(setting, originalEntity, newEntity);
				}
			}
			EcoreUtil.replace(originalEntity, newEntity);
		}
	}
}
