/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;

import com.google.common.collect.Lists;
import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV3 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return -2;
	}

	@Override
	public int getDestinationVersion() {
		return -3;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<String, URI> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV2Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<String, URI> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV3Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final Map<ModelsLNGSet_v1, EObject> models) {
		final MetamodelLoader v2Loader = destiniationLoader;

		migrateCargoModel(v2Loader, models);
		migrateScheduleModel(v2Loader, models);
	}

	private void migrateCargoModel(MetamodelLoader loader, Map<ModelsLNGSet_v1, EObject> models) {
		final EObject cargoModel = models.get(ModelsLNGSet_v1.Cargo);
		final EPackage cargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_Cargo = MetamodelUtils.getEClass(cargoPackage, "Cargo");
		final EStructuralFeature feature_Cargo_slots = MetamodelUtils.getStructuralFeature(class_Cargo, "slots");

		final EClass class_CargoModel = MetamodelUtils.getEClass(cargoPackage, "CargoModel");
		final EStructuralFeature feature_cargoes = MetamodelUtils.getStructuralFeature(class_CargoModel, "cargoes");

		final Map<EObject, AnyType> oldFeatures = ((XMLResource) cargoModel.eResource()).getEObjectToExtensionMap();

		final EPackage mmxcorePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_MMXCore);
		EClass class_UUIDObject = MetamodelUtils.getEClass(mmxcorePackage, "UUIDObject");
		final EStructuralFeature feature_uuid = MetamodelUtils.getStructuralFeature(class_UUIDObject, "uuid");

		Map<String, EObject> slotMap = new HashMap<String, EObject>();
		{
			final EList<EObject> slots = (EList<EObject>) cargoModel.eGet(MetamodelUtils.getStructuralFeature(class_CargoModel, "loadSlots"));
			for (EObject slot : slots) {
				slotMap.put((String) slot.eGet(feature_uuid), slot);
			}
		}
		{
			final EList<EObject> slots = (EList<EObject>) cargoModel.eGet(MetamodelUtils.getStructuralFeature(class_CargoModel, "dischargeSlots"));
			for (EObject slot : slots) {
				slotMap.put((String) slot.eGet(feature_uuid), slot);
			}
		}

		@SuppressWarnings("unchecked")
		final EList<EObject> cargoes = (EList<EObject>) cargoModel.eGet(feature_cargoes);
		for (final EObject cargo : cargoes) {
			// Convert unknown features
			if (oldFeatures.containsKey(cargo)) {

				final AnyType anyType = oldFeatures.get(cargo);

				List<Object> slots = new ArrayList<Object>(2);
				slots.add(null);
				slots.add(null);
				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Copy value from attributeB to attributeC
					if (eStructuralFeature.getName().equals("loadSlot")) {
						slots.set(0, slotMap.get(entry.getValue()));
						iter.remove();
					}
					if (eStructuralFeature.getName().equals("dischargeSlot")) {
						slots.set(1, slotMap.get(entry.getValue()));
						iter.remove();
					}
				}
				slots.remove(null);
				slots.remove(null);
				cargo.eSet(feature_Cargo_slots, slots);
			}
		}
	}

	private void migrateScheduleModel(MetamodelLoader loader, Map<ModelsLNGSet_v1, EObject> models) {
		final EObject scheduleModel = models.get(ModelsLNGSet_v1.Schedule);
		final EPackage schedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_CargoAllocation = MetamodelUtils.getEClass(schedulePackage, "CargoAllocation");
		final EStructuralFeature feature_CargoAllocation_slotAllocations = MetamodelUtils.getStructuralFeature(class_CargoAllocation, "slotAllocations");
		final EStructuralFeature feature_CargoAllocation_events = MetamodelUtils.getStructuralFeature(class_CargoAllocation, "events");

		final EClass class_SlotAllocation = MetamodelUtils.getEClass(schedulePackage, "SlotAllocation");
		final EStructuralFeature feature_SlotAllocation_volumeTransferred = MetamodelUtils.getStructuralFeature(class_SlotAllocation, "volumeTransferred");

		final EClass class_ScheduleModel = MetamodelUtils.getEClass(schedulePackage, "ScheduleModel");
		final EStructuralFeature feature_ScheduleModel_schedule = MetamodelUtils.getStructuralFeature(class_ScheduleModel, "schedule");

		final EClass class_Schedule = MetamodelUtils.getEClass(schedulePackage, "Schedule");
		final EStructuralFeature feature_Schedule_cargoAllocations = MetamodelUtils.getStructuralFeature(class_Schedule, "cargoAllocations");

		EObject schedule = (EObject) scheduleModel.eGet(feature_ScheduleModel_schedule);
		if (schedule == null) {
			return;
		}

		final Map<EObject, AnyType> oldFeatures = ((XMLResource) scheduleModel.eResource()).getEObjectToExtensionMap();

		@SuppressWarnings("unchecked")
		final EList<EObject> cargoAllocations = (EList<EObject>) schedule.eGet(feature_Schedule_cargoAllocations);
		for (final EObject cargoAllocation : cargoAllocations) {
			// Convert unknown features
			if (oldFeatures.containsKey(cargoAllocation)) {

				final AnyType anyType = oldFeatures.get(cargoAllocation);
				EObject loadAllocation = null;
				EObject dischargeAllocation = null;

				Object loadVolume = null;
				Object dischargeVolume = null;

				EObject ladenJourney = null;
				EObject ladenIdle = null;
				EObject ballastJourney = null;
				EObject ballastIdle = null;

				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Copy value from attributeB to attributeC
					if (eStructuralFeature.getName().equals("loadAllocation")) {
						Object o = schedule.eResource().getEObject((String) entry.getValue());
						// Non-contained / non UUID object ref is stored as uri fragment
						loadAllocation = (EObject) schedule.eResource().getEObject((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("dischargeAllocation")) {
						// Non-contained / non UUID object ref is stored as uri fragment
						dischargeAllocation = (EObject) (EObject) schedule.eResource().getEObject((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("ladenLeg")) {
						// Non-contained / non UUID object ref is stored as uri fragment
						ladenJourney = (EObject) schedule.eResource().getEObject((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("ladenIdle")) {
						// Non-contained / non UUID object ref is stored as uri fragment
						ladenIdle = (EObject) schedule.eResource().getEObject((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("ballastLeg")) {
						// Non-contained / non UUID object ref is stored as uri fragment
						ballastJourney = (EObject) schedule.eResource().getEObject((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("ballastIdle")) {
						// Non-contained / non UUID object ref is stored as uri fragment
						ballastIdle = (EObject) schedule.eResource().getEObject((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("loadVolume")) {
						loadVolume = Integer.parseInt((String) entry.getValue());
						iter.remove();
					} else if (eStructuralFeature.getName().equals("dischargeVolume")) {
						dischargeVolume = Integer.parseInt((String) entry.getValue());
						iter.remove();
					}
				}

				ArrayList<EObject> allocationList = Lists.newArrayList(loadAllocation, dischargeAllocation);
				// Ensure no nulls!
				allocationList.remove(null);
				allocationList.remove(null);
				cargoAllocation.eSet(feature_CargoAllocation_slotAllocations, allocationList);

				ArrayList<EObject> eventList = Lists.newArrayList(ladenJourney, ladenIdle, ballastJourney, ballastIdle);
				// Ensure no nulls!
				eventList.remove(null);
				eventList.remove(null);
				eventList.remove(null);
				eventList.remove(null);
				cargoAllocation.eSet(feature_CargoAllocation_events, eventList);
				if (loadAllocation != null) {
					loadAllocation.eSet(feature_SlotAllocation_volumeTransferred, loadVolume);
				}
				if (dischargeAllocation != null) {
					dischargeAllocation.eSet(feature_SlotAllocation_volumeTransferred, dischargeVolume);
				}
			}
		}
	}
}
