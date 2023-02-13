/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.MigrationModelRecord;
import com.mmxlabs.models.migration.utils.EObjectWrapper;

public class MigrateToV141 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 140;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 141;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {
		final @NonNull EObjectWrapper modelRoot = modelRecord.getModelRoot();
		final EObjectWrapper adpModel = modelRoot.getRef("adpModel");
		if (adpModel != null) {
			final EObjectWrapper mullProfile = adpModel.getRef("mullProfile");
			if (mullProfile != null) {
				final List<EObjectWrapper> inventoryRows = mullProfile.getRefAsList("inventories");
				if (inventoryRows != null) {
					for (final EObjectWrapper inventoryRow : inventoryRows) {
						final List<EObjectWrapper> entityRows = inventoryRow.getRefAsList("entityTable");
						if (entityRows != null) {
							for (final EObjectWrapper entityRow : entityRows) {
								entityRow.unsetFeature("ports");
							}
						}
					}
				}
			}
		}
	}
}