/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

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

public class MigrateToV21 extends AbstractMigrationUnit {

	private MetamodelLoader destiniationLoader;
	private MetamodelLoader sourceLoader;

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 20;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 21;
	}

	@Override
	protected MetamodelLoader getSourceMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (sourceLoader == null) {
			sourceLoader = MetamodelVersionsUtil.createV20Loader(extraPackages);
		}
		return sourceLoader;
	}

	@Override
	protected MetamodelLoader getDestinationMetamodelLoader(final Map<URI, PackageData> extraPackages) {
		if (destiniationLoader == null) {
			destiniationLoader = MetamodelVersionsUtil.createV21Loader(extraPackages);
		}
		return destiniationLoader;
	}

	@Override
	protected void doMigration(final EObject model) {

		final MetamodelLoader modelLoader = getDestinationMetamodelLoader(null);
		modifyEquivalenceFactor(modelLoader, model);

	}

	private void modifyEquivalenceFactor(final MetamodelLoader modelLoader, final EObject model) {
		final EPackage package_ScenarioModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_ScenarioModel);
		final EPackage package_FleetModel = modelLoader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_FleetModel);

		final EClass class_LNGScenarioModel = MetamodelUtils.getEClass(package_ScenarioModel, "LNGScenarioModel");
		final EClass class_FleetModel = MetamodelUtils.getEClass(package_FleetModel, "FleetModel");

		final EReference reference_LNGScenarioModel_fleetModel = MetamodelUtils.getReference(class_LNGScenarioModel, "fleetModel");
		
		final EClass class_BaseFuel = MetamodelUtils.getEClass(package_FleetModel, "BaseFuel");

		final EReference reference_FleetModel_baseFuels = MetamodelUtils.getReference(class_FleetModel, "baseFuels");
		
		final EAttribute attribute_BaseFuel_equivalenceFactor = MetamodelUtils.getAttribute(class_BaseFuel, "equivalenceFactor");

		final EObject fleetModel = (EObject) model.eGet(reference_LNGScenarioModel_fleetModel);
		if (fleetModel == null) {
			return;
		}

		final EList<EObject> baseFuels = MetamodelUtils.getValueAsTypedList(fleetModel, reference_FleetModel_baseFuels);

		if (baseFuels != null) {
			for (final EObject baseFuel : baseFuels) {
				double currentEquiv = (Double) baseFuel.eGet(attribute_BaseFuel_equivalenceFactor);
				baseFuel.eSet(attribute_BaseFuel_equivalenceFactor, getDefaultEquivalence(currentEquiv));
			}
		}

	}

	private double getDefaultEquivalence(double equiv) {
		return 22.8/equiv; 
	}
	
}
