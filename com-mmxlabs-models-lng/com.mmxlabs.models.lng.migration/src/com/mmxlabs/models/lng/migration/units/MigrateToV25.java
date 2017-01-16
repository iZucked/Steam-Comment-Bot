/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
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

public class MigrateToV25 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 24;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 25;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV24Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destinationLoader == null) {
			destinationLoader = MetamodelVersionsUtil.createV25Loader(extraPackages);
		}
		return destinationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader loader = getDestinationMetamodelLoader(null);

		final EPackage package_ScenarioModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_LNGPortfolioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGPortfolioModel");
		final EReference reference_LNGScenarioModel_spotMarketsModel = MetamodelUtils.getReference(class_LNGScenarioModel, "spotMarketsModel");
		final EReference reference_LNGScenarioModel_portfolioModel = MetamodelUtils.getReference(class_LNGScenarioModel, "portfolioModel");
		final EReference reference_LNGPortfolioModel_cargoModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "cargoModel");
		final EReference reference_LNGPortfolioModel_scheduleModel = MetamodelUtils.getReference(class_LNGPortfolioModel, "scheduleModel");

		final EPackage package_SpotMarketsModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_SpotMarketsModel);

		final EClass class_SpotMarketsModel = MetamodelUtils.getEClass(package_SpotMarketsModel, "SpotMarketsModel");
		final EReference reference_SpotMarketsModel_charterInMarkets = MetamodelUtils.getReference(class_SpotMarketsModel, "charterInMarkets");

		final EClass class_CharterInMarket = MetamodelUtils.getEClass(package_SpotMarketsModel, "CharterInMarket");
		// final EAttribute attribute_CharterInMarket_enabled = MetamodelUtils.getAttribute(class_CharterInMarket, "enabled");
		final EReference reference_CharterInMarket_vesselClass = MetamodelUtils.getReference(class_CharterInMarket, "vesselClass");
		final EAttribute attribute_CharterInMarket_spotCharterCount = MetamodelUtils.getAttribute(class_CharterInMarket, "spotCharterCount");

		final EPackage package_fleetModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);
		final EClass class_Vessel = MetamodelUtils.getEClass(package_fleetModel, "Vessel");
		final EClass class_VesselClass = MetamodelUtils.getEClass(package_fleetModel, "VesselClass");

		final EPackage package_cargoModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_CargoModel = MetamodelUtils.getEClass(package_cargoModel, "CargoModel");
		final EReference reference_CargoModel_vesselAvailabilities = MetamodelUtils.getReference(class_CargoModel, "vesselAvailabilities");
		final EReference reference_CargoModel_vesselEvents = MetamodelUtils.getReference(class_CargoModel, "vesselEvents");
		final EReference reference_CargoModel_cargoes = MetamodelUtils.getReference(class_CargoModel, "cargoes");
		final EReference reference_CargoModel_loadSlots = MetamodelUtils.getReference(class_CargoModel, "loadSlots");
		final EReference reference_CargoModel_dischargeSlots = MetamodelUtils.getReference(class_CargoModel, "dischargeSlots");

		final EClass class_VesselAvailability = MetamodelUtils.getEClass(package_cargoModel, "VesselAvailability");
		final EReference reference_VesselAvailability_vessel = MetamodelUtils.getReference(class_VesselAvailability, "vessel");
		// final EClass class_Cargo = MetamodelUtils.getEClass(package_cargoModel, "Cargo");
		final EClass class_Slot = MetamodelUtils.getEClass(package_cargoModel, "Slot");
		final EReference refernce_Slot_nominatedVessel = MetamodelUtils.getReference(class_Slot, "nominatedVessel");

		// final EClass class_LoadSlot = MetamodelUtils.getEClass(package_cargoModel, "LoadSlot");
		// final EClass class_DischargeSlot = MetamodelUtils.getEClass(package_cargoModel, "DischargeSlot");
		// final EClass class_VesselEvent = MetamodelUtils.getEClass(package_cargoModel, "VesselEvent");

		final EClass class_AssignableElement = MetamodelUtils.getEClass(package_cargoModel, "AssignableElement");
		final EReference reference_AssignableElement_assignment = MetamodelUtils.getReference(class_AssignableElement, "assignment");
		final EAttribute attribute_AssignableElement_spotIndex = MetamodelUtils.getAttribute(class_AssignableElement, "spotIndex");
		final EAttribute attribute_AssignableElement_sequenceHint = MetamodelUtils.getAttribute(class_AssignableElement, "sequenceHint");
		final EReference reference_AssignableElement_vesselAssignmentType = MetamodelUtils.getReference(class_AssignableElement, "vesselAssignmentType");

		final EPackage package_ScheduleModel = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);
		final EClass class_ScheduleModel = MetamodelUtils.getEClass(package_ScheduleModel, "ScheduleModel");
		final EReference reference_ScheduleModel_schedule = MetamodelUtils.getReference(class_ScheduleModel, "schedule");

		final EClass class_Schedule = MetamodelUtils.getEClass(package_ScheduleModel, "Schedule");
		final EReference reference_Schedule_sequences = MetamodelUtils.getReference(class_Schedule, "sequences");

		final EClass class_Sequence = MetamodelUtils.getEClass(package_ScheduleModel, "Sequence");
		final EReference reference_Sequence_vesselClass = MetamodelUtils.getReference(class_Sequence, "vesselClass");
		final EReference reference_Sequence_charterInMarket = MetamodelUtils.getReference(class_Sequence, "charterInMarket");

		final Map<EObject, EObject> vesselClassToFirstMarketMap = new HashMap<>();
		// For the cargo model
		final Map<EObject, List<EObject>> vesselClassToMarketOptionsMap1 = new HashMap<>();
		// For the schedule model
		final Map<EObject, List<EObject>> vesselClassToMarketOptionsMap2 = new HashMap<>();

		// Process spot markets models to pull out charter in market options
		{
			final EObject spotMarketModel = (EObject) model.eGet(reference_LNGScenarioModel_spotMarketsModel);
			if (spotMarketModel == null) {
				return;
			}

			final List<EObject> charterInModels = MetamodelUtils.getValueAsTypedList(spotMarketModel, reference_SpotMarketsModel_charterInMarkets);
			if (charterInModels != null) {
				for (final EObject charterInModel : charterInModels) {
					final EObject vesselClass = (EObject) charterInModel.eGet(reference_CharterInMarket_vesselClass);
					if (vesselClass != null) {
						final int spotCharterCount = (Integer) charterInModel.eGet(attribute_CharterInMarket_spotCharterCount);

						if (!vesselClassToFirstMarketMap.containsKey(vesselClass)) {
							vesselClassToFirstMarketMap.put(vesselClass, charterInModel);
						}
						List<EObject> queue1;
						if (vesselClassToMarketOptionsMap1.containsKey(vesselClass)) {
							queue1 = vesselClassToMarketOptionsMap1.get(vesselClass);
						} else {
							queue1 = new LinkedList<>();
							vesselClassToMarketOptionsMap1.put(vesselClass, queue1);
						}
						for (int i = 0; i < spotCharterCount; ++i) {
							queue1.add(charterInModel);

						}
						List<EObject> queue2;
						if (vesselClassToMarketOptionsMap2.containsKey(vesselClass)) {
							queue2 = vesselClassToMarketOptionsMap2.get(vesselClass);
						} else {
							queue2 = new LinkedList<>();
							vesselClassToMarketOptionsMap2.put(vesselClass, queue2);
						}
						for (int i = 0; i < spotCharterCount; ++i) {
							queue2.add(charterInModel);

						}
					}
				}
			}
		}

		final EObject portfolioModel = (EObject) model.eGet(reference_LNGScenarioModel_portfolioModel);
		if (portfolioModel != null) {
			final EObject cargoModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_cargoModel);
			if (cargoModel != null) {

				// Process cargo and events.

				// Convert DES Purchase information.
				final List<EObject> loadSlots = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_loadSlots);
				if (loadSlots != null) {
					for (final EObject loadSlot : loadSlots) {
						if (loadSlot.eIsSet(reference_AssignableElement_assignment)) {
							final EObject vessel = (EObject) loadSlot.eGet(reference_AssignableElement_assignment);
							loadSlot.eSet(refernce_Slot_nominatedVessel, vessel);
							loadSlot.eUnset(reference_AssignableElement_assignment);
						}
						loadSlot.eUnset(attribute_AssignableElement_sequenceHint);
						loadSlot.eUnset(attribute_AssignableElement_spotIndex);
					}
				}

				// Convert FOB Sale information
				final List<EObject> dischargeSlots = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_dischargeSlots);
				if (dischargeSlots != null) {
					for (final EObject dischargeSlot : dischargeSlots) {
						if (dischargeSlot.eIsSet(reference_AssignableElement_assignment)) {
							final EObject vessel = (EObject) dischargeSlot.eGet(reference_AssignableElement_assignment);
							dischargeSlot.eSet(refernce_Slot_nominatedVessel, vessel);
							dischargeSlot.eUnset(reference_AssignableElement_assignment);
						}
						dischargeSlot.eUnset(attribute_AssignableElement_sequenceHint);
						dischargeSlot.eUnset(attribute_AssignableElement_spotIndex);
					}
				}
				final Map<EObject, EObject> vesselToVesselAvailabilityMap = new HashMap<>();
				{
					// Populate map
					final List<EObject> vesselAvailabilities = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_vesselAvailabilities);
					if (vesselAvailabilities != null) {
						for (final EObject vesselAvailability : vesselAvailabilities) {
							vesselToVesselAvailabilityMap.put((EObject) vesselAvailability.eGet(reference_VesselAvailability_vessel), vesselAvailability);
						}
					}
				}
				final List<EObject> cargoes = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_cargoes);
				if (cargoes != null) {
					for (final EObject cargo : cargoes) {
						if (cargo.eIsSet(reference_AssignableElement_assignment)) {
							final EObject vesselOrVesselClass = (EObject) cargo.eGet(reference_AssignableElement_assignment);
							if (class_Vessel.isInstance(vesselOrVesselClass)) {
								cargo.eSet(reference_AssignableElement_vesselAssignmentType, vesselToVesselAvailabilityMap.get(vesselOrVesselClass));
							} else if (class_VesselClass.isInstance(vesselOrVesselClass)) {
								final List<EObject> options = vesselClassToMarketOptionsMap1.get(vesselOrVesselClass);
								if (options != null && !options.isEmpty()) {
									cargo.eSet(reference_AssignableElement_vesselAssignmentType, options.remove(0));
								} else {
									cargo.eSet(reference_AssignableElement_vesselAssignmentType, vesselClassToFirstMarketMap.get(vesselOrVesselClass));
								}
							}
							cargo.eUnset(reference_AssignableElement_assignment);
						}
					}
				}

				final List<EObject> events = MetamodelUtils.getValueAsTypedList(cargoModel, reference_CargoModel_vesselEvents);
				if (events != null) {
					for (final EObject event : events) {
						if (event.eIsSet(reference_AssignableElement_assignment)) {
							final EObject vesselOrVesselClass = (EObject) event.eGet(reference_AssignableElement_assignment);
							if (class_Vessel.isInstance(vesselOrVesselClass)) {
								event.eSet(reference_AssignableElement_vesselAssignmentType, vesselToVesselAvailabilityMap.get(vesselOrVesselClass));
							} else if (class_VesselClass.isInstance(vesselOrVesselClass)) {
								final List<EObject> options = vesselClassToMarketOptionsMap1.get(vesselOrVesselClass);
								if (options != null && !options.isEmpty()) {
									event.eSet(reference_AssignableElement_vesselAssignmentType, options.remove(0));
								} else {
									event.eSet(reference_AssignableElement_vesselAssignmentType, vesselClassToFirstMarketMap.get(vesselOrVesselClass));
								}
							}
							event.eUnset(reference_AssignableElement_assignment);
						}
					}
				}
			}

			final EObject scheduleModel = (EObject) portfolioModel.eGet(reference_LNGPortfolioModel_scheduleModel);
			if (scheduleModel != null) {
				final EObject schedule = (EObject) scheduleModel.eGet(reference_ScheduleModel_schedule);
				if (schedule != null) {
					final EList<EObject> sequences = MetamodelUtils.getValueAsTypedList(schedule, reference_Schedule_sequences);
					if (sequences != null) {
						for (final EObject sequence : sequences) {
							if (sequence.eIsSet(reference_Sequence_vesselClass)) {
								final EObject vesselClass = (EObject) sequence.eGet(reference_Sequence_vesselClass);
								final List<EObject> options = vesselClassToMarketOptionsMap2.get(vesselClass);
								if (options != null && !options.isEmpty()) {
									sequence.eSet(reference_Sequence_charterInMarket, options.remove(0));
								} else {
									sequence.eSet(reference_Sequence_charterInMarket, vesselClassToFirstMarketMap.get(vesselClass));
								}
							}
							sequence.eUnset(reference_Sequence_vesselClass);
						}
					}
				}
			}
		}
	}
}
