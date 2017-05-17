/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.migration.units;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.migration.AbstractMigrationUnit;
import com.mmxlabs.models.lng.migration.ModelsLNGMigrationConstants;
import com.mmxlabs.models.migration.utils.EObjectWrapper;
import com.mmxlabs.models.migration.utils.MetamodelLoader;

public class MigrateToV73 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 72;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 73;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {

		final EObjectWrapper analyticsModel = model.getRef("analyticsModel");
		if (analyticsModel == null) {
			return;
		}
		List<EObjectWrapper> toModify = new LinkedList<EObjectWrapper>();
		{
			final List<EObjectWrapper> l = analyticsModel.getRefAsList("actionableSetPlans");
			if (l != null) {
				toModify.addAll(l);
			}
		}
		{
			final List<EObjectWrapper> l = analyticsModel.getRefAsList("insertionOptions");
			if (l != null) {
				toModify.addAll(l);
			}
		}

		// Generate a persistent UUID. We will automatically create one on load, but it will change each time, so create and persist one here.
		for (final EObjectWrapper obj : toModify) {
			obj.setAttrib("uuid", EcoreUtil.generateUUID());
		}

	}
}
