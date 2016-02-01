/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
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
 */
public class MigrateToV8 extends AbstractMigrationUnit {


	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 7;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 8;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV7Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV8Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {
		// This should get the cached loader instance
		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		migrateEntities(loader, model);

		migrateScenarioFleetModel(loader, model);
	}

	protected void migrateEntities(final MetamodelLoader loader, final EObject model) {

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

		final EObject commercialModel = (EObject) model.eGet(reference_LNGScenarioModel_commercialModel);
		final List<EObject> entities = MetamodelUtils.getValueAsTypedList(commercialModel, reference_CommercialModel_entities);

		final Map<EObject, EObject> originalToNew = new HashMap<>();
		if (entities != null) {
			for (final EObject entity : entities) {

				if (class_BaseLegalEntity.isInstance(entity)) {
					if (!class_LegalEntity.isInstance(entity)) {
						// Create new entity
						final EObject newEntity = package_CommercialModel.getEFactoryInstance().create(class_LegalEntity);
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
		}

		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByOriginal = UsageCrossReferencer.findAll(originalToNew.keySet(), model);

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

	protected void migrateScenarioFleetModel(final MetamodelLoader loader, final EObject model) {

		final EPackage mmxCorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		final EClass uuidObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "UUIDObject");
		final EClass namedObject_Class = MetamodelUtils.getEClass(mmxCorePackage, "NamedObject");
		final EStructuralFeature attribute_NamedObject_name = MetamodelUtils.getAttribute(namedObject_Class, "name");
		final EStructuralFeature attribute_UUIDObject_uuid = MetamodelUtils.getAttribute(uuidObject_Class, "uuid");

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");
		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");

		final EReference reference_LNGScenarioModel_fleetModel = MetamodelUtils.getReference(class_LNGScenarioModel, "fleetModel");
		final EReference reference_LNGPortfolioModel_scenarioFleetModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scenarioFleetModel");
		final EPackage package_FleetModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);

		final EClass class_FleetModel = MetamodelUtils.getEClass(package_FleetModel, "FleetModel");
		final EClass class_ScenarioFleetModel = MetamodelUtils.getEClass(package_FleetModel, "ScenarioFleetModel");
//		final EClass class_fleet_VesselAvailability = MetamodelUtils.getEClass(package_FleetModel, "VesselAvailability");
//		final EClass class_fleet_AssignableElement = MetamodelUtils.getEClass(package_FleetModel, "AssignableElement");
//		final EClass class_fleet_VesselEvent = MetamodelUtils.getEClass(package_FleetModel, "VesselEvent");
		final EClass class_fleet_DryDockEvent = MetamodelUtils.getEClass(package_FleetModel, "DryDockEvent");
		final EClass class_fleet_MaintenanceEvent = MetamodelUtils.getEClass(package_FleetModel, "MaintenanceEvent");
		final EClass class_fleet_CharterOutEvent = MetamodelUtils.getEClass(package_FleetModel, "CharterOutEvent");

//		final EEnum enum_fleet_VesselType = MetamodelUtils.getEEnum(package_FleetModel, "VesselType");
		final EClass class_fleet_VesselTypeGroup = MetamodelUtils.getEClass(package_FleetModel, "VesselTypeGroup");

		final EAttribute attribute_fleet_VesselTypeGroup_vesselType = MetamodelUtils.getAttribute(class_fleet_VesselTypeGroup, "vesselType");

		final EReference reference_FleetModel_specialVesselGroups = MetamodelUtils.getReference(class_FleetModel, "specialVesselGroups");
		final EReference reference_ScenarioFleetModel_vesselAvailabilies = MetamodelUtils.getReference(class_ScenarioFleetModel, "vesselAvailabilities");
		final EReference reference_ScenarioFleetModel_vesselEvents = MetamodelUtils.getReference(class_ScenarioFleetModel, "vesselEvents");

		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");
		final EPackage package_CargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EClass class_CargoModel = MetamodelUtils.getEClass(package_CargoModel, "CargoModel");
		final EClass class_cargo_VesselAvailability = MetamodelUtils.getEClass(package_CargoModel, "VesselAvailability");
//		final EClass class_cargo_AssignableElement = MetamodelUtils.getEClass(package_CargoModel, "AssignableElement");
//		final EClass class_cargo_VesselEvent = MetamodelUtils.getEClass(package_CargoModel, "VesselEvent");
		final EClass class_cargo_DryDockEvent = MetamodelUtils.getEClass(package_CargoModel, "DryDockEvent");
		final EClass class_cargo_MaintenanceEvent = MetamodelUtils.getEClass(package_CargoModel, "MaintenanceEvent");
		final EClass class_cargo_CharterOutEvent = MetamodelUtils.getEClass(package_CargoModel, "CharterOutEvent");

		final EEnum enum_cargo_VesselType = MetamodelUtils.getEEnum(package_CargoModel, "VesselType");
		final EClass class_cargo_VesselTypeGroup = MetamodelUtils.getEClass(package_CargoModel, "VesselTypeGroup");

		final EAttribute attribute_cargo_VesselTypeGroup_vesselType = MetamodelUtils.getAttribute(class_cargo_VesselTypeGroup, "vesselType");

		final EReference reference_CargoModel_vesselAvailabilies = MetamodelUtils.getReference(class_CargoModel, "vesselAvailabilities");
		final EReference reference_CargoModel_vesselEvents = MetamodelUtils.getReference(class_CargoModel, "vesselEvents");
		final EReference reference_CargoModel_vesselTypeGroups = MetamodelUtils.getReference(class_CargoModel, "vesselTypeGroups");
		final EReference reference_cargo_VesselAvailability_entity = MetamodelUtils.getReference(class_cargo_VesselAvailability, "entity");

		final EReference reference_LNGScenarioModel_commercialModel = MetamodelUtils.getReference(class_LNGScenarioModel, "commercialModel");
		final EPackage package_CommercialModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CommercialModel);
		final EClass class_CommercialModel = MetamodelUtils.getEClass(package_CommercialModel, "CommercialModel");
		final EReference reference_commercialModel_shippingEntity = MetamodelUtils.getReference(class_CommercialModel, "shippingEntity");

		final EObject fleetModel = (EObject) model.eGet(reference_LNGScenarioModel_fleetModel);
		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		final EObject scenarioFleetModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_scenarioFleetModel);
		final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);
		final EObject commercialModel = (EObject) model.eGet(reference_LNGScenarioModel_commercialModel);

