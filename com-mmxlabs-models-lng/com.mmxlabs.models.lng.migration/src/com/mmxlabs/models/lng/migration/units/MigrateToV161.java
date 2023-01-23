/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV161 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 160;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 161;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EPackage analyticsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);

		final EClass clsExistingVesselCharterOption = MetamodelUtils.getEClass(analyticsPackage, "ExistingVesselCharterOption");
		final EClass clsFullVesselCharterOption = MetamodelUtils.getEClass(analyticsPackage, "FullVesselCharterOption");

		final EObjectWrapper analysisModel = scenarioModel.getRef("analyticsModel");

		final TreeIterator<EObject> itr = analysisModel.eAllContents();
		while (itr.hasNext()) {
			final EObject obj = itr.next();

			// Move temp fields back to correct name
			if (clsFullVesselCharterOption.isInstance(obj) || clsExistingVesselCharterOption.isInstance(obj)) {
				final EObjectWrapper option = (EObjectWrapper) obj;
				option.setRef("vesselCharter", option.getRef("vesselCharterTmp"));
				option.unsetFeature("vesselCharterTmp");
			}
		}

		// Part 2 - rename VesselClassRouteParameters to VesselRouteParameters
		{
			final EObjectWrapper referenceModel = scenarioModel.getRef("referenceModel");
			final EObjectWrapper fleetModel = referenceModel.getRef("fleetModel");

			final List<EObjectWrapper> vessels = fleetModel.getRefAsList("vessels");
			if (vessels != null) {
				for (final EObjectWrapper vessel : vessels) {
					final List<EObjectWrapper> routeParameters = vessel.getRefAsList("routeParametersTmp");
					if (routeParameters != null) {
						vessel.setRef("routeParameters", routeParameters);
						vessel.unsetFeature("routeParametersTmp");
					}
				}
			}
		}
	}
}