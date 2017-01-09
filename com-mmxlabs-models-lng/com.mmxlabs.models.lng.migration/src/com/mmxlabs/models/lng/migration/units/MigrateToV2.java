/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.PackageData;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

/**
 */
public class MigrateToV2 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 1;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 2;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV1Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV1_V2_IntermediateLoader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		fixSpotMarketGroups(loader, model);

		// Update commodity indicies
		updateIndices(loader, model, "CommodityIndex", "commodityIndices");
		// Update charter indicies
		updateIndices(loader, model, "CharterIndex", "charterIndices");

		// Clear all Index objects name and UUID fields
		clearIndexFields(loader, model);

	}

	protected void fixSpotMarketGroups(MetamodelLoader loader, EObject model) {

		final EPackage scenarioModelPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass lngScenarioModel_Class = MetamodelUtils.getEClass(scenarioModelPackage, "LNGScenarioModel");
		final EReference lngScenarioModel_spotMarketsModel_Reference = MetamodelUtils.getReference(lngScenarioModel_Class, "spotMarketsModel");

		final EPackage spotMarketsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass spotMarketsModel_Class = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketsModel");
		final EClass SpotMarketGroup_Class = MetamodelUtils.getEClass(spotMarketsPackage, "SpotMarketGroup");
		final EEnum SpotType_Enum = MetamodelUtils.getEEnum(spotMarketsPackage, "SpotType");

		final EReference spotMarketsModel_desPurchases_group = MetamodelUtils.getReference(spotMarketsModel_Class, "desPurchaseSpotMarket");
		final EReference spotMarketsModel_desSales_group = MetamodelUtils.getReference(spotMarketsModel_Class, "desSalesSpotMarket");
		final EReference spotMarketsModel_fobPurchases_group = MetamodelUtils.getReference(spotMarketsModel_Class, "fobPurchasesSpotMarket");
		final EReference spotMarketsModel_fobSales_group = MetamodelUtils.getReference(spotMarketsModel_Class, "fobSalesSpotMarket");

		final EAttribute spotMarketsGroup_type = MetamodelUtils.getAttribute(SpotMarketGroup_Class, "type");

		EObject spotMarketsModel = (EObject) model.eGet(lngScenarioModel_spotMarketsModel_Reference);

		if (spotMarketsModel.eGet(spotMarketsModel_desPurchases_group) == null) {
			EObject group = spotMarketsPackage.getEFactoryInstance().create(SpotMarketGroup_Class);
			group.eSet(spotMarketsGroup_type, MetamodelUtils.getEEnum_Literal(SpotType_Enum, "DESPurchase"));
			spotMarketsModel.eSet(spotMarketsModel_desPurchases_group, group);
		}
		if (spotMarketsModel.eGet(spotMarketsModel_desSales_group) == null) {
			EObject group = spotMarketsPackage.getEFactoryInstance().create(SpotMarketGroup_Class);
			group.eSet(spotMarketsGroup_type, MetamodelUtils.getEEnum_Literal(SpotType_Enum, "DESSale"));
			spotMarketsModel.eSet(spotMarketsModel_desSales_group, group);
		}
		if (spotMarketsModel.eGet(spotMarketsModel_fobPurchases_group) == null) {
			EObject group = spotMarketsPackage.getEFactoryInstance().create(SpotMarketGroup_Class);
			group.eSet(spotMarketsGroup_type, MetamodelUtils.getEEnum_Literal(SpotType_Enum, "FOBPurchase"));
			spotMarketsModel.eSet(spotMarketsModel_fobPurchases_group, group);
		}
		if (spotMarketsModel.eGet(spotMarketsModel_fobSales_group) == null) {
			EObject group = spotMarketsPackage.getEFactoryInstance().create(SpotMarketGroup_Class);
			group.eSet(spotMarketsGroup_type, MetamodelUtils.getEEnum_Literal(SpotType_Enum, "FOBSale"));
			spotMarketsModel.eSet(spotMarketsModel_fobSales_group, group);
		}

	}

	protected void clearIndexFields(final MetamodelLoader loader, final EObject model) {
		final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass uuidObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "UUIDObject");
		final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
		final EStructuralFeature namedObject_name_attribute = MetamodelUtils.getAttribute(namedObject_Class, "name");
		final EStructuralFeature uuidObject_uuid_attribute = MetamodelUtils.getAttribute(uuidObject_Class, "uuid");

		final EPackage pricingPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EClass index_Class = MetamodelUtils.getEClass(pricingPackage, "Index");

		final Iterator<EObject> itr = model.eAllContents();
		while (itr.hasNext()) {
			final EObject obj = itr.next();
			if (index_Class.isInstance(obj)) {
				obj.eUnset(uuidObject_uuid_attribute);
				obj.eUnset(namedObject_name_attribute);
			}
		}
	}

	/**
	 * Update the commodity and charter indices data structures and replace references to these objects. This requires an intermediate ecore model set where we use UUIDObject references rather than
	 * real type so we can load both versions of the data model under the saem ecore.
	 * 
	 * @return
	 */
	protected void updateIndices(final MetamodelLoader loader, final EObject scenario, final String wrapperClassName, final String wrapperClassFeatureName) {

		final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass uuidObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "UUIDObject");
		final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
		final EStructuralFeature namedObject_name_attribute = MetamodelUtils.getAttribute(namedObject_Class, "name");
		final EStructuralFeature uuidObject_uuid_attribute = MetamodelUtils.getAttribute(uuidObject_Class, "uuid");

		final EPackage scenarioModelPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass lngScenarioModel_Class = MetamodelUtils.getEClass(scenarioModelPackage, "LNGScenarioModel");
		final EReference lngScenarioModel_PricingModel_Reference = MetamodelUtils.getReference(lngScenarioModel_Class, "pricingModel");

		final EPackage pricingPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_PricingModel);
		final EClass pricingModel_Class = MetamodelUtils.getEClass(pricingPackage, "PricingModel");
		final EClass index_Class = MetamodelUtils.getEClass(pricingPackage, "Index");
		final EClass wrapperIndex_Class = MetamodelUtils.getEClass(pricingPackage, wrapperClassName);
		final EReference wrapperIndex_data_reference = MetamodelUtils.getReference(wrapperIndex_Class, "data");

		final EReference pricingModel_wrapperIndex_Reference = MetamodelUtils.getReference(pricingModel_Class, wrapperClassFeatureName);

		final EObject pricingModel = (EObject) scenario.eGet(lngScenarioModel_PricingModel_Reference);

		final List<EObject> indices = MetamodelUtils.getValueAsTypedList(pricingModel, pricingModel_wrapperIndex_Reference);

		// Build up the replacement map
		final Map<EObject, EObject> oldToNew = new HashMap<EObject, EObject>();
		if (indices != null) {
			final List<EObject> copy = new ArrayList<EObject>(indices);
			for (final EObject obj : copy) {
				if (index_Class.isInstance(obj)) {
					final EObject oldIndex = obj;
					final EObject newIndex = pricingPackage.getEFactoryInstance().create(wrapperIndex_Class);
					// Copy attributes
					newIndex.eSet(uuidObject_uuid_attribute, oldIndex.eGet(uuidObject_uuid_attribute));
					newIndex.eSet(namedObject_name_attribute, oldIndex.eGet(namedObject_name_attribute));

					// Replace entry in the datamodel
					EcoreUtil.replace(pricingModel, pricingModel_wrapperIndex_Reference, oldIndex, newIndex);

					// Link index to new wrapper
					newIndex.eSet(wrapperIndex_data_reference, oldIndex);

					oldToNew.put(oldIndex, newIndex);
				}
			}

			// Replace all cross references
			final Map<EObject, Collection<Setting>> usages = EcoreUtil.UsageCrossReferencer.findAll(oldToNew.keySet(), scenario);
			if (usages != null) {

				for (final Map.Entry<EObject, Collection<Setting>> e : usages.entrySet()) {
					final EObject eObject = e.getKey();
					for (final Setting setting : e.getValue()) {
						EcoreUtil.replace(setting.getEObject(), setting.getEStructuralFeature(), eObject, oldToNew.get(eObject));
					}
				}
			}

		}
		for (final EObject eObject : oldToNew.keySet()) {
			eObject.eUnset(uuidObject_uuid_attribute);
			eObject.eUnset(namedObject_name_attribute);
		}

		return;
	}
}