		// map for cross reference update
		final Map<EObject, EObject> originalToNew = new HashMap<>();
		final EObject shippingEntity = (EObject) commercialModel.eGet(reference_commercialModel_shippingEntity);
		commercialModel.eUnset(reference_commercialModel_shippingEntity);

		if (scenarioFleetModel != null) {

			// Phase 1 - Move vessel availabilities and set default shipping entity
			{

				final List<EObject> fleetModelAvailabilities = MetamodelUtils.getValueAsTypedList(scenarioFleetModel, reference_ScenarioFleetModel_vesselAvailabilies);
				if (fleetModelAvailabilities != null) {
					final List<EObject> cargo_VA = new ArrayList<>(fleetModelAvailabilities.size());
					for (final EObject fleet_VA : fleetModelAvailabilities) {

						// Construct new VA - copy data over
						final EObject newVA = package_CargoModel.getEFactoryInstance().create(class_cargo_VesselAvailability);
						for (final EStructuralFeature f : fleet_VA.eClass().getEAllStructuralFeatures()) {
							if (fleet_VA.eIsSet(f)) {
								newVA.eSet(newVA.eClass().getEStructuralFeature(f.getName()), fleet_VA.eGet(f));
							}
						}
						// set shipping entity
						newVA.eSet(reference_cargo_VesselAvailability_entity, shippingEntity);

						// Add to list
						cargo_VA.add(newVA);

						// Replace cross references
						originalToNew.put(fleet_VA, newVA);
					}
					scenarioFleetModel.eUnset(reference_ScenarioFleetModel_vesselAvailabilies);
					cargoModel.eSet(reference_CargoModel_vesselAvailabilies, cargo_VA);
				}
			}

			// Phase 2 - Move vessel events
			{
				final List<EObject> fleetModelEvents = MetamodelUtils.getValueAsTypedList(scenarioFleetModel, reference_ScenarioFleetModel_vesselEvents);
				if (fleetModelEvents != null) {
					final List<EObject> cargo_Events = new ArrayList<>(fleetModelEvents.size());
					for (final EObject fleet_Event : fleetModelEvents) {
						EObject newEvent = null;
						if (class_fleet_MaintenanceEvent.isInstance(fleet_Event)) {
							newEvent = package_CargoModel.getEFactoryInstance().create(class_cargo_MaintenanceEvent);
						} else if (class_fleet_CharterOutEvent.isInstance(fleet_Event)) {
							newEvent = package_CargoModel.getEFactoryInstance().create(class_cargo_CharterOutEvent);
						} else if (class_fleet_DryDockEvent.isInstance(fleet_Event)) {
							newEvent = package_CargoModel.getEFactoryInstance().create(class_cargo_DryDockEvent);
						}
						if (newEvent != null) {
							for (final EStructuralFeature f : fleet_Event.eClass().getEAllStructuralFeatures()) {
								if (fleet_Event.eIsSet(f)) {
									newEvent.eSet(newEvent.eClass().getEStructuralFeature(f.getName()), fleet_Event.eGet(f));
								}
							}
						}

						// Add to list
						cargo_Events.add(newEvent);

						// Replace cross references
						originalToNew.put(fleet_Event, newEvent);

					}
					scenarioFleetModel.eUnset(reference_ScenarioFleetModel_vesselEvents);
					cargoModel.eSet(reference_CargoModel_vesselEvents, cargo_Events);
				}
			}
		}
		// Phase 3 - move vessel type groups
		{

			final List<EObject> specialVesselGroups = MetamodelUtils.getValueAsTypedList(fleetModel, reference_FleetModel_specialVesselGroups);
			if (specialVesselGroups != null) {
				final List<EObject> vesselTypeGroups = new ArrayList<>(specialVesselGroups.size());
				for (final EObject specialGroup : specialVesselGroups) {

					// Construct new VA - copy data over
					final EObject typeGroup = package_CargoModel.getEFactoryInstance().create(class_cargo_VesselTypeGroup);

					final EEnumLiteral vesselType = (EEnumLiteral) specialGroup.eGet(attribute_fleet_VesselTypeGroup_vesselType);
					final EEnumLiteral newVesselType = MetamodelUtils.getEEnum_Literal(enum_cargo_VesselType, vesselType.toString());
					typeGroup.eSet(attribute_cargo_VesselTypeGroup_vesselType, newVesselType);

					typeGroup.eSet(attribute_UUIDObject_uuid, specialGroup.eGet(attribute_UUIDObject_uuid));
					typeGroup.eSet(attribute_NamedObject_name, specialGroup.eGet(attribute_NamedObject_name));

					// Add to list
					vesselTypeGroups.add(typeGroup);

					// Replace cross references
					originalToNew.put(specialGroup, typeGroup);
				}
				fleetModel.eUnset(reference_FleetModel_specialVesselGroups);
				cargoModel.eSet(reference_CargoModel_vesselTypeGroups, vesselTypeGroups);
			}
		}

