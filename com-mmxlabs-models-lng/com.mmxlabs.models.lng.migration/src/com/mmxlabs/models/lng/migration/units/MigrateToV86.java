/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV86 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 85;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 86;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final MetamodelLoader loader = modelRecord.getMetamodelLoader();
		final EObjectWrapper model = modelRecord.getModelRoot();
		final EObjectWrapper analyticsModel = model.getRef("analyticsModel");
		if (analyticsModel == null) {
			return;
		}
		final List<EObjectWrapper> optimisations = analyticsModel.getRefAsList("optimisations");
		if (optimisations == null) {
			return;
		}
		final EPackage package_AnalyticsPackage = loader.getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		final EClass class_SlotInsertionOptions = MetamodelUtils.getEClass(package_AnalyticsPackage, "SlotInsertionOptions");
		final EClass class_ActionableSetPlan = MetamodelUtils.getEClass(package_AnalyticsPackage, "ActionableSetPlan");
		for (final EObjectWrapper solutionSet : optimisations) {
			boolean moveFirst = false;
			final List<EObjectWrapper> results = solutionSet.getRefAsList("options");
			if (class_SlotInsertionOptions.isInstance(solutionSet)) {
				moveFirst = true;
			} else if (class_ActionableSetPlan.isInstance(solutionSet)) {
				moveFirst = true;
			} else {
				if (results != null) {
					if (results.size() > 1) {
						moveFirst = true;
					}
				}
			}

			if (results != null && moveFirst && solutionSet.getRef("baseOption") == null) {
				final List<EObjectWrapper> copy = new ArrayList<>(results);
				final EObjectWrapper base = copy.remove(0);
				solutionSet.setRef("baseOption", base);
				solutionSet.setRef("options", copy);
			}
		}
	}
}
