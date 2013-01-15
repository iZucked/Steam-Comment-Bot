/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.internal;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil;
import com.mmxlabs.models.lng.migration.MetamodelVersionsUtil.ModelsLNGSet_v1;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV1 extends AbstractMigrationUnit {

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
	protected MetamodelLoader createSourceMetamodelLoader() {
		return MetamodelVersionsUtil.getV0Loader();
	}

	@Override
	protected MetamodelLoader createDestinationMetamodelLoader() {
		return MetamodelVersionsUtil.getV1Loader();
	}

	@Override
	protected void doMigration(final Map<ModelsLNGSet_v1, EObject> models) {

		final MetamodelLoader v1Loader = createDestinationMetamodelLoader();
		clearFixedPrice(v1Loader, models);

	}

	public void clearFixedPrice(final MetamodelLoader loader, final Map<ModelsLNGSet_v1, EObject> models) {
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
}