		// Phase 4 - remove the ScenarioFleetModel
		portfolioModel.eUnset(reference_LNGPortfolioModel_scenarioFleetModel);

		// Phase 5 - update cross references

		final Map<EObject, Collection<EStructuralFeature.Setting>> usagesByOriginal = UsageCrossReferencer.findAll(originalToNew.keySet(), model);

//		final EReference reference_LNGPortfolioModel_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");
		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EClass class_VesselEventVisit = MetamodelUtils.getEClass(package_ScheduleModel, "VesselEventVisit");
		final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");

		final EReference reference_VesselEventVisit_vesselEvent = MetamodelUtils.getReference(class_VesselEventVisit, "vesselEvent");
		final EReference reference_VesselEventVisit_vesselEvent2 = MetamodelUtils.getReference(class_VesselEventVisit, "vesselEvent2");
		final EReference reference_Sequence_vesselAvailability = MetamodelUtils.getReference(class_Sequence, "vesselAvailability");
		final EReference reference_Sequence_vesselAvailability2 = MetamodelUtils.getReference(class_Sequence, "vesselAvailability2");

		for (final Map.Entry<EObject, EObject> entry : originalToNew.entrySet()) {
			final EObject originalEntity = entry.getKey();
			final EObject newEntity = entry.getValue();

			final Collection<EStructuralFeature.Setting> usages = usagesByOriginal.get(originalEntity);
			if (usages != null) {
				for (final EStructuralFeature.Setting setting : usages) {
					if (setting.getEStructuralFeature() == reference_VesselEventVisit_vesselEvent) {
						setting.getEObject().eUnset(reference_VesselEventVisit_vesselEvent);
						setting.getEObject().eSet(reference_VesselEventVisit_vesselEvent2, newEntity);
					} else if (setting.getEStructuralFeature() == reference_Sequence_vesselAvailability) {
						setting.getEObject().eUnset(reference_Sequence_vesselAvailability);
						setting.getEObject().eSet(reference_Sequence_vesselAvailability2, newEntity);
					} else {
						EcoreUtil.replace(setting, originalEntity, newEntity);
					}
				}
			}
			EcoreUtil.replace(originalEntity, newEntity);
		}
	}
}
