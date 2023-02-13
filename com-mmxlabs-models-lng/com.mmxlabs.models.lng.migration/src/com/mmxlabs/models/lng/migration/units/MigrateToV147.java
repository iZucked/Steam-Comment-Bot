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

public class MigrateToV147 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 146;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 147;
	}

	@Override
	protected void doMigration(@NonNull final MigrationModelRecord modelRecord) {

		final @NonNull EObjectWrapper scenarioModel = modelRecord.getModelRoot();

		final EObjectWrapper analyticsModel = scenarioModel.getRef("analyticsModel");

		if (analyticsModel != null) {
			final List<EObjectWrapper> sandboxes = analyticsModel.getRefAsList("optionModels");
			if (sandboxes != null) {
				for (final EObjectWrapper sandbox : sandboxes) {
					final EObjectWrapper baseCase = sandbox.getRef("baseCase");
					if (baseCase != null) {
						final List<EObjectWrapper> rows = baseCase.getRefAsList("baseCase");
						if (rows != null) {
							for (final EObjectWrapper row : rows) {
								row.setAttrib("optionise", Boolean.TRUE);
							}
						}
					}
				}
			}
		}
	}
}