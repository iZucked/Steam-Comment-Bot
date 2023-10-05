/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelUtils;

public class MigrateToV197 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 196;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 197;
	}

	@Override
	protected void doMigration(final MigrationModelRecord modelRecord) {
		// Unset arrive cold on generated spot slots.

		final EPackage analyticsPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_AnalyticsModel);
		final EClass clsAbstractSolutionSet = MetamodelUtils.getEClass(analyticsPackage, "AbstractSolutionSet");
		final EPackage cargoPackage = modelRecord.getMetamodelLoader().getPackageByNSURI(ModelsLNGMigrationConstants.NSURI_CargoModel);
		final EClass clsSpotLoadSlot = MetamodelUtils.getEClass(cargoPackage, "SpotLoadSlot");

		final EObjectWrapper modelRoot = modelRecord.getModelRoot();

		final EObjectWrapper analyticsModel = modelRoot.getRef("analyticsModel");
		var treeItr = analyticsModel.eAllContents();
		while (treeItr.hasNext()) {
			final var c = treeItr.next();
			if (clsAbstractSolutionSet.isInstance(c)) {
				final List<EObjectWrapper> extraSlots = ((EObjectWrapper) c).getRefAsList("extraSlots");
				if (extraSlots != null) {
					for (final var ls : extraSlots) {
						if (clsSpotLoadSlot.isInstance(ls)) {
							ls.unsetFeature("arriveCold");
						}
					}
				}
				treeItr.prune();
			}
		}
	}
}
