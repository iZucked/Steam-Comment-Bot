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

public class MigrateToV74 extends AbstractMigrationUnit {

	@Override
	public String getScenarioContext() {
		return ModelsLNGMigrationConstants.Context;
	}

	@Override
	public int getScenarioSourceVersion() {
		return 73;
	}

	@Override
	public int getScenarioDestinationVersion() {
		return 74;
	}

	@Override
	protected void doMigrationWithHelper(final MetamodelLoader loader, final EObjectWrapper model) {
		final EObjectWrapper referenceModel = model.getRef("referenceModel");
		final EObjectWrapper spotMarketsModel = referenceModel.getRef("spotMarketsModel");
		if (spotMarketsModel == null) {
			return;
		}
		List<EObjectWrapper> toModify = new LinkedList<EObjectWrapper>();
		{
			final List<EObjectWrapper> l = spotMarketsModel.getRefAsList("charterInMarkets");
			if (l != null) {
				toModify.addAll(l);
			}
		}

		// set all charter ins to nominal
		for (final EObjectWrapper obj : toModify) {
			obj.setAttrib("nominal", true);
		}

	}
}
