/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV1 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getSourceVersion() {
		return 0;
	}

	@Override
	public int getDestinationVersion() {
		return -1;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader() {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.getV0Loader();
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader() {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.getV1Loader();
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final Map<ModelsLNGSet_v1, EObject> models) {

		final MetamodelLoader v1Loader = getDestinationMetamodelLoader();
		migrateFixedPrice(v1Loader, models);
		removeOptimisedSchedule(v1Loader, models);
		clearAssignments(v1Loader, models);
		removeExtraAnalyticsFields(v1Loader, models);
	}

	public void migrateFixedPrice(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject cargoModel = models.get(ModelsLNGSet_v1.Cargo);
		final EPackage cargoPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);

		final EClass class_Slot = MetamodelUtils.getEClass(cargoPackage, "Slot");
		final EStructuralFeature feature_priceExpression = MetamodelUtils.getStructuralFeature(class_Slot, "priceExpression");

		final EClass class_CargoModel = MetamodelUtils.getEClass(cargoPackage, "CargoModel");
		final EStructuralFeature feature_loadSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "loadSlots");
		final EStructuralFeature feature_dischargeSlots = MetamodelUtils.getStructuralFeature(class_CargoModel, "dischargeSlots");

		final Map<EObject, AnyType> oldFeatures = ((XMLResource) cargoModel.eResource()).getEObjectToExtensionMap();

		@SuppressWarnings("unchecked")
		final EList<EObject> loadSlots = (EList<EObject>) cargoModel.eGet(feature_loadSlots);
		for (final EObject slot : loadSlots) {
			// Convert unknown features
			if (oldFeatures.containsKey(slot)) {

				final AnyType anyType = oldFeatures.get(slot);

				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Copy value from attributeB to attributeC
					if (eStructuralFeature.getName().equals("fixedPrice")) {
						if (!slot.eIsSet(feature_priceExpression)) {
							slot.eSet(feature_priceExpression, entry.getValue());
						}
						iter.remove();
					}
				}
			}
		}

		@SuppressWarnings("unchecked")
		final EList<EObject> dischargeSlots = (EList<EObject>) cargoModel.eGet(feature_dischargeSlots);
		for (final EObject slot : dischargeSlots) {
			// Convert unknown features
			if (oldFeatures.containsKey(slot)) {

				final AnyType anyType = oldFeatures.get(slot);
				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Copy value from attributeB to attributeC
					if (eStructuralFeature.getName().equals("fixedPrice")) {
						if (!slot.eIsSet(feature_priceExpression)) {
							slot.eSet(feature_priceExpression, entry.getValue());
						}
						iter.remove();
					}
				}
			}
		}

	}

	public void removeOptimisedSchedule(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject scheduleModel = models.get(ModelsLNGSet_v1.Schedule);
		final EPackage schedulePackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScheduleModel);

		final EClass class_ScheduleModel = MetamodelUtils.getEClass(schedulePackage, "ScheduleModel");
		final EStructuralFeature feature_optimisedSchedule = MetamodelUtils.getStructuralFeature(class_ScheduleModel, "optimisedSchedule");

		scheduleModel.eUnset(feature_optimisedSchedule);
	}

	public void clearAssignments(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
		final EObject inputModel = models.get(ModelsLNGSet_v1.Input);
		final EPackage inputPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_InputModel);

		final EClass class_InputModel = MetamodelUtils.getEClass(inputPackage, "InputModel");
		final EStructuralFeature feature_assignments = MetamodelUtils.getStructuralFeature(class_InputModel, "assignments");

		inputModel.eUnset(feature_assignments);
	}

	public void removeExtraAnalyticsFields(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {

		final EObject analyticsModel = models.get(ModelsLNGSet_v1.Analytics);
		if (analyticsModel == null) {
			return;
		}
		final EPackage analyticsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);

		final EClass class_AnalyticsModel = MetamodelUtils.getEClass(analyticsPackage, "AnalyticsModel");
		final EStructuralFeature feature_roundTripMatrices = MetamodelUtils.getStructuralFeature(class_AnalyticsModel, "roundTripMatrices");

		final EClass class_UnitCostMatrix = MetamodelUtils.getEClass(analyticsPackage, "UnitCostMatrix");
		final EStructuralFeature feature_toPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "toPorts");
		final EStructuralFeature feature_fromPorts = MetamodelUtils.getStructuralFeature(class_UnitCostMatrix, "fromPorts");

		final List<EObject> matrices = MetamodelUtils.getValueAsTypedList(analyticsModel, feature_roundTripMatrices);

		final Map<EObject, AnyType> oldFeatures = ((XMLResource) analyticsModel.eResource()).getEObjectToExtensionMap();

		for (final EObject matrix : matrices) {
			// Convert unknown features
			if (oldFeatures.containsKey(matrix)) {

				final AnyType anyType = oldFeatures.get(matrix);

				for (final Iterator<FeatureMap.Entry> iter = anyType.getAnyAttribute().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();
					// Unset times
					if (eStructuralFeature.getName().equals("returnIdleTime")) {
						iter.remove();
					}
					if (eStructuralFeature.getName().equals("dischargeIdleTime")) {
						iter.remove();
					}

				}

				// Each item in the original list is an entry in the iterator, so we need to build up a copy of the list before we can use it.
				final List<EObject> ports = new ArrayList<EObject>();
				for (final Iterator<FeatureMap.Entry> iter = anyType.getAny().iterator(); iter.hasNext();) {
					final FeatureMap.Entry entry = iter.next();
					final EStructuralFeature eStructuralFeature = entry.getEStructuralFeature();

					if (eStructuralFeature.getName().equals("ports")) {

						final EObject port = (EObject) entry.getValue();
						ports.add(port);

						// Clear old reference
						iter.remove();
					}

				}
				// Clone ports list into to/from.
				if (!matrix.eIsSet(feature_toPorts)) {
					matrix.eSet(feature_toPorts, ports);
				}
				if (!matrix.eIsSet(feature_fromPorts)) {
					matrix.eSet(feature_fromPorts, ports);
				}
			}
		}

	}
}
